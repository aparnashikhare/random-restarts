package crypto.psuedoPurple;

@SuppressWarnings("serial")
public class PurpleExceptionInvalidDimensions extends Exception{
	/*
	 * User defined exception to handle the invalid input dimesions for 
	 * Purple machine
	 */
	public PurpleExceptionInvalidDimensions(String msg)
	{
		System.out.println(msg);
	}

}
