package crypto.hmm;

public class HMMData {
	
	private double[] pi;
	private double[][] B;
	private double[] piBar;
	private double[][] Bbar;
	public double[] getPi() {
		return pi;
	}
	public void setPi(double[] pi) {
		this.pi = pi;
	}
	public double[][] getB() {
		return B;
	}
	public void setB(double[][] b) {
		B = b;
	}
	public double[] getPiBar() {
		return piBar;
	}
	public void setPiBar(double[] piBar) {
		this.piBar = piBar;
	}
	public double[][] getBbar() {
		return Bbar;
	}
	public void setBbar(double[][] bbar) {
		Bbar = bbar;
	}
	
	

}
