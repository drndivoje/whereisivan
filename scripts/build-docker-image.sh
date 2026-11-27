#!/usr/bin/env bash

set -euo pipefail

if [[ -z "${TILES_URL:-}" ]]; then
  echo "Error: TILES_URL environment variable is missing" >&2
  exit 1
fi

docker build --build-arg REACT_APP_TILE_URL="$TILES_URL" -t whereisivan:latest -f infra/docker/Dockerfile .
echo "Docker image whereisivan:latest built successfully"