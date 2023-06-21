#!/usr/bin/env bash
dockerTempTag=codefront-server
echo  ${dockerTempTag}

mkdir -p docker/target/bin/
cp ../server/target/codefront-develop-SNAPSHOT.war docker/target/bin/codefront-server.war

docker buildx build docker/. \
    --no-cache \
    -t ${dockerTempTag} \
    --progress=plain
