#!/bin/bash

FILENAME=node_exporter-1.3.1.linux-amd64

if [ ! -d "$FILENAME" ]; then
  wget https://github.com/prometheus/node_exporter/releases/download/v1.3.1/$FILENAME.tar.gz
  tar xvfz $FILENAME.tar.gz
  rm -rf $FILENAME.tar.gz
fi

cd $FILENAME
nohup ./node_exporter 1>/dev/null 2>&1 &

