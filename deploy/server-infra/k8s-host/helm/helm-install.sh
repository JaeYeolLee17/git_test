#!/bin/bash

tar -zxvf helm-v3.8.1-linux-amd64.tar.gz
sudo mv linux-amd64/helm /usr/local/bin/helm
rm -rf linux-amd64/
