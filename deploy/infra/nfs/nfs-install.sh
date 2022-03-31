#!/bin/bash

DIR="/nfs-share/jenkins"
if [ ! -d "$DIR" ]; then
  sudo mkdir $DIR 
  sudo chmod 777 $DIR
fi

if [ ! -f "/etc/exports" ]; then
  sudo apt install -y nfs-kernel-server nfs-common
fi

sudo bash -c 'echo "/nfs-share/jenkins 192.168.0.0/24(rw,sync,no_subtree_check)" >> /etc/exports'
sudo systemctl restart nfs-server.service
