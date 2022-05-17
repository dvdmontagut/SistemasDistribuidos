package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Anibal Vaquero y David Montagut
 */

public class Utils {
	
	
	public static final String ELECCION_ACTIVA="eleccion_activa";
	public static final String ELECCION_PASIVA="eleccion_pasiva";
	public static final String ACUERDO="acuerdo";
	public static final String POST="POST";
	public static final String GET="GET";
	
	public static final String RESPONSE_ERROR="Error";
	public static final String RESPONSE_OK="1";
	
	public static final String SEPARADOR=";";
	
	public static final String CORAZON_ROJO = " O ";
	public static final String CORAZON_VERDE = " # ";
	private static final String EMOTE_ACUERDO = " :3" ;
	private static final String EMOTE_ELECCION_ACTIVA = " O.o ";
	private static final String EMOTE_ELECCION_PASIVA = " :c ";
	
	/*
	 * Usar junto a: if (output == null || output.contains("error")) {
	 * System.out.println(output == null ? "Error" : output); return false; } // End
	 * of if
	 */
	public static String peticion(String link, String method) {
				
		try {
			URL url;
			String output;
			url = new URL(link);
			StringBuilder sb = new StringBuilder();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} // End of method
	
	public static Boolean waitSem(Semaphore s, int n) {
		try {
			s.acquire(n);
		} catch (InterruptedException e) {e.printStackTrace();return false;}
		return true;
	}//End of accederSeccionCritica
	
	public static Boolean signalSem(Semaphore s, int n) {
		try {
			s.release(n);
		} catch (Exception e) {e.printStackTrace();return false;}
		return true;
	}//End of salirSeccionCritica

	public static void dormir(int dormir) {
		try {
			Thread.sleep((long)(dormir*100));} catch (Exception e) {e.printStackTrace(); return;
			}
		
	}//End of dormir

	public static List<String> creaAgenda() {
		
        try {
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.home"),"ip.txt"), StandardCharsets.UTF_8);
            List<String> agenda = new ArrayList <> ();
            for(int i = 0 ; i < lines.size() ; i++) {
            	StringBuilder sb = new StringBuilder();
            	sb.append("http://").append(lines.get(i)).append("/ElAbusonDefinitivo/anibal_montagut/proceso/");
            	//Ejemplo: http://localhost:8080/ElAbusonDefinitivo/anibal_montagut/proceso/estado
            	agenda.add(sb.toString());
            }//End of for
            return agenda;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
	}//End of creaAgenda

	public static String factoryInfo(String peticion) {
		String [] tokens = peticion.split(Utils.SEPARADOR);
		StringBuilder sb = new StringBuilder();
		String estado ="";
		
		sb.append("\n").append("PROCESO: ").append(tokens[0]).append("\n");
		sb.append("\t").append(tokens[1].equals("apagado")?Utils.CORAZON_ROJO:Utils.CORAZON_VERDE);
		sb.append(" ").append(tokens[1].toUpperCase()).append("\n");
		if(tokens[2].toUpperCase().equals(Utils.ACUERDO.toUpperCase()))
			estado = tokens[2].toUpperCase() + " " + Utils.EMOTE_ACUERDO;
		if(tokens[2].toUpperCase().equals(Utils.ELECCION_ACTIVA.toUpperCase()))
			estado = tokens[2].toUpperCase() + " " + Utils.EMOTE_ELECCION_ACTIVA;
		if(tokens[2].toUpperCase().equals(Utils.ELECCION_PASIVA.toUpperCase()))
			estado = tokens[2].toUpperCase() + " " + Utils.EMOTE_ELECCION_PASIVA;
		sb.append("\t").append("Estado: ").append(estado).append("\n");
		sb.append("\t").append("Id Coordinador: ").append(tokens[3]).append("\n");
		return sb.toString();
	}
	
	public static Logger crearLogger(String nombreClase,String id) {
		
		//https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
		//https://www.geeksforgeeks.org/logger-getlogger-method-in-java-with-examples/?ref=lbp
		Logger logger = Logger.getLogger(nombreClase);  
	    FileHandler fh;  
	    boolean creado = false;
	    int intento = 0;
	    String ruta, base = System.getProperty("user.home").concat("/bullyLogs");
	    
	    File carpeta = new File(base);
	        carpeta.mkdir();
	    
	    do {
	    	intento++;
	    	
	    	ruta = base.concat("/"+intento+"proceso"+id+".log");
		    File f = new File (ruta);
		    if(!f.exists()) {
		    	creado = true;
		    }//End of if

	    }while(!creado);
	        	
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler(ruta);  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        logger.setUseParentHandlers(false);

	        // the following statement is used to log any messages  
	        logger.info("Log listo para empezar");  
	        return logger;
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    
	    return null;
		
	}//End of crearLoger
	
	
}//End of class
