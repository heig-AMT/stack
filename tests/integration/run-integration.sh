#!/bin/sh

# Move to the root of the repository
cd ../..

PWD=$(pwd)

export PORT=9081
export JDBC_DATABASE_URL=localhost:5432
export JDBC_DATABASE_NAME=root
export JDBC_DATABASE_PASSWORD=123456
export JDBC_DATABASE_USERNAME=root
export WLP_HOME=$PWD/target/liberty/wlp/

# Cleanup and package.
mvn liberty:stop
mvn clean package

# Start a local open-liberty deployment server.
mvn liberty:create liberty:install-feature liberty:deploy

# Use the OL runtime during verification. This allows us to use ... environment variables :D
mvn verify -Druntime=ol

# Cleanup.
mvn liberty:stop