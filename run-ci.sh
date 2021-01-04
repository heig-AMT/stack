#!/bin/bash

. ./utils.sh

network="stack-net"

gamifyOutsidePort=8081
gamifyInsidePort=1234

user="stack"
pass="1234thisisapassword"

cd docker/topologies/e2e

# Retrieve gamify
echo $TOKEN_GITHUB | docker login https://docker.pkg.github.com --username heig-AMT-bot --password-stdin

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
sh -c 'docker-compose -f stack-topology.yml up --build --force-recreate --no-deps'
