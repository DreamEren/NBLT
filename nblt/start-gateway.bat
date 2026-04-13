@echo off
set DB_HOST=127.0.0.1
set DB_PORT=2881
set DB_NAME=forum_db
set DB_USERNAME=root
set DB_PASSWORD=
"C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe" -jar "D:\wm\JAVA\NBLT_BDAP\nblt\nblt\gateway-service\target\gateway-service-1.0.0-SNAPSHOT.jar"