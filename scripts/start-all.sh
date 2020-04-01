#!/bin/bash

# --Functions--
start_mysql() {
  UP=$(pgrep mysql | wc -l)

  if [ "$UP" -eq 0 ]; then
    /usr/local/bin/mysql.server start

  else
    echo "MySQL is already running!"
  fi
}

migrations() {
  ./gradlew flywayMigrate -i
}

start_app() {
  ./gradlew clean generateItemAppJooqSchemaSource build run
}


# --Main Script--
echo "MySQL script is running..."
start_mysql
echo "Gradle is running db migrations..."
migrations
echo "Gradle is running the app..."
start_app
