#!/usr/bin/env bash
set -e

./gradlew shadowJar

docker build --tag desp/glickorater:local .
