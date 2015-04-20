package crypto.purple;

@SuppressWarnings("serial")
public class PurpleExceptionSwitchLength extends Exception{
	
	/**
	 * Throws an exception when the length of the switches is not 4
	 */
	public PurpleExceptionSwitchLength()
	{
		System.out.println("Length of the switch should be 4");
	}

}
