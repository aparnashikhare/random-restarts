package crypto.purple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import crypto.purple.PurpleMachine;
 /*
  * This is the main class of purple machine which reads the plaintext from file,
  * encrypts it using purpleEncrypt method and decrypts the corresponding cipher text 
  * using the purpleDecrypt method
  * 
  */
public class PurpleMain {

	final static String defaultSwitches="12-6,3,10-31";
	final static String defaultAlphabetLetters=PurpleMachine.plugboard;
	
	final static Map<Character,String> digitsMap=new HashMap<Character, String>();
	/*
	 * This constructor maps the digit to respective words of the digits
	 */
	public PurpleMain()
	{
		digitsMap.put('0', "ZERO");
		digitsMap.put('1', "ONE");
		digitsMap.put('2', "TWO");
		digitsMap.put('3', "THREE");
		digitsMap.put('4', "FOUR");
		digitsMap.put('5', "FIVE");
		digitsMap.put('6', "SIX");
		digitsMap.put('7', "SEVEN");
		digitsMap.put('8', "EIGHT");
		digitsMap.put('9', "NINE");
		
	}
	/*
	 * This method corrects the plain text
	 * If a digit is encountered in the string it is replaced by the word
	 * If a space is encountered it is retained
	 */
	@SuppressWarnings("deprecation")
	public  String correctPlainText(String input)
	{
		
	     StringBuilder correctedInput=new StringBuilder();                   
	     String[] lines=input.split("\n");
	     for(int eachLine=0;eachLine<lines.length;eachLine++)
	     {
	    	 String line=lines[eachLine];
	    	 line=line.toUpperCase();
	    	 for(int i=0;i<line.length();i++)
	    	 {
	    		 if(Character.isLetter(line.charAt(i)))
	    		 {
	    			correctedInput.append(line.charAt(i));
	    		 }
	    		 else if(Character.isDigit(line.charAt(i)))
	    		 {
	    			 correctedInput.append(digitsMap.get(line.charAt(i)));
	    		 }
	    		 else if(Character.isSpace(line.charAt(i)))
	    		 {
	    			 correctedInput.append(' ');
	    		 }
	    	 }
	     }
	     return correctedInput.toString();
	}
	public String formatData(String result,int groupingNumber)
	{
		StringBuilder output=new StringBuilder();
		
		for(int i=0;i<result.length();i++)
		{
			output.append(result.charAt(i));
			if(i%5 ==0)
			{
				output.append(' ');
			}
		}
		return output.toString();
	}
	
	public static void main(String[] args) {
		PurpleMain purpleMain=new PurpleMain();
		
		Scanner scanner=null;
		BufferedWriter cipherwriter=null;
		BufferedWriter plaintextwriter=null;
		
		try {
			//name of the input plain text file
			
			String inputFileName="/Users/other/sjsu/random-restarts/encdec/plaintext.txt";
			//name of the file where cipher text is written after encryption
			String cipherOutputFileName="/Users/other/sjsu/random-restarts/encdec/ciphertext.txt";
			
			//name of the file where decrypted text is written after decryption
			String plaintextOutputFileName="/Users/other/sjsu/random-restarts/encdec/decryptedplaintext.txt";
			scanner=new Scanner(new File(inputFileName));
			cipherwriter=new BufferedWriter(new FileWriter(cipherOutputFileName));
			plaintextwriter=new BufferedWriter(new FileWriter(plaintextOutputFileName));
			PurpleMachine encryptPM = PurpleMachine.constructPurpleMachine(defaultSwitches, defaultAlphabetLetters);
			PurpleMachine decryptPM = PurpleMachine.constructPurpleMachine(defaultSwitches, defaultAlphabetLetters);
			String inputLine="";
			while(scanner.hasNext())
			{
				inputLine=scanner.nextLine();
				inputLine = encryptPM.purpleEncrypt(purpleMain.correctPlainText(inputLine));
				cipherwriter.write(inputLine+"\n");
				inputLine=decryptPM.purpleDecrypt(inputLine);
				
				/*System.out.println("*******decrypted text***********");
				System.out.println(inputLine);*/
				plaintextwriter.write(inputLine+"\n");
			}
		} 
		catch (PurpleExceptionInvalidDimensions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(FileNotFoundException fnd)
		{
			fnd.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(scanner !=null)
			{
				scanner.close();
				try {
					cipherwriter.close();
					plaintextwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
}
