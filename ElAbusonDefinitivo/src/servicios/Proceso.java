package servicios;

import java.awt.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import utils.Utils;

@Path("proceso")
@Singleton
public class Proceso extends Thread{

	
	private int id;
	private int idCordinador;
	private Estado estado;
	private boolean on;
	private List agenda;
	
	
	//ZONA INTERNA
		public Proceso(int idProceso) {
			
			this.id = idProceso;
			this.idCordinador = -1;
			estado.setEstado(Utils.ELECCION_ACTIVA);
			this.on = true;
			agenda = Utils.creaAgenda();
			
		}
		public int computar() {
			
			if(this.on == false)
				return -1;
			Random r = new Random();
			Utils.dormir(r.nextInt(20) + 10);
			return 1;
			
		}// End of method
		
		@Override
		public void run() {

			for(;;) {
				if(this.on == false)
					return;
				Random r = new Random();
				Utils.dormir(r.nextInt(50) + 50);
				if(computar() == -1)
					eleccion();
			}
			
		}
		
		
	//ZONA CLIENTE
	
	//ABUSO DE TI
	public void ok() {
		
	}
	
	//HABLAR CON MAYORES
	public void eleccion() {
		
		
	}
	
	//SOY EL CORDINADOR
	public void coordinador() {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//ZONA SERVIDOR
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("arrancar")
	public String arrancar() {
		this.on = true;
		return "ok";
	}// End of method
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("apagar")
	public String apagar() {
		this.on = false;
		return "ok";
	}// End of method
	
	
	
	
} //End of class


