#!/bin/bash

cp  $1 apache-tomcat-9.0.62/webapps/$1

cp -r apache-tomcat-9.0.62 Proceso0
cp -r apache-tomcat-9.0.62 Proceso1


./super.sh Proceso0 8080 8005
./super.sh Proceso1 8081 8006


exit 54
