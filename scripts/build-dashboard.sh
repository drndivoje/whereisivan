#!/bin/bash

echo "Building Frontend..."
cd dashboard
rm -rf build 
npm install > /dev/null 2>&1 && npm run build > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi
rm -rf ../backend/src/main/resources/dashboard-app
cp -r build ../backend/src/main/resources/dashboard-app
echo "Build succeeded"
