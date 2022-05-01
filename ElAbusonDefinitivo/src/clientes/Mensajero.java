package clientes;


import java.util.concurrent.Semaphore;

import utils.Utils;

/**
 * @author Anibal Vaquero y David Montagut
 *Esta clase esta pensada para ser lanzada como hilo, para hacer peticiones multiples.
 */
public class Mensajero extends Thread{
	
	private String link, metodo;
	private Semaphore s;
	
	
	public Mensajero (String link, String metodo) {
		this.link = link;
		this.metodo = metodo;
		this.s = null;
	}//End of builder
	
	public Mensajero (String link, String metodo, Semaphore ss) {
		this.link = link;
		this.metodo = metodo;
		this.s = ss;
	}//End of builder
	
	@Override
	public void run() {
		if(s == null) {
			Utils.peticion(link, metodo);
			return;
		}
		Utils.waitSem(s, 1);
		Utils.peticion(link, metodo);
		return;
		
		
	}//End of run
}//End of class
