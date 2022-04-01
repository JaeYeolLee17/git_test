#!/bin/bash

if [ -z "$TAG" ]; then
  export TAG=latest
fi

if [ -z "$REPOSITRY" ]; then
  export REGISTRY=192.168.0.241:5000
fi

echo "TAG=$TAG"
echo "REGISTRY=$REGISTRY"

# docker image build 
docker build -t $REGISTRY/challenge-api:$TAG ../backend/challenge-api
docker build -t $REGISTRY/challenge-data-collector:$TAG ../backend/challenge-data-collector
docker build -t $REGISTRY/challenge-data-provider:$TAG ../backend/challenge-data-provider
docker build -t $REGISTRY/challenge-monitor:$TAG ../monitor/frontend

# clean dangling images
docker rmi $(docker images -f "dangling=true" -q)

