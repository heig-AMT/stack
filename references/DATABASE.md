# Database

## `docker-compose` setup

When running the application with `docker-compose` and the `./run-compose.sh` script, a PostgreSQL instance is automatically provisioned and made available to the application instance.

## Local setup

When running Open Liberty with the `mvn liberty:dev` goal, you should provide the following environment variables to indicate which PostgreSQL instance to use :

+ `JDBC_DATABASE_URL` - the URL of the database (such as `localhost:5432`);
+ `JDBC_DATABASE_USERNAME` - the username to use in the database (such as `admin`);
+ `JDBC_DATABASE_PASSWORD` - the password to use in the database (such as `password`); and
+ `JDBC_DATABASE_NAME` - the name of the database (such as `postgres`).

The JDBC driver should be automatically loaded from the `liberty/drivers` folder.