files_servidor := servidor/Proceso.java servidor/Estado.java utils/Utils.java clientes/Mensajero.java
files_cliente := clientes/Gestor.java clientes/Mensajero.java utils/Utils.java
class_cliente := clientes/Gestor.class clientes/Mensajero.class utils/Utils.class
class_servidor := servidor/Proceso.class servidor/Estado.class utils/Utils.class clientes/Mensajero.class

servidor:
	javac -cp "./jaxrs-ri/api/*.jar" $(files_servidor)
	jar -cvmf manifestProceso Proceso.jar $(class_servidor)
cliente:
	javac -cp "./jaxrs-ri/api/*.jar" $(files_cliente)
	jar -cvmf manifestGestor Gestor.jar $(class_cliente)
clean:
	rm -r -d apache-tomcat-9.0.59
	rm -r -d jaxrs-ri
	rm -r -d tom.zip
	rm -r -d jerry.zip
	rm *.class *.java *.jar lanzar.sh limpiar.sh shareKeys.sh
