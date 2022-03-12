package carrera100m;

import java.util.concurrent.Semaphore;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("Carrera100")
@Singleton
public class Carrera100 {

	private int numAtletas = 4;
	private int numPreparados = Integer.MAX_VALUE;
	private int numListos = Integer.MAX_VALUE;

	private long tiempoInicio;
	private long[] atletas;
	private Semaphore semP, semR, semL, semY;
	private boolean corriendo, listo, carreraEmpezada = false;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("reinicio")
	public String reinicio(@QueryParam(value = "num") int num) {
		if (carreraEmpezada)
			return "error E";
		carreraEmpezada = true;
		corriendo = listo = false;
		numAtletas = num;
		atletas = new long[numAtletas];
		numPreparados = numListos = 0;
		semP = new Semaphore(1);
		semL = new Semaphore(0);
		semY = new Semaphore(0);
		semR = new Semaphore(0);
		return "Preparados";
	}// End of method

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("preparado")
	public String preparado() {
		if (!this.carreraEmpezada || this.listo)
			return "error E1";
		try {
			semP.acquire();

			numPreparados++;

			if (this.numPreparados == this.numAtletas)
				semL.release(this.numAtletas);

			semP.release();

			semL.acquire();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		listo = true;
		return "Listos... ";
	}// End of method

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("listo")
	public String listo() {
		if (this.corriendo || !this.listo)
			return "error E2";
		try {
			semP.acquire();

			numListos++;

			if (this.numListos == this.numAtletas) {
				this.tiempoInicio = System.currentTimeMillis();
				semY.release(this.numAtletas);
			}

			semP.release();

			semY.acquire();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		corriendo = true;
		return "Ya";
	}// End of method

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("llegada")
	public String llegada(@QueryParam(value = "dorsal") int dorsal) {
		if (!this.listo)
			return "error E3";
		if (dorsal >= this.numAtletas || dorsal < 0)
			return "error D";
		atletas[dorsal] = System.currentTimeMillis() - tiempoInicio;
		semR.release();
		return "Tiempo!!";
	}// End of method

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("resultados")
	public String resultados() {
		if (!this.carreraEmpezada)
			return "error E";
		try {
			semR.acquire(this.numAtletas);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		carreraEmpezada = false;
		String respuesta = "";
		for (int i = 0; i < numAtletas; i++) {
			respuesta = respuesta + " atleta: " + Integer.toString(i) +" "+ atletas[i]+"\n";
		} // End of for
		semR.release(numAtletas);
		return respuesta;
	}// End of method

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hardReset")
	public String hardReset() {
		corriendo = listo = true;
		semP.release(99);
		semL.release(99);
		semY.release(99);
		semR.release(99);
		try {Thread.sleep((long)12000);} catch (InterruptedException e) {e.printStackTrace();}
		carreraEmpezada = false;
		return "Hard Reset";
	}// End of method
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "Aqui esta el tio";
	}// End of method
}// End of class