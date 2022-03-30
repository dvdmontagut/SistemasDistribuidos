package servicios;


/**
 * @author Anibal Vaquero y David Montagut
 */

public class Estado {

	private int estado = -1;
	
	public Estado(String s) throws Exception {
		
		switch(s) {
			case "acuerdo": 		estado = 0; break;
			case "eleccion_activa": estado = 1; break;
			case "eleccion_pasiva": estado = 2; break;
			default : throw new Exception("Estado no conocido");
		}
	}
	
	public boolean setEstado(String s) {
		
		switch(s) {
			case "acuerdo": 		estado = 0; break;
			case "eleccion_activa": estado = 1; break;
			case "eleccion_pasiva": estado = 2; break;
			default : return false;
		}
		return true;
		
	}
	
	public int getEstado(){
		return this.estado;
	}
	
	@Override
	public String toString() {
		
		switch(this.estado) {
			case 0: return "acuerdo";
			case 1: return "eleccion_activa";
			case 2: return "eleccion_pasiva";
		
		}
		return "estado_no_inicializado";
	}
	
}
