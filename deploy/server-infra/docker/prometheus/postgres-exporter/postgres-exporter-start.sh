#!/bin/bash

docker run -d \
  --name=postgres-exporter \
  -p 9187:9187 \
  -e DATA_SOURCE_NAME="postgresql://postgres:postgres@192.168.0.5:5432/testdb?sslmode=disable" \
  quay.io/prometheuscommunity/postgres-exporter
