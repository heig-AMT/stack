#!/bin/bash

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
    localhost:$port/accounts \
    | jq -r '.token'
}
