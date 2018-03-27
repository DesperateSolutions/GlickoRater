#!/usr/bin/env bash

docker run --name glicko-mongo --network=glickorater -p 27017:27017 -d mongo
