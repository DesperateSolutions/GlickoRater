#!/usr/bin/env bash

docker run --name glicko-rater --network=glickorater -p 3000:3000 -e MONGODB_PORT_27017_TCP_ADDR="glicko-mongo" -d desp/glickorater:local
