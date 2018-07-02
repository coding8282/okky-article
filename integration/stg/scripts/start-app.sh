#!/usr/bin/env bash

sudo chmod +x /home/ec2-user/okky-article-1.0.0.jar
sudo ln -sf /home/ec2-user/okky-article-1.0.0.jar /etc/init.d/okky-article
sudo service okky-article start
sleep 10s