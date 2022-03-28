package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Utils {
	
	
	public static final String ELECCION_ACTIVA="eleccion_activa";
	public static final String ELECCION_PASIVA="eleccion_pasiva";
	public static final String ACUERDO="acuerdo";
	
	
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	} // End of method

	public static void dormir(int dormir) {
		try {
			Thread.sleep((long)(dormir*100));} catch (Exception e) {e.printStackTrace(); return;
			}
		
	}

	public static List<String> creaAgenda() {
		// TODO Auto-generated method stub
	
		 
        try {
            List<String> lines = Files.readAllLines(com.coti.tools.Rutas.pathToFileOnDesktop("ip.txt"), StandardCharsets.UTF_8);
            lines.stream().forEach(System.out::println);
            return lines;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
	}
}
