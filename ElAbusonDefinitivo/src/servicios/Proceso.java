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
import javax.ws.rs.GET;
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
	private List<String> agenda;
	
	private Semaphore muertoVivo;
	private Semaphore seccionCriticaArrancar, seccionCriticaCambiarEstado;
	private Semaphore timeoutEleccion, timeoutCoordinador;
	
	
	//ZONA INTERNA
		public Proceso(int idProceso) {
			this.id = idProceso;
			this.idCordinador = -1;
			
			this.muertoVivo = new Semaphore (0);
			this.seccionCriticaArrancar = new Semaphore (1);
			
			try {
				estado = new Estado(Utils.ELECCION_ACTIVA);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			this.on = true;
			
			try {
				agenda = Utils.creaAgenda();
			} catch (Exception e) {System.err.println("No hay fichero");return;}
			System.out.println(agenda.toString());
		}//End of builder
		
		
		@Override
		public void run() {

			while(true) {
				if(this.on == false)
					Utils.waitSem(muertoVivo, 1);
				Random r = new Random();
				Utils.dormir(r.nextInt(50) + 50);
				String valor = Utils.peticion(agenda.get(this.idCordinador)+"computar",Utils.GET);
				if(valor.equals(Utils.RESPONSE_ERROR))
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
			this.estado.setEstado(Utils.ELECCION_ACTIVA);
			for(int i=id+1;i<agenda.size();i++) {
				Mensajero hilo = new Mensajero(agenda.get(i)+
						"eleccion?id="+this.id, Utils.POST);
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
				else 
					coordinador();
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
		this.estado.setEstado(Utils.ACUERDO);
		for(int i=0;i<agenda.size();i++) {
			if(this.id!=i) {
				Mensajero hilo = new Mensajero(agenda.get(i)+
						"coordinador?id="+this.id, Utils.POST);
				hilo.start();
			}//End of if
		}//End for
	}//End of serCoordinador
/*============================================================================
 * ==============================ZONA SERVIDOR	==============================
 * ===========================================================================
 */
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("arrancar")
	public String arrancar() {
		if(this.on) return Utils.RESPONSE_OK;
		this.on = true;
		if(!Utils.waitSem(seccionCriticaArrancar, 1)) return "Error";
		if(muertoVivo.availablePermits()==0)
			Utils.signalSem(muertoVivo, 1);
		if(!Utils.signalSem(seccionCriticaArrancar, 1)) return "Error";
		return Utils.RESPONSE_OK;
	}// End of arrancar
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("apagar")
	public String apagar() {
		this.on = false;
		return Utils.RESPONSE_OK;
	}// End of apagar
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("computar")
	public String computar() {
		if(this.on == false)
			return Utils.RESPONSE_ERROR;
		Random r = new Random();
		Utils.dormir(r.nextInt(20) + 10);
		return Utils.RESPONSE_OK;
	}// End of method
	
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
		return Utils.RESPONSE_OK;
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
		return Utils.RESPONSE_OK;
	}// End of eleccionRespuesta
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("coordinador")
	public String CoordinadorRespuesta (@QueryParam(value = "id") int id) {
		Utils.waitSem(this.seccionCriticaCambiarEstado, 1);
		if(this.estado.toString().equals(Utils.ACUERDO)) {
			this.posibleIdCoordinador = id;
			this.estado.setEstado(Utils.ACUERDO);
			Utils.signalSem(this.timeoutCoordinador, 1);
		}//End of if
		Utils.signalSem(this.seccionCriticaCambiarEstado, 1);
		return Utils.RESPONSE_OK;
	}//End of CoordinadorRespuesta
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("estado")
	public String estado() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.id).append(Utils.SEPARADOR);
		sb.append(this.on?"encendido":"apagado");
		sb.append(Utils.SEPARADOR).append(this.estado.toString());
		sb.append(Utils.SEPARADOR).append(this.idCordinador);
		return sb.toString();
	}// End of estado
} //End of class


