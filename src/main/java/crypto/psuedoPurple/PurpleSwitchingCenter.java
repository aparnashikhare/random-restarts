package crypto.psuedoPurple;

import crypto.psuedoPurple.PurpleExceptionInvalidDimensions;

public class PurpleSwitchingCenter {
	
	private int[][] decryptSwitch;
	private int[][] encryptSwitch;
	private int noOfRows;
	private int noOfColns;
	private int switchPosition;
	
	public int getSwitchPosition() {
		return switchPosition;
	}
	public void setSwitchPosition(int row) throws PurpleExceptionInvalidDimensions{
		if(row<0 || row>this.noOfRows)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid position of the switch");
		}
		
		this.switchPosition = row;
	}
	public PurpleSwitchingCenter(int[][] decryptSwitch,int[][] encryptSwitch,int startingPos) throws PurpleExceptionInvalidDimensions
	{
		this.decryptSwitch=decryptSwitch;
		this.noOfRows=decryptSwitch.length;
		this.noOfColns=decryptSwitch[0].length;
		
		for(int[] numberOfRows:this.decryptSwitch)
		{
			if(this.noOfColns!=numberOfRows.length)
			{
				throw new PurpleExceptionInvalidDimensions("Invalid decrypt dimensions");
			}
		}
		if(encryptSwitch==null)
		{
			this.encryptSwitch=PurpleSettings.buildEncryptSwitchesTable();
		}
		else
		{
			this.encryptSwitch=encryptSwitch;
		}
		for(int[] numOfRows:this.encryptSwitch)
		{
			if(numOfRows.length != this.noOfColns)
			{
				throw new PurpleExceptionInvalidDimensions("Mismatch in encrypt and decrypt dimesions");
			}
		}
		
		if(this.noOfRows !=this.encryptSwitch.length)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid encrypt dimensions");
		}
		setSwitchPosition(startingPos);
	}
	
	//Switch Position is stepped by one position to the right by incrementing switchPosition
	public int purpleStepping()
	{
		this.switchPosition=(this.switchPosition+1)%this.noOfRows;
		return this.switchPosition;
	}
	//The number in the particular row and column of decryptSwitch matrix is returned 
	public int purpleDecrypt(int column)
	{
		return this.decryptSwitch[this.switchPosition][column];
	}
	//The number in the particular row and column of encryptSwitch matrix is returned 
	public int purpleEncrypt(int column)
	{
		return this.encryptSwitch[this.switchPosition][column];
	}
	/*
	 * Input:starting position of the switch
	 * This method calls the constructor PurpleSwitchingCenter
	 */
	public static PurpleSwitchingCenter buildSwitch(int startingPos) throws PurpleExceptionInvalidDimensions
	{
		PurpleSwitchingCenter purpleSwitch=new PurpleSwitchingCenter(PurpleSettings.decryptSwitch, PurpleSettings.encryptSwitch, startingPos);
		return purpleSwitch;
	}
}
