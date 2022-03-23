#!/bin/bash

ROOT=$(cd .. && pwd)
FRONTEND=$ROOT/monitor/frontend
BACKEND=$ROOT/backend
DEPLOY=$ROOT/deploy
REGISTRY=192.168.0.241:5000

# build
cd $FRONTEND && npm install && npm run build:nextdev
cd $BACKEND && ./gradlew clean build

# make image
cd $DEPLOY && docker-compose build
docker rmi $(docker images -f "dangling=true" -q)

# push image
docker push $REGISTRY/challenge-monitor:0.0.1
docker push $REGISTRY/challenge-api:0.0.1
docker push $REGISTRY/challenge-data-collector:0.0.1
docker push $REGISTRY/challenge-data-provider:0.0.1
