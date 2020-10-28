#!/bin/sh

# Move to the root of the repository.
cd ../..

set PORT='9081'
set JDBC_DATABASE_URL='localhost:5432'
set JDBC_DATABASE_NAME='postgres'
set JDBC_DATABASE_PASSWORD='123456'
set JDBC_DATABASE_USERNAME='postgres'

# Cleanup and package.
mvn liberty:stop
mvn clean package

# Start a local open-liberty deployment server.
mvn liberty:create liberty:install-feature liberty:deploy
# mvn liberty:start
mvn liberty:configure-arquillian

# Verify that the tests were successful.
mvn verify

# Cleanup.
mvn liberty:stop