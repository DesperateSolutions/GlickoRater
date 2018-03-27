#!/usr/bin/env bash
set -e

./gradlew clean shadowJar

docker login --username "${DOCKER_USERNAME}" --password "${DOCKER_PASSWORD}"
docker build --tag desp/glickorater:${TRAVIS_COMMIT} .
docker push desp/glickorater:${TRAVIS_COMMIT}
