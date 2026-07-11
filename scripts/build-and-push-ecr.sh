#!/bin/bash
set -e

# Navigate to the repository root
cd "$(dirname "$0")/.." || exit 1

echo "Fetching ECR repository URL..."
REPO_URL=$(cd infra/aws/ecr && terraform output -raw repository_url)

if [ -z "$REPO_URL" ]; then
    echo "Could not read repository_url output. Has 'make deploy-ecr' been run?"
    exit 1
fi

REGISTRY="${REPO_URL%%/*}"

echo "Logging in to $REGISTRY..."
aws ecr get-login-password --region eu-central-1 --profile terraform | docker login --username AWS --password-stdin "$REGISTRY"

echo "Building and pushing arm64 image to $REPO_URL:latest..."
docker buildx build --platform linux/arm64 -f infra/docker/Dockerfile -t "$REPO_URL:latest" --push .

echo "Pushed $REPO_URL:latest"
