#!/bin/bash

if [ $# -ne 1 ]; then
  echo Necesitamos como parametro el nombre del .war
  exit 1
fi

#Descargamos jersey
curl https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.35/jaxrs-ri-2.35.zip > jerry.zip 
unzip jerry.zip
#Descargamos tomcat
curl https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.59/bin/apache-tomcat-9.0.59.zip > tom.zip
unzip tom.zip
#Descargamos los scripts de Rodrigo
curl http://vis.usal.es/rodrigo/documentos/sisdis/scripts/lanzar.sh > lanzar.sh
curl http://vis.usal.es/rodrigo/documentos/sisdis/scripts/limpiar.sh > limpiar.sh
curl http://vis.usal.es/rodrigo/documentos/sisdis/scripts/shareKeys.sh > shareKeys.sh

chmod +x *.sh

mv jaxrs-ri/ext/*.jar jaxrs-ri/api/
mv jaxrs-ri/lib/*.jar jaxrs-ri/api/


make 


while IFS= read -r line
do
	echo "$line"
done < ip.txt

mv $1.war apache-tomcat-9.0.59/webapps
cd apache-tomcat-9.0.59/bin
chmod +x *.sh
./startup.sh



exit 54
