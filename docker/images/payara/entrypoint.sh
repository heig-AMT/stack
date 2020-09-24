#!/bin/sh
java -Xmx175m -XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0 -jar payara-micro.jar --deploymentDir /opt/payara/deployments --port $PORT