FROM 081731760779.dkr.ecr.us-east-1.amazonaws.com/xcloudiq/openjdk:17.0.2

LABEL maintainer="dprasad@extremenetworks.com"

ENV APP_NAME=xcloud-cis
ENV APP_EXT=jar
ENV APP_HOME=/opt/${APP_NAME}
ENV APP_FILE=${APP_HOME}/${APP_NAME}.${APP_EXT}
ENV CONFIG_URI=/aerohive_app/etc/application.yml

COPY --chown=appadmin entrypoint.sh /usr/local/bin/entrypoint.sh

RUN mkdir $APP_HOME && \
    chmod 755 /usr/local/bin/entrypoint.sh && \
    chown appadmin:appadmin -R $APP_HOME

COPY --chown=appadmin cis-service/build/cis-service*.jar ${APP_FILE}
COPY --chown=appadmin cis-service/src/main/resources/application.yml ${CONFIG_URI}

EXPOSE 5099 9999

CMD ["/usr/local/bin/entrypoint.sh"]
