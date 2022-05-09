#!/bin/bash
echo "------------------------"
echo $1
echo $2
echo $3

sed -i s/8080/$2/g $1/conf/server.xml
sed -i s/8005/$3/g $1/conf/server.xml

./$1/bin/startup.sh
