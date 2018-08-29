#!/usr/bin/env bash

# Fazer undeploy e redeploy da stack (executar no server)
sudo docker image load < notify-docker-images.tar
sudo docker stack rm notify
sudo docker stack rm kafka
sudo docker stack deploy -c kafka-stack.yml kafka
sudo docker stack deploy -c notify-stack.yml notify