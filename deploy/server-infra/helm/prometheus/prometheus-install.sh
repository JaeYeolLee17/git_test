#!/bin/bash

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

kubectl apply -f prometheus-namespace.yml
kubectl apply -f prometheus-server-volume.yml
kubectl apply -f prometheus-alertmanager-volume.yml

helm install prometheus prometheus-community/prometheus \
  -n prometheus \
  -f prometheus-values.yml
