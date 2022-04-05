#!/bin/bash

helm repo add metallb https://metallb.github.io/metallb

helm install metallb metallb/metallb \
  -n metallb --create-namespace \
  -f metallb-values.yml 
