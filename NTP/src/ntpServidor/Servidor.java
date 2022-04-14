package ntpServidor;

import java.util.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("tiempo")
public class Servidor {
	@GET //tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) //tipo de texto devuelto
	@Path("pedir") //ruta al método
	public String pedir() //el método debe retornar String
	{
		Random r = new Random();
		float espera = r.nextFloat();
		
		long t1 = System.currentTimeMillis();
		try {
			Thread.sleep((long)(espera * 1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		
		return t1+";"+t2;
	
	}//End of pedir
}//End of class

