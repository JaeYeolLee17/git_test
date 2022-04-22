#!/bin/bash

if [ -z "$TAG" ]; then
  export TAG=0.0.1
fi

if [ -z "$REGISTRY" ]; then
  export REGISTRY=192.168.0.240:5000
fi

echo "TAG=$TAG"
echo "REGISTRY=$REGISTRY"

# build docker image 
docker build -t $REGISTRY/challenge-api:$TAG ../backend/challenge-api
docker build -t $REGISTRY/challenge-data-collector:$TAG ../backend/challenge-data-collector
docker build -t $REGISTRY/challenge-data-provider:$TAG ../backend/challenge-data-provider
docker build -t $REGISTRY/challenge-monitor:$TAG ../monitor/frontend

# clean dangling images
docker rmi $(docker images -f "dangling=true" -q)

