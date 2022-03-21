#!/bin/bash

# build
cd monitor/frontend && npm install && npm run build:nextdev
cd ../../backend && ./gradlew clean build && cd ..

# make images
docker-compose build

# remove danglings
docker rmi $(docker images -f "dangling=true" -q)

# push images
docker push 192.168.0.241:5000/challenge-monitor:0.0.1
docker push 192.168.0.241:5000/challenge-api:0.0.1
docker push 192.168.0.241:5000/challenge-data-collector:0.0.1
docker push 192.168.0.241:5000/challenge-data-provider:0.0.1
