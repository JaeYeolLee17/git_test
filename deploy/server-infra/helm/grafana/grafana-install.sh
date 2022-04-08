#!/bin/bash

helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

kubectl apply -f grafana-namespace.yml
kubectl apply -f grafana-volume.yml

helm install grafana grafana/grafana \
  -n grafana \
  -f grafana-values.yml
