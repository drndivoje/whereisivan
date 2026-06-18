# whereisivan — Android Client

The Android app that tracks the device's GPS location and streams position updates to the whereisivan backend API.

## Overview

The app runs a background location service that periodically captures the device's GPS coordinates and POSTs them to the configured backend URL. It is built with Jetpack Compose for the UI and uses MVVM architecture with Koin for dependency injection.

## Prerequisites

| Tool | Version |
|------|---------|
| Android Studio | Hedgehog or later |
| Android Gradle Plugin | 9.2.1 |
| Kotlin | 2.2.10 |
| Min SDK | 27 (Android 8.1) |
| Target SDK | 35 |
| Compile SDK | 37 |
| JVM target | 21 |

## Configuration

Before building, create a `local.properties` file in the `android-client/` directory (it is gitignored) and set the backend host:

```properties
REMOTE_HOST=https://your-backend-host.example.com
```

This value is injected into `BuildConfig.REMOTE_BASE_HOST` at compile time for both debug and release builds.

## Build

```bash
# Build a debug APK
./gradlew assembleDebug

# Build a release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests (requires a connected device or emulator)
./gradlew connectedAndroidTest
```

The debug APK is output to `app/build/outputs/apk/debug/app-debug.apk`.

## Project Structure

```
app/src/main/java/rocks/drnd/whereisivan/client/
  WhereIsIvanApplication.kt   # Application class, Koin initialisation
  Domain.kt                   # Domain models
  ui/                         # Compose screens and navigation
  viewmodel/                  # ViewModels (ActivityViewModel, etc.)
  service/                    # Background location service
  network/                    # Ktor HTTP client setup
  repository/                 # Data access layer
  datasource/                 # Remote data sources
  di/                         # Koin modules
```

## Tech Stack

| Component | Library / Version |
|-----------|-------------------|
| UI | Jetpack Compose (BOM 2026.05.01) |
| Navigation | Navigation Compose 2.9.8 |
| DI | Koin Compose 3.5.6 |
| HTTP client | Ktor Client 2.3.11 |
| Serialization | kotlinx.serialization 1.7.3 |
| Location | Google Play Services Location 21.3.0 |
| Local storage | Room 2.8.4 |
| Permissions | Accompanist Permissions 0.32.0 |
