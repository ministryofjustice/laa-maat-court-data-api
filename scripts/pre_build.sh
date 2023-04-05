#!/usr/bin/env bash

set -e

echo Logging in to Amazon ECR...
$(aws ecr get-login-password --region ${AWS_DEFAULT_REGION})
DOCKER_TAG="$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 8)"
echo Created docker tag $DOCKER_TAG
IMAGE_URI="${REPOSITORY_URI}:${DOCKER_TAG}"
