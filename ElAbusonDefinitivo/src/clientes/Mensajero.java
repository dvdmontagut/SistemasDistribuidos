package clientes;

import java.util.concurrent.Semaphore;

import utils.Utils;

/**
 * @author Anibal Vaquero y David Montagut
 *Esta clase esta pensada para ser lanzada como hilo, para hacer peticiones multiples.
 */
public class Mensajero extends Thread{
	
	private String link, metodo;
	private int tipo;
	private Semaphore sem;
	
	public static final int OK = 0;
	
	public Mensajero (String link, String metodo, int tipo, Semaphore sem) {
		this.link = link;
		this.tipo = tipo;
		this.metodo = metodo;
		this.sem = sem;
	}//End of builder
	
	@Override
	public void run() {
		switch(tipo) {
		case Mensajero.OK:
			if(Utils.peticion(link, metodo).equals("Ok"))
				Utils.signalSem(sem, 1);
			break;
		}//End of switch
		return;
	}//End of run
}//End of class
