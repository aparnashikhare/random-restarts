package crypto.purple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trials.purple.PurpleSwitchingCenter.Switches;


/*Constructs Purple machine with the method constructPurpleMachine and constructor
 * Initializes each of the switches with buildSwitch method (sixes and twenties)
 * Creates a plugboard map using alphabetLetters
 * Performs validations on the switches and alphabetLetters
 * 
 */
public class PurpleMachine {
	
	final static String validLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String plugboard="AEIOUYBCDFGHJKLMNPQRSTVWXZ";
	private PurpleSwitchingCenter sixes;
	private List<PurpleSwitchingCenter> twenties = new ArrayList<PurpleSwitchingCenter>();
	private PurpleSwitchingCenter fastSwitch;
	private PurpleSwitchingCenter middleSwitch;
	private PurpleSwitchingCenter slowSwitch;
	private String alphabetLetters;
	private static Map<Character,Integer> plugboardMap=new HashMap<Character, Integer>();
	
	
	/*
	 * Input : switchPosition array of four switches, fastSwitch, middleSwitch and alphabet
	 * This constructor build each of the four switches (sixes and three twenties)
	 * Initializes fastswitch,middleswitch and slowswitch to respective twenties switches
	 * Maps the plugboard characters
	 */
	public PurpleMachine(int switchPos[], int fastSwitch, int middleSwitch, String alphabet) 
			throws PurpleExceptionSwitchLength, PurpleExceptionInvalidDimensions {
		
		if(switchPos.length==0)
		{
			switchPos[0]=0;
			switchPos[1]=0;
			switchPos[2]=0;
			switchPos[3]=0;
		}
		if(switchPos.length !=4)
		{
			throw new PurpleExceptionSwitchLength();
		}
		
		try {
			this.sixes=PurpleSwitchingCenter.buildSwitch(Switches.Sixes, switchPos[0]);
		} catch (PurpleExceptionInvalidDimensions e) {
			
			e.printStackTrace();
				}
		try {
		this.twenties.add(PurpleSwitchingCenter.buildSwitch(Switches.TwentiesOne, switchPos[1]));
		this.twenties.add(PurpleSwitchingCenter.buildSwitch(Switches.TwentiesTwo, switchPos[2]));
		this.twenties.add(PurpleSwitchingCenter.buildSwitch(Switches.TwentiesThree, switchPos[3]));
		
		}catch (PurpleExceptionInvalidDimensions e) {
			
			e.printStackTrace();
		}
		//fast switch, middle switch and slow switch should be between 1 and 3 (inclusive)
		if(fastSwitch <1 || fastSwitch >3)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid fast switch range");
		}
		if(middleSwitch <1 || middleSwitch >3)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid middle switch range");
		}
		if(fastSwitch==middleSwitch)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid fast/middleSwitch switch ");
		
		}
		this.fastSwitch=this.twenties.get(fastSwitch-1);
		this.middleSwitch=this.twenties.get(middleSwitch-1);
		List<Integer> switches=new ArrayList<Integer>();
		switches.add(1);
		switches.add(2);
		switches.add(3);
		
		
		
		for(int i=0;i<switches.size();i++)
		{
		
			if(fastSwitch==switches.get(i))
			{
				switches.remove(i);
			}
		}
		for(int i=0;i<switches.size();i++)
		{
		
			if(middleSwitch==switches.get(i))
			{
				switches.remove(i);
			}
		}
			
		// identifies the remaining switch as slow switch
		this.slowSwitch=this.twenties.get(switches.get(0)-1);
		
			
		if(alphabet ==null)
		{
			alphabet=plugboard;
		}
		
		if(alphabet.length() !=26)
		{
			throw new PurpleExceptionInvalidDimensions("Alphabet length should be 26");
			
		}
		alphabet=alphabet.toUpperCase();
		
		Map<Character,Integer> ctr=new HashMap<Character, Integer>();
		
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
			if(eachValue!=2)
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
	/*
	 * input: input given by the user in the format (1-1,1,1-12) and alphabet
	 * This method splits the input and assigns the values to various parameters
	 * such as, sixes position, twenties position, fast switch,middle switch, slow switch
	 * Finally calls the constructor
	 */
	public static PurpleMachine constructPurpleMachine(String input,String alphabet) throws PurpleExceptionInvalidDimensions
	{
		
		
		String[] splitInputs = null;
		try
		{
			splitInputs=input.split("-");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String sixes=splitInputs[0];
		String[] twenties=splitInputs[1].split(",");
		String speed=splitInputs[2];
		
		if(twenties.length !=3)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid twenties");
		}
		
		int[] switchPosition=new int[4];
		switchPosition[0]=Integer.parseInt(sixes)-1;
		switchPosition[1]=Integer.parseInt(twenties[0])-1;
		switchPosition[2]=Integer.parseInt(twenties[1])-1;
		switchPosition[3]=Integer.parseInt(twenties[2])-1;
		
		
		if(speed.length() !=2)
		{
			throw new PurpleExceptionInvalidDimensions("Invalid switch speed");
		}
		
		int fastSwitch=Integer.parseInt(speed.charAt(0)+"");
		int middleSwitch=Integer.parseInt(speed.charAt(1)+"");
		PurpleMachine createMachine=null;
		try {
			 createMachine=new PurpleMachine(switchPosition, fastSwitch, middleSwitch, alphabet);
		} catch (PurpleExceptionSwitchLength e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createMachine;

	}
	/* 
	 * This method performs the decryption for the given cipher text
	 * If the character is vowel it is redirected to the sixes switch
	 * If the character is consonant it is redirected to the twenties switch
	 * After each character is decrypted the switch is stepped
	 */
	public String purpleDecrypt(String ciphertext) throws PurpleExceptionInvalidDimensions
	{
		
		StringBuilder plaintext=new StringBuilder();
		for(int eachChar=0;eachChar<ciphertext.length();eachChar++)
		{
				//retains - as it is 
				if(ciphertext.charAt(eachChar) == '-')
				{
					plaintext.append('-');
					purpleStep();
					continue;
				}
				//retains space as it is 
				/*if(ciphertext.charAt(eachChar) == ' ')
				{
					plaintext.append(' ');
					purpleStep();
					continue;
				}*/
				if(!Character.isLetter(ciphertext.charAt(eachChar)) )
				{
					throw new PurpleExceptionInvalidDimensions("Invalid ciphertext");
				}
			
				else
				{
					int n=plugboardMap.get(ciphertext.charAt(eachChar));
					int x;
					if(n< 6)
					{
						x=sixes.purpleDecrypt(n);
					}
					else
					{
						n=n-6;
						x=twenties.get(0).purpleDecrypt(twenties.get(1).purpleDecrypt(twenties.get(2).purpleDecrypt(n)));
						x=x+6;
						
					}
					plaintext.append(this.alphabetLetters.charAt(x));
					purpleStep();
					//break;
				}
			
		}
		return plaintext.toString();
	}
	/* 
	 * This method performs the encryption for the given plain text
	 * If the character is vowel it is redirected to the sixes switch
	 * If the character is consonant it is redirected to the twenties switch
	 * After each character is encrypted the switch is stepped
	 */
	public String purpleEncrypt(String plaintext) throws PurpleExceptionInvalidDimensions
	{
		
		
		StringBuilder ciphertext=new StringBuilder();
		for(int eachChar=0;eachChar<plaintext.length();eachChar++)
		{
			//retains space as it is 
			if(plaintext.charAt(eachChar) == ' ')
			{
				//ciphertext.append(' ');
				//purpleStep();
				continue;
			}
				if(!Character.isLetter(plaintext.charAt(eachChar)) && eachChar !=' ')
				{
					throw new PurpleExceptionInvalidDimensions("Invalid ciphertext");
				}
				else
				{
					int n=plugboardMap.get(plaintext.charAt(eachChar));
					int x;
					if(n< 6)
					{
						x=sixes.purpleEncrypt(n);
					}
					else
					{
						n -=6;
						x=twenties.get(2).purpleEncrypt(twenties.get(1).purpleEncrypt(twenties.get(0).purpleEncrypt(n)));
						x=x+6;
						
					}
					ciphertext.append(this.alphabetLetters.charAt(x));
					
					//System.out.println("eachChar = " + plaintext.charAt(eachChar) +  " ciphertext = " + ciphertext);
					purpleStep();
					
				
			}
		}
		
		return ciphertext.toString();
	}
	/*
	 * This method steps the switches
	 * If the switch is sixes it is stepped everytime a new character is encountered
	 * In case of twenties switch the fast switch steps everytime a new character is encountered
	 * whereas middle switch and slow switch step depending on switch positions of other switches
	 */
	public void purpleStep()
	{
		int positionSix=this.sixes.getSwitchPosition();
		
		int positionMiddle=middleSwitch.getSwitchPosition();
		
		sixes.purpleStepping();
		
		if(positionSix==23 && positionMiddle==24)
		{
			slowSwitch.purpleStepping();
		}
		else if(positionSix==24)
		{
			middleSwitch.purpleStepping();
		}
		else
		{
			fastSwitch.purpleStepping();
		}
	}
	

}
