package ntpCliente;


public class Par {
	private long d,o;
	
	public Par (long d, long o) {
		this.d = d;
		this.o = o;
	}//End of builder
	
	public long getDelay() {
		return d;
	}//End of getter
	public long getOffset() {
		return o;
	}//End of getter
}//End of class
