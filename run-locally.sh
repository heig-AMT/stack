#!/bin/bash

. ./utils.sh

network="stack-net"

gamifyOutsidePort=8081
gamifyInsidePort=1234

user="stack"
pass="1234thisisapassword"

stackTopology="stack-topology.yml"

cd docker/topologies/local

if [ $1 = "e2e" ]
then
  stackTopology="e2e-stack-topology.yml"
fi

# Be sure everything is down
docker network rm $network
GAMIFY_API_TOKEN="empty" GAMIFY_SERVER="empty" docker-compose -f stack-topology.yml down
docker-compose -f gamify-topology.yml down

# Create external network
docker network create $network

# Run gamify server
docker-compose -f gamify-topology.yml up --build --force-recreate --no-deps &

waitOnPort $gamifyOutsidePort

# Run Stack Underflow
GAMIFY_API_TOKEN=$(getApiToken $gamifyOutsidePort $user $pass) \
GAMIFY_SERVER="http://gamify:${gamifyInsidePort}" \
docker-compose -f $stackTopology up --build --force-recreate --no-deps
