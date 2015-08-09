package crypto.psuedoPurple;

import java.util.HashMap;
import java.util.Map;

import crypto.psuedoPurple.PurpleExceptionInvalidDimensions;
import crypto.psuedoPurple.PurpleMachine;

/*Constructs Purple machine with the method constructPurpleMachine and constructor
 * Initializes the switch with buildSwitch method
 * Creates a plugboard map using alphabetLetters
 * Performs validations on the switches and alphabetLetters
 * 
 */
public class PurpleMachine {
	final static String validLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String plugboard="AEIOUYBCDFGHJKLMNPQRSTVWXZ";
	private PurpleSwitchingCenter variableSwitch;
	private String alphabetLetters;
	private static Map<Character,Integer> plugboardMap=new HashMap<Character, Integer>();
	
	public PurpleMachine(int initialPos, String alphabet) throws PurpleExceptionInvalidDimensions {
		this.variableSwitch=PurpleSwitchingCenter.buildSwitch(initialPos);
		if(alphabet ==null)
		{
			alphabet=plugboard;
		}
		if(alphabet.length() !=26)
		{
			throw new PurpleExceptionInvalidDimensions("Alphabet length should be 26");
		}
		alphabet=alphabet.toUpperCase();
		Map<Character,Integer> ctr=new HashMap<Character,Integer>();
		for(int i=0;i<validLetters.length();i++)
		{
			ctr.put(validLetters.charAt(i), 1);
		}
		for(int i=0;i<alphabet.length();i++)
		{
			if(ctr.containsKey(alphabet.charAt(i)))
			{
				ctr.put(alphabet.charAt(i), 2);
			}
		}
		for(int eachValue:ctr.values())
		{
			if(eachValue !=2)
			{
				throw new PurpleExceptionInvalidDimensions("Invalid alphabet");
			}
		}
		this.alphabetLetters=alphabet;
		 //maps the plugboard to values of indexes
        for(int i=0;i<alphabet.length();i++)
		{
        	plugboardMap.put(alphabet.charAt(i), i);
		}
	}

	public static PurpleMachine constructPurpleMachine(int switchPos,String alphabet) throws PurpleExceptionInvalidDimensions 
	{
		if(switchPos<1 || switchPos>25)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid initial positions");
		}
		int initialPos=switchPos-1;
		PurpleMachine createMachine=null;
		
		createMachine=new PurpleMachine(initialPos,alphabet);
		return createMachine;
	
	}
	/* 
	 * This method performs the decryption for the given cipher text
	 * After each character is decrypted the switch is stepped
	 */
	public String purpleDecrypt(String cipherText) throws PurpleExceptionInvalidDimensions
	{
		StringBuilder plaintext=new StringBuilder();
		for(int eachChar=0;eachChar<cipherText.length();eachChar++)
		{
			//retains - as it is 
			if(cipherText.charAt(eachChar) =='-')
			{
				plaintext.append('-');
				purpleStep();
				continue;
			}
			if(!Character.isLetter(cipherText.charAt(eachChar)))
			{
				throw new PurpleExceptionInvalidDimensions("Invalid CipherText");
			}
			else
			{
				int n=plugboardMap.get(cipherText.charAt(eachChar));
				int x=variableSwitch.purpleDecrypt(n);
				plaintext.append(this.alphabetLetters.charAt(x));
				purpleStep();
			}
		}
		return plaintext.toString();
	}
	/* 
	 * This method performs the encryption for the given plain text
	 * After each character is encrypted the switch is stepped
	 */
	public String purpleEncrypt(String plainText) throws PurpleExceptionInvalidDimensions
	{
		StringBuilder ciphertext=new StringBuilder();
		for(int eachChar=0;eachChar<plainText.length();eachChar++)
		{
			if(plainText.charAt(eachChar)==' ')
			{
				continue;
			}
			if(!Character.isLetter(plainText.charAt(eachChar)) && eachChar !=' ')
			{
				throw new PurpleExceptionInvalidDimensions("Invalid ciphertext");
			}
			else
			{
				int n=plugboardMap.get(plainText.charAt(eachChar));
				int x=variableSwitch.purpleEncrypt(n);
				ciphertext.append(this.alphabetLetters.charAt(x));
				purpleStep();
			}
		}
		return ciphertext.toString();
	}
	/*
	 * This method is used to step the switch one character at a time
	 */
	public void purpleStep() {
	
		variableSwitch.purpleStepping();
	}
	
}
