#!/bin/bash

export TAG=1.0.0

cd deploy
./challenge-build.sh
./challenge-docker-build.sh
./challenge-docker-push.sh

