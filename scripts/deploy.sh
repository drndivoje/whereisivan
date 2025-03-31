#!/bin/bash

# Navigate to the infra folder
cd "$(dirname "$0")/../infra" || exit 1

terraform init -backend-config="./state.config"
# Run terraform apply
terraform apply -auto-approve