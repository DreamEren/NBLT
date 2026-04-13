#!/bin/bash
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(dirname "$0")}"
MAVEN_OPTS="$(concat_lines "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config")" $MAVEN_OPTS
java -Dspring.config.additional-location=src/main/resources/application.yml $MAVEN_OPTS -classpath $MAVEN_PROJECTBASEDIR/target/classes com.forum.user.UserServiceApplication