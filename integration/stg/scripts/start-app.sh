#!/usr/bin/env bash

nohup java -jar \
/home/ec2-user/okky-article-1.0.0.jar \
--spring.cloud.config.label=develop \
> /dev/null 2> /dev/null < /dev/null &

sleep 20s