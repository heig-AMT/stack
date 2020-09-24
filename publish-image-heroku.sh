#!/usr/bin/bash
docker tag stack-payara registry.heroku.com/heig-amt-stackunderflow/web
echo $TOKEN_HEROKU | docker login registry.heroku.com --username=_ --password-stdin
docker push registry.heroku.com/heig-amt-stackunderflow/web