# whereisivan — Docker

A multi-stage Dockerfile that builds the complete whereisivan application (dashboard + backend) into a single container image.

## Overview

The build has three stages:

1. **dashboard-builder** — installs Node.js dependencies and compiles the React app.
2. **backend-builder** — copies the compiled dashboard assets into the backend's resource directory and builds a fat JAR with Gradle.
3. **Runtime** — runs the fat JAR on a minimal Eclipse Temurin 25 JRE image.

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

Alternatively, use Docker Compose directly:

```bash
docker compose -f infra/docker/docker-compose.yml up --build
```

## Building the Image Manually

From the **repository root** (the Dockerfile uses the repo root as its build context):

```bash
docker build -f infra/docker/Dockerfile -t whereisivan-backend .
```

Run the container:

```bash
docker run -p 80:8080 whereisivan-backend
```

## Notes

- The Dockerfile must be built with the repository root as the build context because it copies from both `dashboard/` and `backend/`.
- The Ktor Gradle plugin can also produce a local Docker image via `./gradlew publishImageToLocalRegistry` (from the `backend/` directory), but that path requires the dashboard to be pre-built and copied separately. The `infra/docker/Dockerfile` handles both steps in one pass.
