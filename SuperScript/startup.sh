#!/bin/bash

# 1 Nombre del war(ElAbusonDefinitvo.war)
# 2 Usuario remoto
# 3 Ip de la maquina 1
# 4 Ip de la maquina 2

cp  $1 apache-tomcat-9.0.62/webapps/$1

cp -r apache-tomcat-9.0.62 Proceso0
cp -r apache-tomcat-9.0.62 Proceso1

tar -zcvf Proceso0.tar.gz Proceso0
tar -zcvf Proceso1.tar.gz Proceso1


#./shareKeys.sh $3
# ./shareKeys.sh $4

ip=$(hostname -I)
echo "$ip:8080
$ip:8081
$3:8080
$3:8081" > ip.txt
sed -i s/' '//g ip.txt

#ssh $2@$3 "mkdir Proceso0;mkdir Proceso1;exit;"
ssh $2@$3 "touch Proceso0.tar.gz;touch Proceso1.tar.gz;exit;"

# ssh $2@$4
#mkdir Proceso0
#mkdir Proceso1
# exit


scp ip.txt $2@$3:ip.txt
scp lanzar.sh $2@$3:lanzar.sh
scp Proceso0.tar.gz $2@$3:Proceso0.tar.gz 
scp Proceso1.tar.gz $2@$3:Proceso1.tar.gz 

# scp Proceso0/* $2@$4:Proceso0
# scp Proceso1/* $2@$4:Proceso1

ssh $2@$3 "tar -xvzf Proceso0.tar.gz;tar -xvzf Proceso1.tar.gz;./lanzar.sh Proceso0 8080 8005;./lanzar.sh Proceso1 8081 8006;exit"


# ssh $2@$4
# ./lanzar.sh Proceso0 8080 8005
# ./lanzar.sh Proceso1 8081 8006
# exit

./lanzar.sh Proceso0 8080 8005
./lanzar.sh Proceso1 8081 8006


exit 54
