#!/bin/bash
# user-service 统一启动脚本
# 固定使用 OceanBase test 租户 root@test

export DB_HOST=127.0.0.1
export DB_PORT=2881
export DB_NAME=forum_db
export DB_USERNAME='root@test'
export DB_PASSWORD=''

cd "$(dirname "$0")"

JAR_FILE="target/user-service-1.0.0-SNAPSHOT.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "Error: $JAR_FILE not found"
    exit 1
fi

echo "Starting user-service with:"
echo "  DB_HOST=$DB_HOST"
echo "  DB_PORT=$DB_PORT"
echo "  DB_NAME=$DB_NAME"
echo "  DB_USERNAME=$DB_USERNAME"
echo "  DB_PASSWORD=$DB_PASSWORD"

nohup java -jar "$JAR_FILE" > /tmp/user-service.log 2>&1 &
echo $!
