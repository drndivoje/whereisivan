# whereisivan — Claude Code Guide

A personal bicycle tracking app that streams real-time GPS data from an Android device to a Ktor backend and visualises it on a React/Leaflet dashboard. Infrastructure is managed with Terraform on AWS.

## Repository Layout

```
android-client/   # Kotlin + Jetpack Compose Android app
backend/          # Kotlin + Ktor server (JVM 25)
dashboard/        # React 18 + react-leaflet web app
infra/            # Terraform (AWS EC2, S3, Route53)
test-client/      # CLI GPX simulator for local testing
scripts/          # Shell helpers used by Make targets
Makefile          # Top-level build / deploy orchestration
```

## Common Commands

### Backend (Kotlin / Ktor)
```bash
cd backend
./gradlew run                  # start dev server
./gradlew build                # compile + test
./gradlew test                 # run tests only
./gradlew buildFatJar          # produce deployable JAR
./gradlew publishImageToLocalRegistry  # build Docker image
```

### Dashboard (React)
```bash
cd dashboard
npm start          # dev server (http://localhost:3000)
npm test           # run tests
npm run build      # production build → build/
```

### Android Client
```bash
cd android-client
./gradlew assembleDebug    # build debug APK
./gradlew test             # unit tests
```

### Test Client (GPX simulator)
```bash
cd test-client
./gradlew run --args="<gpx-file> <backend-url>"
```

### Full Build & Deploy (from repo root)
```bash
make build-dashboard   # builds React app
make build-backend     # bundles dashboard into JAR, builds backend
make deploy            # runs Terraform apply
make destroy           # tears down AWS infra
```

## Architecture

- **Android client** captures GPS location and POSTs location updates to the backend API.
- **Backend** (Ktor + Koin DI, Netty) stores activity/user state in-memory and exposes REST endpoints. Key route files: `ActivityRoutes.kt`, `UserRoutes.kt`, `DashboardRoutes.kt`. OpenAPI docs served via `OpenApiRoute.kt`.
- **Dashboard** React SPA with react-leaflet map. Two screens: Activity List and live Current Location Map. Served as static assets embedded in the backend JAR for deployment.
- **Infra** Terraform provisions an EC2 instance, S3 bucket (stores the JAR), IAM policy, security group, and Route53 subdomain. Default region: `eu-central-1`.

## Tech Stack

| Layer | Technologies |
|---|---|
| Backend | Kotlin 2.4.0, Ktor 3.1.3, Koin, kotlinx.serialization, Netty, JVM 21 |
| Android | Kotlin, Jetpack Compose |
| Dashboard | React 18, react-leaflet 4, react-router-dom 6 |
| Infra | Terraform, AWS (EC2, S3, Route53) |
| Packaging | Docker (backend image: `whereisivan-backend`) |

## Infra Variables

When running Terraform you must supply:

| Variable | Description |
|---|---|
| `aws_profile` | AWS CLI profile name |
| `backend_jar_path` | Path to the compiled backend JAR |
| `route53_zone` | Route53 hosted zone name |
| `aws_region` | Defaults to `eu-central-1` |

## Notes

- The backend JAR embeds the built dashboard static assets — run `make build-dashboard` before `make build-backend` when updating the frontend.
- The test-client takes a `.gpx` file and a backend URL, replaying the track to simulate the Android client; useful for local development without a physical device.
- Backend Docker image maps host port 80 → container port 8080.
