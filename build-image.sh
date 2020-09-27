#!/bin/bash

# Maven
echo "Building StackUnderflow"
mvn clean package

# Payara
echo "Building stack-payara Docker image"
cp target/mvc-simple.war docker/images/payara
docker build -t stack-payara ./docker/images/payara

# Open Liberty
echo "Building open-liberty Docker image"
cp target/mvc-simple.war docker/images/openliberty
cp src/main/liberty/config/server.xml docker/images/openliberty
docker build -t stack-openliberty ./docker/images/openliberty