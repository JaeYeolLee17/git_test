#!/bin/bash

cd ../backend
./gradlew clean build

cd ../monitor/frontend
npm install && npm run build:nextdev

