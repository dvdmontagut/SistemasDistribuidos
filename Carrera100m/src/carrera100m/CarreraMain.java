package carrera100m;

import java.net.*;
import java.io.*;

public class CarreraMain {

	public static void main(String[] args) {
		int numAtletas = 4;

		// Reinicio
		if (!reiniciar(numAtletas))
			return;
		// Creo los corredores
		crearAtletas(numAtletas);
		// Pido resultados
		if (!resultados())
			;
		return;
	}// End of main

	private static boolean reiniciar(int numAtletas) {
		String link = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100/reinicio?num="
				+ numAtletas;
		String output = peticion(link, "POST");
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error reiniciar" : output);
			return false;
		} // End of if
		System.out.println(output);
		System.out.flush();
		return true;
	}// End of method

	private static void crearAtletas(int numAtletas) {
		for (int i = 0; i < numAtletas; i++)
			new Thread(new Atleta(i)).start();
	}// End of method

	private static boolean resultados() {

		String link = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100/resultados";
		String output = peticion(link, "GET");
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error resultados" : output);
			return false;
		} // End of if
		System.out.println(output);
		System.out.flush();
		return true;
	}// End of method

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
