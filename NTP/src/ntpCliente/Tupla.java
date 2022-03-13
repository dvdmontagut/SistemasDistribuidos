package ntpCliente;

public class Tupla {
	private Par par;
	private int cnt;

	public Par getPar() {
		return this.par;
	}

	public int getCnt() {
		return cnt;
	}

	public Tupla(Par par, int cnt) {
		this.par = par;
		this.cnt = cnt;
	}// End of builder

}
