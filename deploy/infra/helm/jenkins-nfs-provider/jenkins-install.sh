#!/bin/bash

helm repo add jenkins https://charts.jenkins.io
helm repo add nfs-subdir-external-provisioner \
  https://kubernetes-sigs.github.io/nfs-subdir-external-provisioner/
helm repo update

helm install nfs-provisioner \
  nfs-subdir-external-provisioner/nfs-subdir-external-provisioner \
  --set nfs.server=192.168.0.241 \
  --set nfs.path=/nfs-share/jenkins-home \
  --set storageClass.name=nfs-provisioner

kubectl apply -f jenkins-namespace.yml
kubectl apply -f jenkins-sa.yml

helm install jenkins jenkins/jenkins \
  -n jenkins \
  -f jenkins-values.yml
