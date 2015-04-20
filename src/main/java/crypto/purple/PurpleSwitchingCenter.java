package crypto.purple;

/*
 * This class is used to handle the building of switches
 * namely sixes,twentiesOne,twentiesTwo,twentiesThree
 */
public class PurpleSwitchingCenter {
	
	private int[][] decryptSwitch;
	private int[][] encryptSwitch;
	private int noOfRows;
	private int noOfColns;
	private int switchPosition;
	
	// enumeration for different switches
	public enum Switches
	{
		Sixes(1),TwentiesOne(2),TwentiesTwo(3),TwentiesThree(4);
		
		int value;
		Switches(int value) {
			this.value = value;
		}
		public int valueOf()
		{
			return value;
		}
	}
	/*
	 * Constructor for PurpleSwitchingCenter class which is called from buildSwitch method.
	 * Input :decryptSwitch,encryptSwitch and startingPos for a switch 
	 * It performs validations on the dimensions and builds encrypt switch using the decrypt switch
	 * It sets the starting position for the switch
	 */
	public PurpleSwitchingCenter(int[][] decryptSwitch,int[][] encryptSwitch,int startingPos) throws PurpleExceptionInvalidDimensions
	{
		//startingPos=0;
		this.decryptSwitch=decryptSwitch;
		this.noOfRows=decryptSwitch.length;
		this.noOfColns=decryptSwitch[0].length;
		
		for(int[] numberOfRows:this.decryptSwitch)
		{
			if(numberOfRows.length != this.noOfColns)
			{
				throw new PurpleExceptionInvalidDimensions("Invalid decrypt dimensions");
			}
		}
		
		if(encryptSwitch==null)
		{
			//construct encrypt table using the given decrypt switch if encrypt switch is null
			this.encryptSwitch=PurpleSettingsData.buildEncryptSwitchesTable(decryptSwitch); 
		}
		else
		{
			this.encryptSwitch=encryptSwitch;
		}
		for(int[] numberOfRows:this.encryptSwitch)
		{
			if(numberOfRows.length != this.noOfColns)
			{
				throw new PurpleExceptionInvalidDimensions("Mismatch in encrypt and decrypt dimesions");
			}
		}
		
		if(this.noOfRows != this.encryptSwitch.length)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid encrypt dimensions");
		}
		//set staring position for the switch
		setSwitchPosition(startingPos);
	}
	
	

	public int getSwitchPosition() {
		return this.switchPosition;
	}
	//sets the switch position to a particular in the row
	public void setSwitchPosition(int row) throws PurpleExceptionInvalidDimensions
	{
		if(row < 0 && row > this.noOfRows )
		{
			throw new PurpleExceptionInvalidDimensions("Invalid position of the switch");
		}
		this.switchPosition=row;
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
	 * Input: switchType (can be sixes,twentiesOne,twentiesTwo,twentiesThree) and starting position of the switch
	 * Validates the switchType input parameter and calls the constructor PurpleSwitchingCenter
	 */
	public static PurpleSwitchingCenter buildSwitch(Switches switchType,int startingPos) throws PurpleExceptionInvalidDimensions
	{
		//startingPos=0;
		boolean flag=false;
		for(Switches var:Switches.values())
		{
			if(var == switchType)
			{
				flag=true;
				break;
			}
			
		}
		if(!flag)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid Switch type");
		}
		
		
		PurpleSwitchingCenter purpleSwitch=new PurpleSwitchingCenter
				(PurpleSettingsData.decryptData[switchType.valueOf()-1],
						PurpleSettingsData.encryptData[switchType.valueOf()-1],
						startingPos);
		
		return purpleSwitch;
		
	}
}
