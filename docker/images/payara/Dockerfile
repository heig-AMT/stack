FROM payara/micro:5.2020.4-jdk11

LABEL maintainer="alexandre@piveteau.email"

ADD mvc-simple.war $DEPLOY_DIR

ENTRYPOINT  java                                                    \
            -XX:+UseContainerSupport                                \
            -XX:MaxRAMPercentage=90.0                               \
            -jar payara-micro.jar                                   \
            --deploymentDir /opt/payara/deployments                 \
            --port $PORT