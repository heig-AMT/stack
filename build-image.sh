#!/bin/bash
mvn clean package
cp target/mvc-simple.war docker/images/payara
docker build -t stack-payara ./docker/images/payara