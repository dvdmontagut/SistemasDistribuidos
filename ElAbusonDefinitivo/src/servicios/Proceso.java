package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import clientes.Mensajero;
import utils.Utils;


/**
 * @author Anibal Vaquero y David Montagut
 */
@Path("proceso")
@Singleton
public class Proceso extends Thread{

	
	private int id;
	private int idCordinador, posibleIdCoordinador;
	private Estado estado;
	private boolean on;
	private boolean esperandoOk, esperandoCoordinador;
	private List<String> agenda;
	
	private Semaphore muertoVivo;
	private Semaphore seccionCriticaArrancar, seccionCriticaOk, seccionCriticaEsperaOk, seccionCriticaCoordinador, seccionCriticaCambiarEstado;
	private Semaphore timeoutEleccion, timeoutCoordinador;
	
	
	//ZONA INTERNA
		public Proceso(int idProceso) {
			this.id = idProceso;
			this.idCordinador = -1;
			
			this.muertoVivo = new Semaphore (0);
			this.seccionCriticaArrancar = new Semaphore (1);
			this.seccionCriticaOk = new Semaphore (0);
			this.seccionCriticaEsperaOk = new Semaphore (1);
			this.seccionCriticaCoordinador = new Semaphore (0);
			
			this.esperandoOk = false;
			
			try {
				estado = new Estado(Utils.ELECCION_ACTIVA);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			this.on = true;
			
			try {
				agenda = Utils.creaAgenda();
			} catch (Exception e) {
				System.err.println("No hay fichero");
				return;
			}
			
			System.out.println(agenda.toString());
			
		}//End of builder
		
		public int computar() {
			
			if(this.on == false)
				return -1;
			Random r = new Random();
			Utils.dormir(r.nextInt(20) + 10);
			return 1;
			
		}// End of method
		
		@Override
		public void run() {

			while(true) {
				if(this.on == false)
					Utils.waitSem(muertoVivo, 1);
				Random r = new Random();
				Utils.dormir(r.nextInt(50) + 50);
				if(computar() == -1)
					eleccionPeticion();
			}//End of while
			
		}//End of run
		
		
	//ZONA CLIENTE

	
	/**
	 * Notifica a los procesos de mayor pid que él de que el coordinador se ha
	 * caido.
	 */
	public void eleccionPeticion() {
		
		while(true) {
			this.timeoutEleccion = new Semaphore (0);
			this.timeoutCoordinador = new Semaphore (0);
			
			for(int i=id;i<agenda.size();i++) {
				Mensajero hilo = new Mensajero(agenda.get(i)+"eleccion?id="+this.id, Utils.POST, Mensajero.OK);
				hilo.start();
			}//End of for
			
			try {
				if(timeoutEleccion.tryAcquire(1, TimeUnit.SECONDS)) {
					if(timeoutCoordinador.tryAcquire(1, TimeUnit.SECONDS)) {
						this.idCordinador=this.posibleIdCoordinador;
						return;
					}//End of if
					else {
						continue;
					}//End of else
				}//End of if
				else {
					this.idCordinador=this.id;
					coordinador();
				}//End of else
			} catch (InterruptedException e) {e.printStackTrace();}
		}//End of while
	}//End of eleccionPeticion
	
	/**
	 * Espera por la elección de un coordinador, si no obtiene respuesta
	 * significa que un proceso de id superior se ha caido y se resetea
	 * el proceso.
	 */
	public void esperarCoordinador() {
		try {
			if(timeoutCoordinador.tryAcquire(1, TimeUnit.SECONDS))
				this.idCordinador = this.posibleIdCoordinador;
			else 
				eleccionPeticion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}//End of esperarCoordinador
	
	/**
	 * Notifica a los demás procesos que este se ha convertido en coordinador
	 */
	public void coordinador() {
		this.idCordinador = this.id;
		for(String nodo : agenda)
			Utils.peticion(nodo + "coordinador?=" + this.id, Utils.POST);
	}//End of serCoordinador
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//ZONA SERVIDOR
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("arrancar")
	public String arrancar() {
		if(this.on) return "Ok";
		this.on = true;
		if(!Utils.waitSem(seccionCriticaArrancar, 1)) return "Error";
		if(muertoVivo.availablePermits()==0)
			Utils.signalSem(muertoVivo, 1);
		if(!Utils.signalSem(seccionCriticaArrancar, 1)) return "Error";
		return "Ok";
	}// End of arrancar
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("apagar")
	public String apagar() {
		this.on = false;
		return "Ok";
	}// End of apagar
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("eleccion")
	public String eleccionRespuesta (@QueryParam(value = "id") int id) {
		Utils.peticion(this.agenda.get(id)+"ok","POST");
		Utils.waitSem(this.seccionCriticaCambiarEstado, 1);
		if(this.estado.toString().equals(Utils.ACUERDO)) {
			this.estado.setEstado(Utils.ELECCION_ACTIVA);
			eleccionPeticion();
		}//End of if
		Utils.signalSem(this.seccionCriticaCambiarEstado, 1);
		return "Ok";
	}// End of eleccionRespuesta	
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("ok")
	public String ok() {
		Utils.waitSem(this.seccionCriticaCambiarEstado, 1);
		if(this.estado.toString().equals(Utils.ELECCION_ACTIVA)) {
			this.estado.setEstado(Utils.ELECCION_PASIVA);
			Utils.signalSem(this.timeoutEleccion, 1);
		}//End of if
		Utils.signalSem(this.seccionCriticaCambiarEstado, 1);
		return "Ok";
	}// End of eleccionRespuesta
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("coordinador")
	public String CoordinadorRespuesta (@QueryParam(value = "num") int num) {
		return "Ok";
	}//End of CoordinadorRespuesta
} //End of class


