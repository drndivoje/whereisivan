# whereisivan — Docker

A multi-stage Dockerfile that builds the complete whereisivan application (dashboard + backend) into a single container image.

## Overview

The build has three stages:

1. **dashboard-builder** — installs Node.js dependencies and compiles the React app.
2. **backend-builder** — copies the compiled dashboard assets into the backend resource directory and builds a fat JAR with Gradle.
3. **runtime** — runs the fat JAR on a minimal Eclipse Temurin 25 JRE image.

The resulting container serves both the API and the dashboard static files on **port 8080**.

## Prerequisites

| Tool | Version |
|------|---------|
| Docker | 24 or later |
| Docker Compose | v2 |

## Running with Docker Compose

From the **repository root**, run:

```bash
make local-run
```

This builds the image and starts the container with host port 80 mapped to container port 8080. The application is then available at **http://localhost**.


## Building the Image Manually

To build the image locally from the **repository root**, run:

```bash
make build-docker-image
```

Run the container (this will build the image again):

```bash
make local-run
```

This is a local Docker build, so it builds images for the same platform as the host machine.

## Deployment to AWS

To deploy the app to AWS from the **repository root**, run:

```bash
make deploy
```

The AWS deployment consists of two tiers:

1. **ECR** — stores the Docker image for the application container.
2. **EC2 compute tier** — runs Docker, pulls the image from ECR, and starts the container.

**Note:** Docker image builds for deployment target ARM. Building ARM images from an amd64 host can be much slower because Docker uses emulation (see [tonistiigi/binfmt](https://github.com/tonistiigi/binfmt)).

## Notes

- The Dockerfile must be built with the repository root as the build context because it copies from both `dashboard/` and `backend/`.
- The Ktor Gradle plugin can also produce a local Docker image via `./gradlew publishImageToLocalRegistry` (from the `backend/` directory), but that path requires the dashboard to be pre-built and copied separately. The `infra/docker/Dockerfile` handles both steps in one pass.
