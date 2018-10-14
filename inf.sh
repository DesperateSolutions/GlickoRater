#!/usr/bin/env bash

docker run --name glicko-postgres --network=glickorater -p 5432:5432 -d postgres
