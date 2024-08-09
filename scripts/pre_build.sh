#!/usr/bin/env bash

set -e

echo Checking Git version...
git --version

echo Logging in to Amazon ECR...
aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com
DOCKER_TAG="$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | head -c 8)"
echo Created docker tag $DOCKER_TAG
IMAGE_URI="${REPOSITORY_URI}:${DOCKER_TAG}"
echo "Pulling in SQS integration test Docker image dependencies"
docker pull localstack/localstack
echo "Finished pulling localstack/localstack test dependency"