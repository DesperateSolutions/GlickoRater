#!/usr/bin/env bash

docker run --name glicko-rater --network=glickorater -p 3000:3000 -e MONGODB_ADDR="glicko-mongo" -d desp/glickorater:local
