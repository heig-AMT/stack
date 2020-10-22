#!/bin/sh
cd docker/topologies/test
docker-compose down
docker-compose up --build --force-recreate --no-deps
