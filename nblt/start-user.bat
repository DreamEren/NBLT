@echo off
set DB_HOST=127.0.0.1
set DB_PORT=2881
set DB_NAME=forum_db
set DB_USERNAME=root
set DB_PASSWORD=@@KINGdream1999
start "user-service" cmd /k "cd /d D:\wm\JAVA\NBLT_BDAP\nblt\nblt\user-service && "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe" -jar target\user-service-1.0.0-SNAPSHOT.jar"