#!/bin/bash

cd /home/ec2-user

sudo mkdir -p /usr/local/lib/docker/cli-plugins/
sudo curl -SL https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

sudo git clone -b deploy-test https://github.com/leechanwoo/technopark-git-practice.git
sudo docker compose -f $(pwd)/technopark-git-practice/docker-compose-deploy.yaml up -d