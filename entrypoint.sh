#!/bin/bash

LOGTYPE=${APP_NAME}
LOGOWNER=appadmin
ORIGINLOGDIR=${APP_HOME}/logs

linkLogDir(){
    HOSTNAME=`hostname`

    LOGDIR="/data/log/${LOGTYPE}/$HOSTNAME"
    mkdir -p $LOGDIR
    chown -R $LOGOWNER $LOGDIR
    rm -rf $ORIGINLOGDIR
    ln -s $LOGDIR $ORIGINLOGDIR
}

linkLogDir

su appadmin -c "/opt/jdk-17/bin/java -Dlogging.file.path=${ORIGINLOGDIR} ${JVM_SIZE} -jar ${APP_FILE} --spring.config.additional-location=classpath:/application.yml,file://${CONFIG_URI}"
