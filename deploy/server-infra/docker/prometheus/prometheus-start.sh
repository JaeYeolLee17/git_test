#!/bin/bash

docker build -t prometheus-custom .
docker run --name prometheus -d -p 9090:9090 prometheus-custom

#docker run --name prometheus -d -p 9090:9090 prom/prometheus
