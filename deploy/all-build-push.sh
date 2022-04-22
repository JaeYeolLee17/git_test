#!/bin/bash

export TAG=0.0.1

cd deploy
./challenge-build.sh
./challenge-docker-build.sh
./challenge-docker-push.sh

