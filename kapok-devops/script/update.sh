#!/bin/bash

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`/webapps
DIST_DIR=$DEPLOY_DIR/dist
DIST_NAME=$DEPLOY_DIR/dist.tar

rm -rf $DIST_DIR
tar xvf $DIST_NAME -C $DEPLOY_DIR
#rm -f $DIST_NAME

UP_TIMESTAMP=`date +"%Y-%m-%d %H:%M:%S"`
sed -i "s/\(<\/html>\)/<!--$UP_TIMESTAMP-->\1/" $DIST_DIR/index.html
