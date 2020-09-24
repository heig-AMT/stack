#!/usr/bin/bash
mvn clean package
cp target/mvc-simple.war docker/images/payara
docker build -t docker.pkg.github.com/heig-amt/stack/payara ./docker/images/payara