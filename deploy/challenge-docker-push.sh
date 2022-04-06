#!/bin/bash

if [ -z "$TAG" ]; then
  export TAG=latest
fi
echo "TAG=$TAG"

export REGISTRY=192.168.0.241:5000

# push images
docker push $REGISTRY/challenge-api:$TAG
docker push $REGISTRY/challenge-data-collector:$TAG
docker push $REGISTRY/challenge-data-provider:$TAG
docker push $REGISTRY/challenge-monitor:$TAG
