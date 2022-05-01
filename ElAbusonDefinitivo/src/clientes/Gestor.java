package clientes;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.coti.tools.Esdia;

import utils.Utils;
import clientes.Mensajero;

public class Gestor {
	
	private static Logger log;
	
	public static void main(String[] args) {
		boolean fin = false;
		log = Utils.crearLoger(Gestor.class.getName(), "Gestor");
		iniciarProcesos();
		
		String menu = "==========MENU==========\n" +
				"========PRINCIPAL========\n" +
				"Pulse 1 para parar procesos.\n" +
				"Pulse 2 para arrancar procesos.\n" +
				"Pulse 3 para obtener información.\n" +
				"Pulse 4 para salir.\n";
		while(!fin) {
			int input = Esdia.readInt(menu);
			switch(input) {
			case 1: menuParar(); break;
			case 2: menuArrancar(); break;
			case 3: pedirInfo(); break;
			case 4: fin=true; break;
			default: break;
			}//End of switch
		}//End of while
	}//End of main

	

	

	private static void iniciarProcesos() {
		List<String> agenda = Utils.creaAgenda();
		for(int i=0; i<agenda.size(); i++) {
			Mensajero m = new Mensajero(agenda.get(i)+"inicio?id="+i, Utils.POST);
			m.start();
		}//End of for
		log.info("Inciando procesos del 1 al " + (agenda.size()-1));
	}//End of iniciarProcesos





	private static void menuParar() {
		Set <Integer> peticion = new HashSet<>();
		boolean salir = false;

		while(!salir) {
			String menu = "----------MENU----------\n" +
					"----------PARAR---------\n" +
					"Escriba los número de los procesos a parar\n" +
					"Pulse 'q' para cancelar y pulse 'y' para confirmar\n\n"+
					"Procesos seleccionados("+peticion.size()+"):" + peticion.toString() + "\n\n\n";
			String valor = Esdia.readString(menu);
			switch(valor.toUpperCase()) {
			case "Q":
				salir = true;
				break;
			case "Y":
				parar(peticion);
				salir = true;
				break;
			default:
				int n;
				try {
					n = Integer.parseInt(valor);
				}catch(Exception e) {System.out.println("Valor no válido.");break;}
				if(comprobar(n)) {
						peticion.add(n);
						
				}//End if
			}//End switch
		}//End of while
	}//End of menuParar
	
	private static void parar(Set<Integer> peticion) {
		List<String> agenda = Utils.creaAgenda();
		for (int i:peticion) {
			String link = agenda.get(i) + "apagar";
			Mensajero m = new Mensajero(link, Utils.POST);
			m.start();
		}//End of for
		log.info("Se ha mandado parar los procesos: "+ peticion.toString());
	}//End of parar

	private static boolean comprobar(int n) {
		return true;
	}//End of comprobar

	private static void menuArrancar() {
		
		Set <Integer> peticion = new HashSet<>();
		boolean salir = false;

		while(!salir) {
			String menu = "++++++++++MENU++++++++++\n" +
					"++++++++ARRANCAR++++++++\n" +
					"Escriba los número de los procesos a arrancar\n" +
					"Pulse 'q' para cancelar y pulse 'y' para confirmar\n\n"+
					"Procesos seleccionados("+peticion.size()+"):" + peticion.toString() + "\n\n\n";
			String valor = Esdia.readString(menu);
			switch(valor.toUpperCase()) {
			case "Q":
				salir = true;
				break;
			case "Y":
				arrancar(peticion);
				salir = true;
				break;
			default:
				int n;
				try {
					n = Integer.parseInt(valor);
				}catch(Exception e) {System.out.println("Valor no válido.");break;}
				if(comprobar(n)) {
						peticion.add(n);	
				}//End if
			}//End switch
		}//End of while
	}//End of menuArrancar
	
	private static void arrancar(Set<Integer> peticion) {
		List<String> agenda = Utils.creaAgenda();
		for (int i:peticion) {
			String link = agenda.get(i) + "arrancar";
			Mensajero m = new Mensajero(link, Utils.POST);
			m.start();
		}//End of for
		log.info("Se ha mandado arrancar los procesos: "+ peticion.toString());
	}//End of arrancar





	private static void pedirInfo() {
		List<String> agenda = Utils.creaAgenda();
		StringBuilder sb = new StringBuilder();
		for(String i:agenda) {
			String s;
			s = Utils.factoryInfo(Utils.peticion(i+"estado", Utils.GET));
			System.out.println(s);
			sb.append(s).append("\n");
		}//end of for
		log.info(sb.toString());
	}//End of pedirInfor
	
}//End of class



