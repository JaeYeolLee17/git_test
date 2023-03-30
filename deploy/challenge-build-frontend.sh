#!/bin/bash

cd ../frontend && rm -rf build && npm install --save --legacy-peer-deps && npm run build
