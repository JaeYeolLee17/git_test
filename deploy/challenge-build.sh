#!/bin/bash

# build backend
cd ../backend
./gradlew clean build

#build frontend
cd ../../monitor/frontend
npm install && npm run build:nextdev

