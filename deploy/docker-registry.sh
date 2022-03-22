#!/bin/bash

docker run -d --restart unless-stopped --name Docker-Registry -p 5000:5000 registry

