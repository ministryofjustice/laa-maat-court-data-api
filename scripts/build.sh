#!/usr/bin/env bash

set -e

echo Building the ear file and Docker image with gradle...
pushd maat-court-data-api
chmod +x ./gradlew
./gradlew build
./gradlew sonarqube \
  -Dsonar.scm.provider=git
  -Dsonar.projectKey=maat-cd-api \
  -Dsonar.host.url=http://sonarqube.aws.ssvs.legalservices.gov.uk \
  -Dsonar.login=${SONARQUBE_TOKEN}
docker build -t maat-cda .
docker tag maat-cda "${IMAGE_URI}"
popd