#!/bin/bash

export TAG=0.0.1
export REGISTRY=192.168.0.240:5000

./challenge-build.sh
./challenge-docker-build.sh
./challenge-docker-push.sh

