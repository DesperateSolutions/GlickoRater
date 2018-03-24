#!/bin/bash

./gradlew shadowJar
cp build/libs/glicko-rater-1.0.jar docker/

docker build --tag glickorater docker/