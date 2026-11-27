#!/bin/bash

set -euo pipefail

if [[ -z "${TILES_URL:-}" ]]; then
  echo "Error: TILES_URL environment variable is missing" >&2
  exit 1
fi

echo "Building Frontend..."
cd dashboard
rm -rf dist
export REACT_APP_TILE_URL="$TILES_URL"
npm install > /dev/null 2>&1 && npm run build > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi
rm -rf ../backend/src/main/resources/dashboard-app
cp -r dist ../backend/src/main/resources/dashboard-app
echo "Build succeeded"
