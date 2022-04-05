package utils;
import java.io.BufferedReader;
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
	
	public static final String CORAZON_ROJO = "💔";
	public static final String CORAZON_VERDE = "💚";
	
	
	
	/*
	 * Usar junto a: if (output == null || output.contains("error")) {
	 * System.out.println(output == null ? "Error" : output); return false; } // End
	 * of if
	 */
	public static String peticion(String link, String method) {
				
		try {
			URL url;
			String output;
			System.out.println(LocalDateTime.now()+": "+link);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	} // End of method
	
	public static Boolean waitSem(Semaphore s, int n) {
		try {
			s.acquire(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}//End of accederSeccionCritica
	
	public static Boolean signalSem(Semaphore s, int n) {
		try {
			s.release(n);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
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
		sb.append("\n").append("PROCESO: ").append(tokens[0]).append("\n");
		sb.append("\t").append(tokens[1].toUpperCase());
		sb.append(tokens[1].equals("apagado")?Utils.CORAZON_ROJO:Utils.CORAZON_VERDE).append("\n");
		sb.append("\t").append("Estado: ").append(tokens[2].toUpperCase()).append("\n");
		sb.append("\t").append("Id Coordinador: ").append(tokens[3]).append("\n");
		return sb.toString();
	}
	
	
}//End of class
