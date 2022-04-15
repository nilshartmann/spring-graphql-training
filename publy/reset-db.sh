#! /bin/bash

docker-compose -f docker-compose.yml down

rm -rf ./docker/db-data

docker-compose -f docker-compose.yml up -d