# whereisivan — Dashboard

A React single-page application that visualises live cycling activity data from the whereisivan backend on an interactive Leaflet map.

## Overview

The dashboard has two screens:

1. **Activity List** — lists all activities currently tracked by the server.
2. **Current Location Map** — shows the cyclist's live position on a Leaflet map, updating in real time.

In production the compiled static assets are embedded in the backend JAR and served by Ktor. During development the React dev server proxies API requests to the backend.

## Prerequisites

| Tool | Version |
|------|---------|
| Node.js | 18 or later |
| npm | 9 or later |

## Setup

```bash
npm install
```

## Development

```bash
npm start
```

Opens the app at **http://localhost:3000**. The page reloads automatically on file changes.

By default the dev server expects the backend to be running on **http://localhost:8080**. Update the `proxy` field in `package.json` if your backend runs on a different port.

## Testing

```bash
npm test
```

Runs the test suite in interactive watch mode using Jest and React Testing Library.

## Production Build

```bash
npm run build
```

Outputs optimised static files to the `build/` directory. These assets are copied into the backend module during a full `make build-dashboard` run.

## Tech Stack

| Component | Library / Version |
|-----------|-------------------|
| Framework | React 19.2.7 |
| Map | react-leaflet 5.0.0, leaflet 1.9.4 |
| Routing | react-router-dom 7.17.0 |
| Build tooling | Create React App (react-scripts 5.0.1) |
| Testing | Jest, React Testing Library 16.2.0 |
