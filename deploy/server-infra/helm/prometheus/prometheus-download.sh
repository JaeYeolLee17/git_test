#!/bin/bash

FILENAME=kube-prometheus-stack-34.8.0

if [ ! -d "kube-prometheus-stack" ]; then
  wget https://github.com/prometheus-community/helm-charts/releases/download/kube-prometheus-stack-34.8.0/$FILENAME.tgz
  tar xvfz $FILENAME.tgz
  rm -rf $FILENAME.tgz
fi

