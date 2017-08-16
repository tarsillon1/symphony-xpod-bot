#!/bin/bash
mvn clean install
export CONFIG_DIR="./config"
export TARGET="./target/symphony-xpod-bot-1.0-SNAPSHOT-jar-with-dependencies.jar"
nohup java -Dbot.config.dir=$CONFIG_DIR -jar $TARGET &
