package carrera100m;

import java.net.*;
import java.io.*;

public class CarreraMain {

	private static String linkd = "http://localhost:8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100";

	public static void main(String[] args) {
		
		/* El link del servidor se almacena en la siguiente variable que puede ser modificada por argumentos (como se muestra a continuación) o a mano*/
		// linkd = "http://" + args[1] + ":8080/Carrera100m_DavidMontagut_AnibalVaquero/Carrera100";
		
		/*Hay 3 maneras de ejecutar el método principal:
		 * 	Servidor y clientes en la misma máquina:
		 * 		Para ello hay que utilizar: unOrdenador(numero de atletas);
		 * Servidor y clientes distribuidos, se diferencian 2 roles, el cliente que reinicia y los demas:
		 * 		El primero de los clientes debe ejecutar el método variosOrdenadores(numero atletas, numero total atletas); que implica reinicio de variables,
		 * 		Los demás clientes ejecutarán el método serCliente(numero atletas);
		 * 
		 * La manera de alternar entre modos de ejecución es descomentar las lineas pertinentes a continuación:*/
		
		// No distribuido, necesita el número de atletas totales:
		unOrdenador(4);
		
		
		// Distribuido
		
		/*Primer cliente, debe establecer el numero de atletas que añade a la carrera y cual va a ser el número total de atletas*/
		// variosOrdenadores(4, 12);
		
		/* Demás clientes, debe establecer cuantos atletas añade a la carrera*/
		// serCliente(4);
	}// End of main



	public static void unOrdenador(int numAtletas) {


		// Reinicio
		if (!reiniciar(numAtletas))
			return;
		// Creo los corredores
		crearAtletas(numAtletas);
		// Pido resultados
		if (!resultados())
			;
		return;
	}


public static void variosOrdenadores(int numAtletas, int numAtletasTotales) {

		// Reinicio
		if (!reiniciar(numAtletasTotales))
			return;
		// Creo los corredores
		crearAtletas(numAtletas);
		// Pido resultados
		if (!resultados())
			;
		return;
	}

public static void serCliente(int numAtletas) {


	// Creo los corredores
	crearAtletas(numAtletas);
	return;
}

	private static boolean reiniciar(int numAtletas) {
		String link = linkd + "/reinicio?num="
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
			new Thread(new Atleta(0,linkd)).start();
	}// End of method

	private static boolean resultados() {

		String link = linkd + "/resultados";
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
