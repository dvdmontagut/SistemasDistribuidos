package clientes;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coti.tools.Esdia;

import utils.Utils;

public class Gestor {
	
	public static void main(String[] args) {
		boolean fin = false;
		
		String menu = "==========MENU==========\n" +
				"========PRINCIPAL========\n" +
				"Pulse 1 para parar procesos.\n" +
				"Pulse 2 para arrancar procesos.\n" +
				"Pulse 3 para obtener información.\n" +
				"Pulse 4 para salir.\n";
		String [] opciones = {"1","2","3","4"};
		while(!fin) {
			String input = Esdia.readString(menu, opciones);
			switch(input) {
			case "1": menuParar(); break;
			case "2": menuArrancar(); break;
			case "3": pedirInfo(); break;
			case "4": fin=true;
			}//End of switch
		}//End of while
	}//End of main

	

	

	private static void menuParar() {
		Set <Integer> peticion = new HashSet<>();
		boolean salir = false;

		while(!salir) {
			String menu = "----------MENU----------\n" +
					"----------PARAR---------\n" +
					"Escriba los número de los procesos a para\n" +
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
			System.out.println("Se ha mandado apagar al proceso " + i);
		}//End of for
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
					"Escriba los número de los procesos a para\n" +
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
			System.out.println("Se ha mandado arrancar al proceso " + i);
		}//End of for
	}//End of arrancar





	private static void pedirInfo() {
		List<String> agenda = Utils.creaAgenda();
		
		for(String i:agenda) 
			System.out.println(Utils.factoryInfo(Utils.peticion(i+"datos", Utils.GET)));
	}//End of pedirInfor
	
}//End of class



