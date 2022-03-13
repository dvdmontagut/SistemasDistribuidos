package ntpCliente;

import java.util.*;

public class Marzullo {
	
	//Se ha tomado como punto de partida el codigo en https://gist.github.com/tai2/3684493
	public static List<Long> calcular(List<Par> pares){
		Tupla [] tabla = new Tupla [pares.size()];
		long best = 0;
		long cnt = 0;
		
		for (int i=0; i< tabla.length;i++)
			tabla[i] = new Tupla (pares.get(i),+1);
		
		return null;
	}
}
