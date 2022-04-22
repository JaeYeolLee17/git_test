#!/bin/bash

helm uninstall prometheus -n prometheus

kubectl delete -f prometheus-server-volume.yml
kubectl delete -f prometheus-alertmanager-volume.yml
kubectl delete -f prometheus-namespace.yml
