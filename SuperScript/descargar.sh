cd Escritorio
tar -xzvf $1

#Descargamos jersey
curl https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.35/jaxrs-ri-2.35.zip > jerry.zip
unzip jerry.zip

mv jaxrs-ri/lib/*.jar ElAbusonDefinitivo/WebContent/WEB-INF/lib
mv jaxrs-ri/ext/*.jar ElAbusonDefinitivo/WebContent/WEB-INF/lib
mv jaxrs-ri/api/*.jar ElAbusonDefinitivo/WebContent/WEB-INF/lib


#Descargamos biblioteca
curl http://maxus.fis.usal.es/HOTHOUSE/p3/biblioteca8.jar > biblioteca8.jar

mv biblioteca8.jar ElAbusonDefinitivo/WebContent/WEB-INF/lib

#Descargamos tomcat
curl https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.62/bin/apache-tomcat-9.0.62.zip > tom.zip
unzip tom.zip
chmod +x apache-tomcat-9.0.62/bin/*.sh

../X/linux/runeclipse
