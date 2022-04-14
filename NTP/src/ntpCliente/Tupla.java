package ntpCliente;


public class Tupla implements Comparable<Tupla>{
	private Long t;
	private int cnt;

	public Long getT() {
		return this.t;
	}//End of getter

	public int getCnt() {
		return cnt;
	}//End of getter

	public Tupla(Long t, int cnt) {
		this.t = t;
		this.cnt = cnt;
	}// End of builder

	@Override
	public int compareTo(Tupla tupla) {
		if(this.getT()>tupla.getT())
			return 1;
		if(this.getT()<tupla.getT())
			return -1;
		if(this.getCnt()>tupla.getCnt())
			return -1;
		if(this.getCnt()<tupla.getCnt())
			return 1;
		return 0;
	}//End of compareTo
}//End of class
