#!/bin/bash

helm repo add jenkins https://charts.jenkins.io
helm repo update

kubectl apply -f jenkins-namespace.yml
kubectl apply -f jenkins-volume.yml
kubectl apply -f jenkins-sa.yml

helm install jenkins jenkins/jenkins \
  -n jenkins \
  -f jenkins-values.yml
