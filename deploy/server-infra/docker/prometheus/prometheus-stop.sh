#!/bin/bash

docker rm -f $(docker ps -a -q -f name=prometheus)
