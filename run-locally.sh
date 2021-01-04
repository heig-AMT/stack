#!/bin/sh

waitOnPort() {
  port=$1

  until $(curl -o /dev/null -s --head --fail localhost:$port)
  do
    sleep 1
  done
}

getApiToken() {
  port=$1
  user=$2
  pass=$3

  curl -s \
    -H "Content-Type: application/json" \
    -X POST \
    -d "{ \"username\": \"$user\", \"password\": \"$pass\"}" \
    localhost:$port/account \
    | jq -r '.token'
}

cd docker/topologies/local

network="stack-net"

gamifyOutsidePort=8081
gamifyInsidePort=1234

user="stack"
pass="1234thisisapassword"

# Be sure everything is down
docker network rm $network
docker-compose -f stack-topology.yml down
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

