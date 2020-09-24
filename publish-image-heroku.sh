#!/usr/bin/bash
docker tag stack-payara registry.heroku.com/heig-amt-stackunderflow/web
echo $TOKEN_HEROKU | docker login --username=_ --password-stdin registry.heroku.com
docker push registry.heroku.com/heig-amt-stackunderflow/web