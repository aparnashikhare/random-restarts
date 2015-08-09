package crypto.hmm;

public class ScoreUnit {
	private double[] scores;
	private int maxCount;
	private int currCount;
	private int sizeOfArray;
	private int loc;
	private double avg;
	public double[] getScores() {
		return scores;
	}
	public void setScores(double[] scores) {
		this.scores = scores;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getCurrCount() {
		return currCount;
	}
	public void setCurrCount(int currCount) {
		this.currCount = currCount;
	}
	public int getSizeOfArray() {
		return sizeOfArray;
	}
	public void setSizeOfArray(int sizeOfArray) {
		this.sizeOfArray = sizeOfArray;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	
}
