#!/bin/bash

echo "Building API..."

# Navigate to the backend folder
cd backend

# Run the Gradle build command and suppress output
./gradlew clean buildFatJar > /dev/null 2>&1

# Check if the build was successful
if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi

echo "Build succeeded"