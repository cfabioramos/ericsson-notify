#!/usr/bin/env bash

# install projects
mvn clean install

# gerate images whith images projects 
mvn --projects smartnotification-gateway,smartnotification-rules-engine,smartnotification-scheduler,smartnotification-route,smartnotification-web,smartnotification-enrichment,smartnotification-campaign  \
    dockerfile:build
    
# gerate .tar whith images projects 
docker image ls --format "{{.Repository}}" | grep smartnotification/ | xargs docker image save -o notify-docker-images.tar

#install sshpass
sudo apt-get install sshpass

# copy files from server
sshpass -p "Ericsson@2018" scp notify-docker-images.tar notify-stack.yml ../smart-notification-infra/kafka-stack.yml deploy.sh adm_admin@10.1.2.75:

# access SSH
sshpass -p "Ericsson@2018" ssh -o StrictHostKeyChecking=no adm_admin@10.1.2.75
