#!/bin/bash
echo "------------------------"
echo $1
echo $2
echo $3
echo $4
#Mover el war a la carpeta del tomcat
cp $1.war ./$2/webapps/


sed -i s/<Server port="8005" shutdown="SHUTDOWN">/<Server port="$3" shutdown="SHUTDOWN">/g ./$2/conf/server.xml
sed -i s/<Connector port="8080" protocol="HTTP/1.1"/<Connector port="$4" protocol="HTTP/1.1"/g ./$2/conf/server.xml

chmod +x $2/bin/*
./$2/bin/startup.sh
