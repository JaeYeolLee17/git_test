#!/bin/bash

if [ -z "$TAG" ]; then
  export TAG=0.0.1
fi

if [ -z "$REGISTRY" ]; then
  export REGISTRY=192.168.0.240:5000
fi

echo "TAG=$TAG"
echo "REGISTRY=$REGISTRY"

# push images
docker push $REGISTRY/challenge-api:$TAG
docker push $REGISTRY/challenge-data-collector:$TAG
docker push $REGISTRY/challenge-data-provider:$TAG
docker push $REGISTRY/challenge-monitor:$TAG
