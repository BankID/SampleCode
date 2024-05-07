ARG BASE_IMAGE=${BASE_IMAGE:-registry.access.redhat.com/ubi8/openjdk-17-runtime:1.19-4}
FROM ${BASE_IMAGE}

LABEL base-image=${BASE_IMAGE}
LABEL name="codefront-server"
LABEL description="CodeFront-server is the java-based server, currently the main GUI server"
LABEL distribution-scope="opensource"
LABEL summary="codefront-server"

# Expose ports and volumes.
EXPOSE 8443
VOLUME ["/opt/codefront/config/", "/opt/codefront/logs"]

# Copy files.
COPY src/runCodeFront.sh target/bin/codefront-server.war /opt/codefront/
COPY src/logback-spring.xml /opt/codefront/default-config/

USER root

# Run required commands.
RUN chmod 755 /opt && \
    chmod 755 /opt/codefront/runCodeFront.sh && \
    chmod 644 /opt/codefront/codefront-server.war

# Set working directory and user.
WORKDIR /opt/codefront/
USER 2000

# Set entry point.
ENTRYPOINT ["/opt/codefront/runCodeFront.sh"]
