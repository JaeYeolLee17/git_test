#!/bin/bash

cd ../monitor/frontend && rm -rf build && npm install && npm run build:nextdev
cd ../../backend && ./gradlew clean build
