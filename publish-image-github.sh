#!/bin/bash

# Tag the Docker images.
docker tag stack-payara docker.pkg.github.com/heig-amt/stack/payara
docker tag stack-openliberty docker.pkg.github.com/heig-amt/stack/openliberty

# Log in to GitHub package registry.
echo $TOKEN_GITHUB | docker login https://docker.pkg.github.com -u heig-AMT-bot --password-stdin

# Push the Docker images.
docker push docker.pkg.github.com/heig-amt/stack/payara
docker push docker.pkg.github.com/heig-amt/stack/openliberty