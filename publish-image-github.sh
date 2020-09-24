#!/bin/bash
docker tag stack-payara docker.pkg.github.com/heig-amt/stack/payara
echo $TOKEN_GITHUB | docker login https://docker.pkg.github.com -u heig-AMT-bot --password-stdin
docker push docker.pkg.github.com/heig-amt/stack/payara