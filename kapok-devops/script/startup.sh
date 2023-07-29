#!/bin/bash

JAVA_BIN=java

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`/webapps
LOGS_DIR=`pwd`/logs

SPRING_PROFILES_ACTIVE="prod"
JAR_NAME=authorization
if [ $# -gt 0 ]; then
    JAR_NAME="$1"
fi
if [ $JAR_NAME == "config-server" ]; then
    SPRING_PROFILES_ACTIVE="native"
fi
SERVER_NAME="KAPOK.ERP.$JAR_NAME"

PIDS=`ps -ef | grep java | grep "$SERVER_NAME" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    exit
fi

SERVER_BIN="$DEPLOY_DIR/$JAR_NAME.jar"
if [ ! -f "$SERVER_BIN" ]; then
    echo "ERROR: The $SERVER_BIN does not exist!"
    exit
fi

if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
SERVER_LOG="$LOGS_DIR/${JAR_NAME}_stdout.log"

JAVA_MEM_OPTS_DEFAULT=""
BITS=`$JAVA_BIN -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS_DEFAULT=" -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS_DEFAULT=" -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

JAVA_MEM_OPTS=" -server -Xms512m -Xmx2048m $JAVA_MEM_OPTS_DEFAULT"

echo -e "Starting the $SERVER_NAME .\c"
nohup $JAVA_BIN $JAVA_MEM_OPTS -DServerName=$SERVER_NAME -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar $SERVER_BIN --spring.config.name=$JAR_NAME > $SERVER_LOG 2>&1 &

echo "OK!"
