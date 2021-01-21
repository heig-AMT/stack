#!/bin/bash

# Tag the Docker images.
docker tag stack-payara ghcr.io/heig-amt/stack-payara
docker tag stack-openliberty ghcr.io/heig-amt/stack-openliberty

# Log in to GitHub container registry.
echo $TOKEN_GITHUB | docker login ghcr.io -u heig-AMT-bot --password-stdin

# Push the Docker images.
docker push ghcr.io/heig-amt/stack-payara
docker push ghcr.io/heig-amt/stack-openliberty
