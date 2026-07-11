#!/bin/bash
set -e

# Usage: destroy.sh <ecr|app>
TIER="$1"
if [[ "$TIER" != "ecr" && "$TIER" != "app" ]]; then
    echo "Usage: $0 <ecr|app>"
    exit 1
fi

# Navigate to the tier's infra folder
cd "$(dirname "$0")/../infra/aws/$TIER" || exit 1

terraform init -backend-config="./state.config"
# Run terraform destroy
terraform destroy --auto-approve
