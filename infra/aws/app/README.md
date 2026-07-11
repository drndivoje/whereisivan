# whereisivan — App Tier (EC2)

Terraform configuration that provisions the AWS resources required to run the whereisivan backend container on EC2.

This is the **app tier**. It depends on the [ECR tier](../ecr/README.md) already existing — the EC2 instance looks up the ECR repository by name via a data source, so the ECR tier must be applied at least once before this tier's first `apply`.

## Overview

This module creates:

- An EC2 instance (Amazon Linux 2, ARM64/Graviton) running the backend as a Docker container
- An IAM policy and role attachment granting the EC2 instance permission to authenticate to and pull from ECR
- A security group permitting inbound traffic on port 8080 (API) and port 22 (SSH via managed prefix list)
- A Route53 A record pointing a subdomain at the EC2 instance

The EC2 instance bootstraps itself on first launch: it installs Docker, logs in to ECR, and runs the backend image.

> **Note:** `user_data` only runs on first boot. Pushing a new `:latest` image to ECR does **not** update a running instance — use `make redeploy` to replace the instance so it re-runs `user_data` and pulls the fresh image.

## Prerequisites

| Tool | Version |
|------|---------|
| Terraform | >= 1.0 |
| AWS CLI | 2.x |
| AWS provider | >= 6.0 |

An AWS CLI profile with permissions to manage EC2, IAM, and Route53 must be configured locally. The ECR repository referenced by `ecr_repository_name` must already exist (see the [ECR tier](../ecr/README.md)).

## Remote State

Terraform state is stored in S3. Copy the example config and fill in your bucket details:

```bash
cp state.config.example state.config
```

Edit `state.config`:

```hcl
bucket = "<your-state-bucket-name>"
key    = "<path/to/whereisivan/app.tfstate>"
region = "<aws-region>"
```

## Variables

| Name | Description | Default | Required |
|------|-------------|---------|:--------:|
| `aws_profile` | AWS CLI profile name | — | yes |
| `route53_zone` | Route53 hosted zone name (e.g. `example.com`) | — | yes |
| `ecr_repository_name` | Name of the ECR repository to pull from (must match the ECR tier) | `whereisivan-backend` | no |
| `image_tag` | Image tag to pull from ECR | `latest` | no |
| `aws_region` | AWS region | `eu-central-1` | no |
| `tags` | Map of additional resource tags | `{}` | no |

## Usage

Normally run via Make from the repository root (see root README). To apply directly:

```bash
# Initialise with remote state backend
terraform init -backend-config="state.config"

# Preview changes
terraform plan

# Apply
terraform apply
```

To force the instance to pick up a newly pushed `:latest` image:

```bash
terraform apply -replace="module.ec2.aws_instance.this"
```

To tear down all resources:

```bash
terraform destroy
```

## Outputs

| Name | Description |
|------|-------------|
| `dashboard_url` | Public URL of the deployed dashboard |

## Resources

| Resource | Type |
|----------|------|
| `data.aws_ecr_repository.backend` | Looks up the ECR repo created by the ECR tier |
| `aws_iam_policy.ecr_pull_policy` | IAM policy granting EC2 permission to pull from ECR |
| `aws_iam_role_policy_attachment.ecr_pull_attachment` | Attaches the policy to the EC2 role |
| `aws_security_group.backend_api` | Security group for the EC2 instance |
| `aws_route53_record.subdomain` | DNS A record for the backend subdomain |
| `module.ec2` | EC2 instance (via `drndivoje/terraform-modules//minimal-ec2`) |
