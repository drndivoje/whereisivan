# Stage 1: Build React dashboard
FROM node:25.4.0-alpine AS dashboard-builder
WORKDIR /dashboard
COPY dashboard/package*.json ./
RUN npm ci
COPY dashboard/ ./
# Optional map tile URL, inlined into the bundle by Vite at build time
ARG REACT_APP_TILE_URL
RUN test -n "$REACT_APP_TILE_URL" || (echo "Error: REACT_APP_TILE_URL build argument is missing" >&2 && exit 1)
RUN npm run build

# Stage 2: Build backend fat JAR
FROM eclipse-temurin:25-jdk AS backend-builder
WORKDIR /backend
COPY backend/gradlew ./
COPY backend/gradle/ ./gradle/
COPY backend/settings.gradle.kts backend/build.gradle.kts backend/gradle.properties ./
#RUN ./gradlew dependencies --no-daemon > /dev/null 2>&1 || true
COPY backend/src/ ./src/
COPY --from=dashboard-builder /dashboard/dist ./src/main/resources/dashboard-app
RUN ./gradlew clean buildFatJar --no-build-cache --no-configuration-cache --no-daemon --parallel 

# Stage 3: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN mkdir -p /app/gpx
ADD infra/docker/application.conf /app/application.conf
COPY --from=backend-builder /backend/build/libs/rocks.drnd.whereisivan-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "-config=/app/application.conf"]
