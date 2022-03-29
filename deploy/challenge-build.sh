#!/bin/bash

TAG=0.0.1
REGISTRY=192.168.0.241:5000

ROOT=$(cd .. && pwd)
FRONTEND=$ROOT/monitor/frontend
BACKEND=$ROOT/backend
DEPLOY=$ROOT/deploy

# build
cd $FRONTEND && npm install && npm run build:nextdev
cd $BACKEND && ./gradlew clean build

# make images
cd $DEPLOY && docker-compose build
docker tag challenge-monitor:latest $REGISTRY/challenge-monitor:$TAG
docker tag challenge-api:latest $REGISTRY/challenge-api:$TAG
docker tag challenge-data-collector:latest $REGISTRY/challenge-data-collector:$TAG
docker tag challenge-data-provider:latest $REGISTRY/challenge-data-provider:$TAG

# push images
docker push $REGISTRY/challenge-monitor:$TAG
docker push $REGISTRY/challenge-api:$TAG
docker push $REGISTRY/challenge-data-collector:$TAG
docker push $REGISTRY/challenge-data-provider:$TAG

# clean danglings
docker rmi $(docker images -f "dangling=true" -q)

