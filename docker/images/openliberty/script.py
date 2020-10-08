import subprocess, os, sys
from urllib.parse import urlparse

# Retrieve the current environment.
my_env = os.environ.copy()
my_url = urlparse(my_env["DATABASE_URL"])

# Prepare different variables in a JDBC + PostgreSQL-friendly format.
database_url = my_url.hostname + ":" + str(my_url.port)
database_name = my_url.path.replace("/", "", 1)
database_username = my_url.username
database_password = my_url.password

# Prepare the environment variables.
my_env["JDBC_DATABASE_NAME"] = database_name
my_env["JDBC_DATABASE_URL"] = database_url
my_env["JDBC_DATABASE_USERNAME"] = database_username
my_env["JDBC_DATABASE_PASSWORD"] = database_password

print("[LAUNCH] script.py")

# Open a subprocess with the new environment, pipe standard output and await termination.
# https://github.com/OpenLiberty/ci.docker/blob/master/releases/20.0.0.10/kernel/Dockerfile.ubi.adoptopenjdk11#L92-L93
subprocess.Popen(["/opt/ol/helpers/runtime/docker-server.sh", "/opt/ol/wlp/bin/server", "run", "defaultServer"],
            env     = my_env,
            stdout  = sys.stdout,
            stderr  = sys.stderr).communicate()