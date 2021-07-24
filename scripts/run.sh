#!/bin/bash

echo build maven project
mvn clean -DskipTests install -Drat.skip=true
echo run project
java -jar target/MyBookShopApp-0.0.1.jar &

