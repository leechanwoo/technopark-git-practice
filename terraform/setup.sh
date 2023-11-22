#!/bin/bash 

# move to working directory 
cd /home/ec2-user

# installation of docker compose plugin 
sudo mkdir -p /usr/local/lib/docker/cli-plugins/
sudo curl -SL https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# git clone
sudo git clone -b deploy https://github.com/leechanwoo/technopark-git-practice.git

# service up 
sudo docker compose up -d