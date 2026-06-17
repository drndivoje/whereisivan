#!/bin/bash

# Navigate to the infra folder
cd "$(dirname "$0")/../infra/aws" || exit 1

terraform init -backend-config="./state.config"
# Run terraform destroy
terraform destroy --auto-approve