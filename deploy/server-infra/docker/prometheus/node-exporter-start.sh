#!/bin/bash

if [ ! -d "node_exporter-1.3.1.linux-amd64" ]; then
  wget https://github.com/prometheus/node_exporter/releases/download/v1.3.1/node_exporter-1.3.1.linux-amd64.tar.gz
  tar xvfz node_exporter-*.*-amd64.tar.gz
  rm -rf node_exporter-*.*-amd64.tar.gz
fi

cd node_exporter-*.*-amd64
./node_exporter
