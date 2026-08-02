# whereisivan — Deployemnt

A multi-stage Dockerfile that builds the complete whereisivan application (dashboard + backend) into a single container image. It is possible to deploy locally or on AWS as a docker container

## Deployment Architecture Overview

AWS Deployment is split into two independent Terraform tiers under `infra/aws/`:

1. **ECR tier** (`infra/aws/ecr/`) — provisions the `whereisivan-backend` ECR repository. Applied via `make deploy-ecr`.
2. **App tier** (`infra/aws/app/`) — provisions the EC2 instance (Amazon Linux 2, ARM64/Graviton), security group, IAM role, and Route53 record. Applied via `make deploy-app`. It looks up the ECR repository by name via a Terraform data source, so the ECR tier must exist first.

`scripts/build-and-push-ecr.sh` (via `make push-image`) builds `infra/docker/Dockerfile` for `linux/arm64` with `docker buildx` and pushes it to ECR as `:latest`.

The EC2 instance's `user_data` installs Docker, authenticates to ECR, and runs the image — but this only happens on first boot. Pushing a new `:latest` image does not update an already-running instance; use `make redeploy` to push and then replace the instance (`terraform apply -replace`) so it re-runs `user_data`.

See [`infra/aws/ecr/README.md`](infra/aws/ecr/README.md) and [`infra/aws/app/README.md`](infra/aws/app/README.md) for Terraform variable reference and state backend configuration for each tier.


The Docker build has three stages:

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
