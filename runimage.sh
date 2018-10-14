#!/usr/bin/env bash

docker run --name glicko-rater --network=glickorater -p 3000:3000 -e DB_ADDR="glicko-postgres" -d desp/glickorater:local
