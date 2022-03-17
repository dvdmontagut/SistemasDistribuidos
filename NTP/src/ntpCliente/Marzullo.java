package ntpCliente;

import java.util.*;

public class Marzullo {
	
	//Se ha tomado como punto de partida el codigo en https://gist.github.com/tai2/3684493
	public static List<Long> calcular(List<Par> pares){
		List <Tupla> tabla = new  ArrayList<>();
		List <Long>valores = new  ArrayList<>();
		long cnt, best, beststart,bestend;
		cnt=best=beststart=bestend=0;
		
		for (int i=0; i< pares.size();i++) {
			tabla.add( new Tupla (pares.get(i).getOffset()-pares.get(i).getDelay(),-1));
			tabla.add(new Tupla (pares.get(i).getOffset()+pares.get(i).getDelay(),+1));
			//System.out.println("Se va a ordenar " + pares.get(i).getOffset()+ " +-" + pares.get(i).getDelay());
		}//End of for
		Collections.sort(tabla);
		for(int i=0;i<tabla.size(); i++) {
			//System.out.println(i.getT()+" "+i.getCnt());
			cnt = cnt - tabla.get(i).getCnt();	
			if(best < cnt) {
				best = cnt;
				beststart = tabla.get(i).getT();
				bestend = tabla.get(i+1).getT();
			}//End of if
		}//End of for
		valores.add(beststart);
		valores.add(bestend);
		return valores;
	}//End of calcular
}//End of class
