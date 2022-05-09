#!/bin/bash

#Descargamos tomcat
curl https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.62/bin/apache-tomcat-9.0.62.zip > tom.zip
unzip tom.zip
chmod +x apache-tomcat-9.0.62/bin/*.sh

cp  $1 apache-tomcat-9.0.62/webapps/$1

cp -r apache-tomcat-9.0.62 Proceso0
cp -r apache-tomcat-9.0.62 Proceso1


./super.sh Proceso0 8080 8005
./super.sh Proceso1 8081 8006


exit 54
