#!/bin/bash

prompt_mysql_healthcheck() {
  echo "checking local mysql health state..."
  while [ $(docker inspect --format={{.State.Health.Status}} items_db) != "healthy" ]; do
    echo "local mysql can not receive queries at the moment..."
    sleep 5
  done
  echo "local mysql is healthy and can now receive queries!"
  return 0
}

prompt_mysql_healthcheck

if [ $(echo $?) == 0 ]
then
  /bin/bash ./gradlew flywayMigrate generateItemsAppJooqSchemaSource -DDB_HOST=items_db -i
else
  echo "something wrong has happened with local mysql!"
fi
