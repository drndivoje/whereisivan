# whereisivan — Backend

The Ktor-based server that receives GPS location updates from the Android client, stores activity state in memory, and serves the dashboard as embedded static assets.

## Overview

The backend exposes a REST API over HTTP. The Android client POSTs location updates to the activity endpoints; the React dashboard reads from those same endpoints. In production the dashboard's compiled static files are bundled into the JAR, so a single process serves both the API and the web UI.

OpenAPI documentation is served at `/api/openapi` when the server is running.

## Prerequisites

| Tool | Version |
|------|---------|
| JDK | 25 (Eclipse Temurin recommended) |
| Kotlin | 2.4.0 |
| Gradle | bundled via `./gradlew` |

## Build and Run

```bash
# Start the development server (hot-reload via Ktor development mode)
./gradlew run

# Compile and run all tests
./gradlew build

# Run tests only
./gradlew test

# Produce a deployable fat JAR
./gradlew buildFatJar

# Build and push a Docker image to the local registry
./gradlew publishImageToLocalRegistry
```

The development server starts on **port 8080** by default.

## Configuration

The application reads `src/main/resources/application.conf` (HOCON). No mandatory environment variables are required for local development.

## Bundling the Dashboard

The build pipeline copies the compiled React app into `src/main/resources/dashboard-app/` before building the JAR. To produce a fully self-contained JAR:

```bash
# From the repository root
make build-dashboard   # compiles the React app and copies assets here
make build-backend     # builds the fat JAR with the dashboard embedded
```

Running `./gradlew buildFatJar` directly will embed whatever assets are already present in `src/main/resources/dashboard-app/` — run `make build-dashboard` first if you need a fresh frontend.

## Docker

A Docker image named `whereisivan-backend` is built by the Ktor Gradle plugin:

```bash
./gradlew publishImageToLocalRegistry
```

The image maps **host port 80 to container port 8080**. To run locally using Docker Compose (from the repository root):

```bash
make local-run
```

## Project Structure

```
src/main/kotlin/rocks/drnd/whereisivan/
  Application.kt          # Entry point, server configuration
  plugins/                # Ktor plugin setup (serialization, CORS, DI)
  route/
    ActivityRoutes.kt     # Activity CRUD and location update endpoints
    UserRoutes.kt         # User management endpoints
    DashboardRoutes.kt    # Serves embedded dashboard static assets
    OpenApiRoute.kt       # Serves OpenAPI spec
  model/                  # Data classes
  impl/                   # Business logic implementations
```

## Tech Stack

| Component | Library / Version |
|-----------|-------------------|
| Framework | Ktor 3.1.3 |
| Language | Kotlin 2.4.0 |
| DI | Koin 3.5.1 |
| Serialization | kotlinx.serialization |
| Server engine | Netty |
| JVM | 25 |
