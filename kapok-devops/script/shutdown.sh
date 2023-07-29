#!/bin/bash

cd `dirname $0`

JAR_NAME=authorization
if [ $# -gt 0 ]; then
    JAR_NAME="$1"
fi
SERVER_NAME="KAPOK.ERP.$JAR_NAME"

PIDS=`ps -ef | grep java | grep "$SERVER_NAME" |awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit
fi

echo -e "Stopping the $SERVER_NAME \c"
for PID in $PIDS ; do
    kill $PID > /dev/null 2>&1
done

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in $PIDS ; do
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        fi
    done
done

echo "OK!"
