package carrera100m;

import java.net.*;
import java.util.Random;
import java.io.*;

public class Atleta implements Runnable {

	private int dorsal;
	private long tiempo;

	public Atleta(int dorsal) {
		this.dorsal = dorsal;
		final Random r = new Random();
		tiempo = (long)(((r.nextFloat()*2.2f) + 9.56f)*1000f);
	}// End of builder

	@Override
	public void run() {
		// Me preparo
		if (!preparado())
			return;
		// Ojo que salgo
		if (!listo())
			return;
		// Llegue
		if (!llegue())
			return;
	}// End of run

	private boolean preparado() {
		String link = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100/preparado";
		String output = peticion(link, "GET");
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error" : output);
			return false;
		} // End of if
		System.out.println("Atleta " + this.dorsal + ": " + output);
		System.out.flush();
		return true;
	}

	private boolean listo() {
		String link = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100/listo";
		String output = peticion(link, "GET");
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error" : output);
			return false;
		} // End of if
		System.out.println("Atleta " + this.dorsal + ": Empieza");
		System.out.flush();
		return true;
	}

	private boolean llegue() {
		try {Thread.sleep(this.tiempo);} catch (InterruptedException e) {e.printStackTrace();}
		String link = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100/llegada?dorsal="+dorsal;
		String output = peticion(link, "POST");
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error" : output);
			return false;
		} // End of if
		System.out.println("Atleta " + this.dorsal + ": Llegó");
		System.out.flush();
		return true;
	}

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
	}
}// End of class
