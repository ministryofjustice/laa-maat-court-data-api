#!/usr/bin/env bash

set -e

echo Building the ear file and Docker image with gradle...
pushd maat-court-data-api
chmod +x ./gradlew && ./gradlew build
docker build -t maat-cda .
docker tag maat-cda "${IMAGE_URI}"
popd
