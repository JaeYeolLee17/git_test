#!/bin/bash

helm uninstall jenkins -n jenkins

kubectl delete -f jenkins-volume.yml
kubectl delete -f jenkins-sa.yml
kubectl delete -f jenkins-namespace.yml
