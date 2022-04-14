package ntpCliente;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Cliente {

	
	public static void main(String[] args) {
		List<String> links = new ArrayList<>();
		links.add("http://localhost:8080/NTP/montadito/tiempo/pedir"); //la manera de añadir servidores es utilizar links.add("");
		//links.add("http://000.000.000.000:8080/NTP/montadito/tiempo/pedir");
		int repeticiones = 1; //Esto es opcional lo dejamos en uno, pero por curiosidad probamos con más iteraciones de las necesarias.
		for(int i=0; i<repeticiones; i++)
			todo(links); //La función todo se encarga de ejecutar el problema.
		
	}//End of main
	
	private static void todo (List<String> links) {
		List<Par> pares = new ArrayList<>();
		List<Long> valores;
		
		for (String s : links) {
			int repeticiones = 8;//Número de peticiones que se hacen al servidor.
			long delay, offset = Integer.MAX_VALUE;
			
			for (int i = 0; i < repeticiones; i++) {
				long t0 = System.currentTimeMillis();
				List<Long> t = pedirDatos(s);
				long t3 = System.currentTimeMillis();

				delay = DeterminarDelay(t0,t.get(0),t.get(1),t3);
				offset = DeterminarOffset(delay, t0, t.get(0), t.get(1), t3);
				pares.add(new Par(delay,offset));
			}//End of for
			valores = Marzullo.calcular(pares);
			System.out.println("Para el link " + s + " Obtenemos: [" + valores.get(0) + "," + valores.get(1) + "]");
		}
	}
	
	private static long DeterminarDelay(long t0, Long t1, Long t2, long t3) {
		return t1+t3-t2-t0;
	}//End of method
	
	private static long DeterminarOffset(long delay, long t0, Long t1, Long t2, long t3) {
		long oi = t1-t0 +t2 - t3;
		long offsetMax = oi+delay/2;
		long offsetMin = oi-delay/2;
		return (offsetMax + offsetMin)/2;
	}//End of method
	
	public static List<Long> pedirDatos(String link) {
		return factoryNumeros(peticion(link,"GET"));
	}//End of method

	public static List<Long> factoryNumeros(String s) {
		List<Long> l = new ArrayList<>();
		String[] tokens = s.split(";");
		l.add(Long.parseLong(tokens[0]));
		l.add(Long.parseLong(tokens[1]));
		return l;
	}//End of method
	

	private static String peticion(String link, String method) {
		try {
			URL url;
			String output;
			url = new URL(link);
			StringBuilder sb = new StringBuilder();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}//End of if
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}//End of while
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {e.printStackTrace();return null;}
	}//End of method
	
}//End of class

