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
    | jq '.token'
}

cd docker/topologies/local

# Be sure everything is down
docker-compose -f stack-topology.yml down
docker-compose -f gamify-topology.yml down

# Run gamify server
docker-compose -f gamify-topology.yml up --build --force-recreate --no-deps &

waitOnPort 8081

# Run Stack Underflow
GAMIFY_API_TOKEN=$(getApiToken 8081 "stack" "1234thisisapassword") \
GAMIFY_SERVER="http://127.0.0.1:8081" \
sh -c 'docker-compose -f stack-topology.yml up --build --force-recreate --no-deps'

