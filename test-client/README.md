# whereisivan — Test Client

A command-line GPX simulator that replays a recorded cycling track against the whereisivan backend API, allowing end-to-end testing without a physical Android device.

## Overview

The test client reads a `.gpx` file, parses its waypoints, and POSTs each location update to the backend in sequence. This replicates the behaviour of the Android client, making it useful for local development and integration testing.

## Prerequisites

| Tool | Version |
|------|---------|
| JDK | 25 |
| Kotlin | 2.4.0 |
| Gradle | bundled via `./gradlew` |

## Usage

```bash
./gradlew run --args="<path-to-file.gpx> <backend-url>"
```

**Example:**

```bash
./gradlew run --args="tracks/morning-ride.gpx http://localhost:8080"
```

The client prints progress to stdout as it sends each waypoint.

## Build a Standalone JAR

```bash
./gradlew jar
```

The fat JAR is output to `build/libs/`. Run it directly:

```bash
java -jar build/libs/test-client-1.0-SNAPSHOT.jar <path-to-file.gpx> <backend-url>
```

## Tech Stack

| Component | Library / Version |
|-----------|-------------------|
| Language | Kotlin 2.4.0 |
| HTTP client | Ktor Client CIO 3.1.0 |
| XML parsing | javax.xml.parsers (JDK built-in) |
| JVM | 25 |
