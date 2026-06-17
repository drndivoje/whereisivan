# whereisivan — AWS Infrastructure

Terraform configuration that provisions the AWS resources required to run the whereisivan backend on EC2.

## Overview

This module creates:

- An EC2 instance (Amazon Linux, ARM64) running the backend JAR under Amazon Corretto 21
- An S3 bucket to store the backend JAR artifact
- An IAM policy and role attachment granting the EC2 instance access to S3
- A security group permitting inbound traffic on port 8080 (API) and port 22 (SSH via managed prefix list)
- A Route53 A record pointing a subdomain at the EC2 instance

The EC2 instance bootstraps itself on first launch: it installs Java, pulls the JAR from S3, and starts the application.

## Prerequisites

| Tool | Version |
|------|---------|
| Terraform | >= 1.0 |
| AWS CLI | 2.x |
| AWS provider | >= 6.0 |

An AWS CLI profile with permissions to manage EC2, S3, IAM, and Route53 must be configured locally.

## Remote State

Terraform state is stored in S3. Copy the example config and fill in your bucket details:

```bash
cp state.config.example state.config
```

Edit `state.config`:

```hcl
bucket = "<your-state-bucket-name>"
key    = "<path/to/whereisivan.tfstate>"
region = "<aws-region>"
```

## Variables

| Name | Description | Default | Required |
|------|-------------|---------|:--------:|
| `aws_profile` | AWS CLI profile name | — | yes |
| `backend_jar_path` | Local path to the compiled backend fat JAR | — | yes |
| `route53_zone` | Route53 hosted zone name (e.g. `example.com`) | — | yes |
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
| `aws_s3_bucket.bucket` | S3 bucket for the backend JAR |
| `aws_s3_object.backend_jar` | Backend JAR uploaded to S3 |
| `aws_iam_policy.s3_bucket_policy` | IAM policy granting EC2 read access to S3 |
| `aws_iam_role_policy_attachment.s3_access_attachment` | Attaches the policy to the EC2 role |
| `aws_security_group.backend_api` | Security group for the EC2 instance |
| `aws_route53_record.subdomain` | DNS A record for the backend subdomain |
| `module.ec2` | EC2 instance (via `drndivoje/terraform-modules//minimal-ec2`) |
