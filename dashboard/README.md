# whereisivan — Dashboard

React/Leaflet dashboard for the whereisivan backend. It shows the cyclist's current position on a live map.

## Requirements

- Node.js 20.19+ or 22.12+
- npm
- A backend running at `http://localhost:8080` (for local development)
- **A map tile server URL.** The dashboard has no default tile provider, so you must supply one or the map will not appear (see [Map tiles](#map-tiles-required)).

## Map tiles (required)

The map background comes from a raster tile server that you configure. The URL is read at **build time** from `REACT_APP_TILE_URL` and inlined into the JavaScript bundle by Vite.

If the variable is missing:

| Build path | What happens |
|---|---|
| `npm start` / `npm run build` | The build succeeds, but the map has no tiles and only the marker shows. |
| `make build-dashboard` | Fails with `Error: TILES_URL environment variable is missing`. |
| `make build-docker-image`, `make push-image`, `make deploy`, `make redeploy` | Fails with the same error before the Docker build starts. |

### URL format

The URL must be a Leaflet tile template that contains `{z}`, `{x}` and `{y}`. You can also use `{s}` (subdomain) and `{r}` (`@2x` on retina screens):

```text
https://tile-provider.example/{z}/{x}/{y}{r}.png?api_key=YOUR_API_KEY
```

Most providers require an API key or a subscription. Check their usage policy before using them in production.

### Which variable to set

| Context | Variable | How it reaches the build |
|---|---|---|
| Local dev (`npm start`, `npm run build`) | `REACT_APP_TILE_URL` | Read from the environment or a `.env.local` file in `dashboard/` |
| Make targets / scripts (`build-dashboard.sh`, `build-docker-image.sh`, `build-and-push-ecr.sh`) | `TILES_URL` | The scripts pass it on as `REACT_APP_TILE_URL` (a Docker `--build-arg` for image builds) |

> **Security:** The tile URL, including any API key, ends up in the public browser bundle. Anyone who opens the dashboard can read it. Restrict the key by allowed domain/referrer and set a usage limit with your provider. Never commit real keys: `.env.local` is already in `.gitignore`.

## Setup

```bash
npm install
```

Create `dashboard/.env.local`:

```dotenv
REACT_APP_TILE_URL=https://tile-provider.example/{z}/{x}/{y}{r}.png?api_key=YOUR_API_KEY
```

## Development

```bash
npm start
```

This opens http://localhost:3000. When the app runs on `localhost`, it requests `/api/dashboard` from `http://localhost:8080`. Restart the dev server after you change `.env.local`.

## Testing

```bash
npm test             # single run
npm run test:watch   # watch mode
```

## Production build

### Standalone

```bash
npm run build
```

Vite writes static assets to `dist/`.

### Embedded in the backend JAR

From the repository root:

```bash
export TILES_URL='https://tile-provider.example/{z}/{x}/{y}{r}.png?api_key=YOUR_API_KEY'
make build-dashboard   # builds the dashboard and copies it to backend/src/main/resources/dashboard-app/
```

### Docker image

The Dockerfile builds the dashboard inside the image, so you pass the tile URL as a build argument. The scripts do this for you:

```bash
export TILES_URL='https://tile-provider.example/{z}/{x}/{y}{r}.png?api_key=YOUR_API_KEY'
make build-docker-image   # local image: whereisivan:latest
make push-image           # arm64 image pushed to ECR
```

To call `docker build` directly, pass the argument yourself:

```bash
docker build \
  --build-arg REACT_APP_TILE_URL="$TILES_URL" \
  -t whereisivan:latest \
  -f infra/docker/Dockerfile .
```

The Dockerfile stops the build if `REACT_APP_TILE_URL` is empty.

Use single quotes around the URL in your shell so that `{z}/{x}/{y}` and any `&` in the query string reach the build unchanged.
