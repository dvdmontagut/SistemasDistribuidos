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
public class Carrera100{
	
	private int numAtletas=4;
	private int numPreparados=Integer.MAX_VALUE;
	private int numListos=Integer.MAX_VALUE;
	
	private long tiempoInicio;
	private long [] atletas;
	private Semaphore semP;
	private Semaphore semL;
	private Semaphore semY;
	
	@POST 
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("reinicio") 
	public String reinicio(@QueryParam(value="num") int num) 
	{
		numAtletas=num;
		atletas = new long[numAtletas];
		numPreparados=0;
		semP = new Semaphore(1);
		semL = new Semaphore(0);
		semY = new Semaphore(0);
		return "Reiniciado";
	}//End of method
	
	@GET
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("preparado")
	public String preparado() 
	{
		if(this.numPreparados == Integer.MAX_VALUE || this.numPreparados == this.numAtletas)
			return "error P";
		try {
			semP.acquire();
		
			numPreparados++;
		
			if(this.numPreparados == this.numAtletas)
				semL.release(this.numAtletas);
		
			semP.release();
		
			semL.acquire();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Listos...";
	}//End of method
	
	@GET
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("listo") 
	public String listo() 
	{
		if(this.numListos == Integer.MAX_VALUE || this.numListos == this.numAtletas)
			return "error L";
		try {
			semP.acquire();
		
			numListos++;
		
			if(this.numListos == this.numAtletas) {
				this.tiempoInicio=System.currentTimeMillis();
				semY.release(this.numAtletas);
			}
				
		
			semP.release();
		
			semY.acquire();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Ya";
	}//End of method
	
	@GET 
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("resultados") 
	public String resultados() 
	{
		String respuesta="";
		for (int i = 0; i < numAtletas; i++) {
			respuesta=respuesta+ "atleta"+Integer.toString(i)+atletas[i];
		}//End of for
		return respuesta;
	}//End of method
	
	@POST 
	@Produces(MediaType.TEXT_PLAIN) 
	@Path("llegada") 
	public String llegada(@QueryParam(value="dorsal") int dorsal) 
	{
		if(dorsal >= this.numAtletas || dorsal < 0)
			return "error D";
		atletas[dorsal-1]=System.currentTimeMillis()-tiempoInicio;
		return "Tiempo!!";
	}//End of method
}//End of class