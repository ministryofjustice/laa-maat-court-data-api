#!/usr/bin/env bash

set -e

aws cloudformation package --template-file aws/application/application.template --s3-bucket ${ARTIFACT_BUCKET} --output-template-file application-packaged.template
echo "{ \"tag\":\"${DOCKER_TAG}\" }" >> 'build_tag.json'

echo ------------------------------
# copy dev parameter file
cp aws/application/parameters/development-${APPLICATION_NAME}-pipeline.json development.json

# copy all other parameter files if IS_ROUTE_TO_LIVE is true
if [[ ${IS_ROUTE_TO_LIVE} == "true" ]]
then
  cp aws/application/parameters/test-${APPLICATION_NAME}-pipeline.json test.json
  cp aws/application/parameters/uat-${APPLICATION_NAME}-pipeline.json uat.json
  cp aws/application/parameters/staging-${APPLICATION_NAME}-pipeline.json staging.json
  cp aws/application/parameters/production-${APPLICATION_NAME}-pipeline.json production.json
fi
echo Pushing the Docker image to... "${IMAGE_URI}"
docker push "${IMAGE_URI}"
