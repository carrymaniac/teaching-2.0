#!/bin/bash
killall java
nohup java -jar -Dspring.profiles.active=pro -Djava.security.egd=file:/dev/./urandom teaching-0.0.1-SNAPSHOT.jar  > teaching.log 2>&1 &
echo "START"

