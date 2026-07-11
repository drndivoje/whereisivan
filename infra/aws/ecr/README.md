# whereisivan — ECR Tier

Terraform configuration that provisions the Amazon ECR repository used to store the whereisivan backend Docker image.

This is the **ECR tier**. It has no dependency on the [app tier](../app/README.md) — apply this tier first, then push an image (`make push-image`) before applying the app tier for the first time.

## Overview

This module creates:

- An ECR repository (`whereisivan-backend` by default) with `force_delete = true`, so `terraform destroy` doesn't fail if images are still present

## Prerequisites

| Tool | Version |
|------|---------|
| Terraform | >= 1.0 |
| AWS CLI | 2.x |
| AWS provider | >= 6.0 |
| Docker | with `buildx` (for building the `linux/arm64` image) |

An AWS CLI profile with permissions to manage ECR must be configured locally.

## Remote State

Terraform state is stored in S3, in the same state bucket as the app tier but under its own key. Copy the example config and fill in your bucket details:

```bash
cp state.config.example state.config
```

Edit `state.config`:

```hcl
bucket = "<your-state-bucket-name>"
key    = "<path/to/whereisivan/ecr.tfstate>"
region = "<aws-region>"
```

## Variables

| Name | Description | Default | Required |
|------|-------------|---------|:--------:|
| `aws_profile` | AWS CLI profile name | — | yes |
| `ecr_repository_name` | Name of the ECR repository to create | `whereisivan-backend` | no |
| `aws_region` | AWS region | `eu-central-1` | no |
| `tags` | Map of additional resource tags | `{}` | no |

## Usage

Normally run via Make from the repository root (see root README):

```bash
make deploy-ecr    # terraform apply for this tier
make push-image    # builds infra/docker/Dockerfile for linux/arm64 and pushes :latest
```

To apply directly:

```bash
terraform init -backend-config="state.config"
terraform plan
terraform apply
```

## Outputs

| Name | Description |
|------|-------------|
| `repository_url` | The ECR repository URL (`<account>.dkr.ecr.<region>.amazonaws.com/whereisivan-backend`) — consumed by the app tier via a data source and by `scripts/build-and-push-ecr.sh` |
| `repository_arn` | The ECR repository ARN |

## Resources

| Resource | Type |
|----------|------|
| `aws_ecr_repository.backend` | ECR repository for the backend image |
