#!/usr/bin/env bash

set -e

echo Building the ear file and Docker image with gradle...
pushd maat-court-data-api
chmod +x ./gradlew
./gradlew build
./gradlew sonar \
  -Dsonar.projectKey=ministryofjustice_laa-maat-court-data-api \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.organization=ministryofjustice

docker build -t maat-cda .
docker tag maat-cda "${IMAGE_URI}"
popd