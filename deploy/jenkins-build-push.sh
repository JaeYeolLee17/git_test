#!/bin/bash

export TAG=0.0.1
export REGISTRY=192.168.0.241:5000

cd deploy
./challenge-build.sh
./docker-image-build.sh
./docker-image-push.sh

