#!/bin/bash

helm uninstall grafana -n grafana

kubectl delete -f grafana-volume.yml
kubectl delete -f grafana-namespace.yml
