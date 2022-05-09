#!/bin/bash

export TAG=0.0.1
export REGISTRY=registry.emgglobal.co.kr:9443

docker build -t $REGISTRY/challenge-api:$TAG ../backend/challenge-api
docker build -t $REGISTRY/challenge-data-collector:$TAG ../backend/challenge-data-collector
docker build -t $REGISTRY/challenge-data-provider:$TAG ../backend/challenge-data-provider
docker build -t $REGISTRY/challenge-monitor:$TAG ../monitor/frontend

docker push $REGISTRY/challenge-api:$TAG
docker push $REGISTRY/challenge-data-collector:$TAG
docker push $REGISTRY/challenge-data-provider:$TAG
docker push $REGISTRY/challenge-monitor:$TAG

docker rmi $REGISTRY/challenge-api:$TAG
docker rmi $REGISTRY/challenge-data-collector:$TAG
docker rmi $REGISTRY/challenge-data-provider:$TAG
docker rmi $REGISTRY/challenge-monitor:$TAG
