#!/bin/bash

if [ $# -ne 1 ]; then
  echo "----------------------------------"
  echo "Usage: $0 VERSION"
  echo "----------------------------------"
  echo "Ex:"
  echo "$ $0 1.0.0"
  echo "----------------------------------"
  exit -1
fi

export TAG=$1

cd deploy
./challenge-build.sh
./challenge-docker-build.sh
./challenge-docker-push.sh

