#!/bin/sh
cd docker/topologies/e2e
docker-compose down
docker-compose up --build --force-recreate --no-deps
