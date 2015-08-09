package crypto.hmm;

public class Step {
	private int obs;
	private int originalObs;
	private double c;
	private double[] alpha;
	private double[] beta;
	private double[] gamma;
	private double[][] diGamma;
	
	public int getObs() {
		return obs;
	}
	public void setObs(int obs) {
		this.obs = obs;
	}
	public int getOriginalObs() {
		return originalObs;
	}
	public void setOriginalObs(int originalObs) {
		this.originalObs = originalObs;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	public double[] getAlpha() {
		return alpha;
	}
	public void setAlpha(double[] alpha) {
		this.alpha = alpha;
	}
	public double[] getBeta() {
		return beta;
	}
	public void setBeta(double[] beta) {
		this.beta = beta;
	}
	public double[] getGamma() {
		return gamma;
	}
	public void setGamma(double[] gamma) {
		this.gamma = gamma;
	}
	public double[][] getDiGamma() {
		return diGamma;
	}
	public void setDiGamma(double[][] diGamma) {
		this.diGamma = diGamma;
	}
	
}
