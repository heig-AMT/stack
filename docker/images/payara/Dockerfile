FROM payara/micro:5.2020.4-jdk11

LABEL maintainer="alexandre@piveteau.email"

ADD mvc-simple.war $DEPLOY_DIR
ADD entrypoint.sh entrypoint.sh

ENTRYPOINT [ "./entrypoint.sh" ]