#!/bin/bash
set -e

# Forces the EC2 instance to be replaced so its user_data re-runs and pulls
# the freshly pushed :latest image. Run `make push-image` first.

cd "$(dirname "$0")/../infra/aws/app" || exit 1

terraform init -backend-config="./state.config"
terraform apply -replace="module.ec2.aws_instance.this" -auto-approve
