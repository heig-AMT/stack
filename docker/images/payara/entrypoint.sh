#!/bin/sh
java                                                    \
    -Xmx175m                                            \
    -XX:+UseContainerSupport                            \
    -XX:MaxRAMPercentage=90.0                           \
    -jar payara-micro.jar                               \
    --deploy /opt/payara/deployments/mvc-simple.war:    \
    --port $PORT