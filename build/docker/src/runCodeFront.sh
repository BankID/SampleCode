#!/bin/bash
exec java \
    $JAVA_OPTS \
    -XX:-ParallelRefProcEnabled \
    -XX:MaxGCPauseMillis=500 \
    -XX:+ExitOnOutOfMemoryError \
    \
    -Djava.net.preferIPv4Stack=true \
    -Djava.awt.headless=true \
    \
    -jar \
    /opt/codefront/codefront-server.war \
    "$@"
