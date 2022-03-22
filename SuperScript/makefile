files_servidor := Carrera100.java
files_cliente := Atleta.java CarreraMain.java
class_cliente := Atleta.class CarreraMain.class
class_servidor := Carrera100.class
class := Atleta.class  Carrera100.class  CarreraMain.class
jars := jaxrs-ri/etc/aopalliance-repackaged-2.6.1.jar jaxrs-ri/etc/hk2-api-2.6.1.jar jaxrs-ri/etc/hk2-locator-2.6.1.jar jaxrs-ri/etc/hk2-utils-2.6.1.jar jaxrs-ri/etc/jakarta.activation-api-1.2.2.jar jaxrs-ri/etc/jakarta.annotation-api-1.3.5.jar jaxrs-ri/etc/jakarta.inject-2.6.1.jar jaxrs-ri/etc/jakarta.json-1.1.6.jar jaxrs-ri/etc/jakarta.json-1.1.6-module.jar jaxrs-ri/etc/jakarta.json-api-1.1.6.jar jaxrs-ri/etc/jakarta.json.bind-api-1.0.2.jar jaxrs-ri/etc/jakarta.persistence-api-2.2.3.jar jaxrs-ri/etc/jakarta.servlet-api-4.0.3.jar jaxrs-ri/etc/jakarta.validation-api-2.0.2.jar jaxrs-ri/etc/jakarta.ws.rs-api-2.1.6-sources.jar jaxrs-ri/etc/jakarta.xml.bind-api-2.3.3.jar jaxrs-ri/etc/javassist-3.25.0-GA.jar jaxrs-ri/etc/org.osgi.core-6.0.0.jar jaxrs-ri/etc/osgi-resource-locator-1.0.3.jar jaxrs-ri/etc/yasson-1.0.9.jar jaxrs-ri/api/jakarta.ws.rs-api-2.1.6.jar jaxrs-ri/lib/jersey-client.jar jaxrs-ri/lib/jersey-common.jar jaxrs-ri/lib/jersey-container-servlet-core.jar jaxrs-ri/lib/jersey-container-servlet.jar jaxrs-ri/lib/jersey-hk2.jar jaxrs-ri/lib/jersey-media-jaxb.jar jaxrs-ri/lib/jersey-media-json-binding.jar jaxrs-ri/lib/jersey-media-sse.jar jaxrs-ri/lib/jersey-server.jar

jarse := "jaxrs-ri/etc/aopalliance-repackaged-2.6.1.jar" "jaxrs-ri/etc/hk2-api-2.6.1.jar"


servidor:
	javac -cp ./jaxrs-ri/api/*.jar $(files_cliente)
	jar -cvmf manifest 
cliente: 
	javac -cp ./jaxrs-ri/api/*.jar $(files_cliente)
	jar cvmf manifest Cliente.jar
	
clean:
	rm -r -d apache-tomcat-9.0.59
	rm -r -d jaxrs-ri
	rm -r -d tom.zip
	rm -r -d jerry.zip
	rm *.class *.java *.jar lanzar.sh limpiar.sh shareKeys.sh
