package ntpCliente;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Cliente {

	
	public static void main(String[] args) {
		List<String> links = new ArrayList<>();
		links.add("http://localhost:8080/NTP/montadito/tiempo/pedir");
		int repeticiones = 5;
		for(int i=0; i<repeticiones; i++)
			todo(links);
	}//End of main
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
	
	/*
	 * Usar junto a: if (output == null || output.contains("error")) {
	 * System.out.println(output == null ? "Error" : output); return false; } // End
	 * of if
	 */
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
	}//End of method
	private static void todo (List<String> links) {
		List<Par> pares = new ArrayList<>();
		List<Long> valores;
		
		for (String s : links) {
			int repeticiones = 8;
			long delay, offset = Integer.MAX_VALUE;
			for (int i = 0; i < repeticiones; i++) {
				long t0 = System.currentTimeMillis();
				List<Long> t = pedirDatos(s);
				long t3 = System.currentTimeMillis();
				delay = DeterminarDelay(t0,t.get(0),t.get(1),t3);
				offset = DeterminarOffset(delay, t0, t.get(0), t.get(1), t3);
				pares.add(new Par(delay,offset));
			}
			valores = Marzullo.calcular(pares);
			System.out.println("Para el link " + s + " Obtenemos: [" + valores.get(0) + "," + valores.get(1) + "]");
		}
	}
}//End of class

