#!/bin/bash
killall java
nohup java -jar teaching-0.0.1-SNAPSHOT.jar --spring.profiles.active=pro > teaching.log 2>&1 &
echo "START"

