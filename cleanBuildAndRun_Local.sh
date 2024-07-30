#!/bin/bash
# Exit on failure (-e) and echo statements before running them (-x)
set -ex

# Get the directory of this script
script_dir=$(dirname "$0")

# Change to the maat-court-data-api directory in order to perform a gradle build
cd "$script_dir/maat-court-data-api" || { echo "Unable to navigate to the root directory"; exit; }
./gradlew clean build

# Change back up to the laa-maat-court-data-api root directory in order to build and run the docker container
cd "$script_dir" || { echo "Unable to navigate to the root directory"; exit; }
docker-compose build
docker-compose up