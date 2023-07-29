#!/bin/bash

JAR_BIN=jar

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`/webapps
CONF_DIR=`pwd`/config
CONF_BAK_DIR=`pwd`/config/backup
UP_TIMESTAMP=`date "+%Y%m%d%H%M%S"`

SPRING_PROFILES_ACTIVE="prod"
JAR_NAME=authorization
if [ $# -gt 0 ]; then
    JAR_NAME="$1"
fi
TEMP_DIR="$DEPLOY_DIR/$JAR_NAME"

if [ ! -d $CONF_DIR ]; then
    mkdir $CONF_DIR
fi
if [ ! -d $CONF_BAK_DIR ]; then
    mkdir $CONF_BAK_DIR
fi
if [ ! -d $TEMP_DIR ]; then
    mkdir $TEMP_DIR
fi
cd $TEMP_DIR

$JAR_BIN xvf "$DEPLOY_DIR/$JAR_NAME.jar" BOOT-INF/classes/application-${SPRING_PROFILES_ACTIVE}.yml
if [ -f "$CONF_DIR/$JAR_NAME-${SPRING_PROFILES_ACTIVE}.yml" ]; then
    cp "$CONF_DIR/$JAR_NAME-${SPRING_PROFILES_ACTIVE}.yml" "$CONF_BAK_DIR/$JAR_NAME-${SPRING_PROFILES_ACTIVE}.yml.$UP_TIMESTAMP"
fi
mv "$TEMP_DIR/BOOT-INF/classes/application-${SPRING_PROFILES_ACTIVE}.yml" "$CONF_DIR/$JAR_NAME-${SPRING_PROFILES_ACTIVE}.yml"
rm -rf $TEMP_DIR

echo "OK!"
