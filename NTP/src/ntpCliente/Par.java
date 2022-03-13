package ntpCliente;


public class Par {
	private long d,o;
	
	public Par (long d, long o) {
		this.d = d;
		this.o = o;
	}
	
	public long getDelay() {
		return d;
	}
	public long getOffset() {
		return o;
	}
}//End of class
