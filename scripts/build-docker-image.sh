#!/usr/bin/env bash

set -euo pipefail

docker build -t whereisivan:latest -f infra/docker/Dockerfile .
echo "Docker image whereisivan:latest built successfully"