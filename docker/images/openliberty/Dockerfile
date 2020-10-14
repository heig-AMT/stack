FROM open-liberty:kernel-java11

LABEL maintainer="alexandre@piveteau.email"

ADD mvc-simple.war /opt/apps/
ADD drivers/postgresql-42.2.16.jar /opt/lib/
ADD server.xml /config/

ADD entrypoint.sh entrypoint.sh
ADD script.py script.py

USER root
RUN apt-get update
RUN apt-get --assume-yes install python3

ENTRYPOINT [ "./entrypoint.sh" ]