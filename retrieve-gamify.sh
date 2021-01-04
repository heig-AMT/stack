#!/bin/bash

echo $TOKEN_GITHUB | docker login https://docker.pkg.github.com --username heig-AMT-bot --password-stdin

docker image pull docker.pkg.github.com/heig-amt/gamify/openjdk:latest
