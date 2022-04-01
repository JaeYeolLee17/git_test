#!/bin/bash

helm uninstall jenkins jenkins/jenkins -n jenkins
helm uninstall nfs-provisioner

kubectl delete -f jenkins-sa.yml
kubectl delete -f jenkins-namespace.yml
