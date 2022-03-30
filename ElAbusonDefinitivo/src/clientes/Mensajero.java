package clientes;


import utils.Utils;

/**
 * @author Anibal Vaquero y David Montagut
 *Esta clase esta pensada para ser lanzada como hilo, para hacer peticiones multiples.
 */
public class Mensajero extends Thread{
	
	private String link, metodo;
	
	
	public Mensajero (String link, String metodo) {
		this.link = link;
		this.metodo = metodo;
	}//End of builder
	
	@Override
	public void run() {
		Utils.peticion(link, metodo);
		return;
	}//End of run
}//End of class
