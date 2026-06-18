#!/bin/bash

docker build -t whereisivan:latest -f infra/docker/Dockerfile .
echo "Docker image whereisivan:latest built successfully"