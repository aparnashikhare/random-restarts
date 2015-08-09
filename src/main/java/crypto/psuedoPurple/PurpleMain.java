package crypto.psuedoPurple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * This is the main class of purple machine which reads the plaintext from file,
 * encrypts it using purpleEncrypt method and decrypts the corresponding cipher text 
 * using the purpleDecrypt method
 * 
 */
public class PurpleMain {
	final static int initialSwitch=2;
	final static String defaultAlphabetLetters=PurpleMachine.plugboard;
	final static Map<Character,String> digitsMap=new HashMap<Character,String>();
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
	public String correctPlaintext(String input)
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
	public static void main(String[] args) {
		PurpleMain purpleMain=new PurpleMain();
		
		String inputFileName="/Users/vishwa/Documents/workspace/PseudoPurple/src/Purple/plaintext.txt";
		String cipherFileName="/Users/vishwa/Documents/workspace/PseudoPurple/src/Purple/ciphertext.txt";
		String decryptedFileName="/Users/vishwa/Documents/workspace/PseudoPurple/src/Purple/decryptedtext.txt";
		
		
		try {
			BufferedReader reader=new BufferedReader(new FileReader(new File(inputFileName)));
			BufferedWriter cipherWriter=new BufferedWriter(new FileWriter(new File(cipherFileName)));
			BufferedWriter decryptedWriter=new BufferedWriter(new FileWriter(new File(decryptedFileName)));
			String inputLine="";
			
			PurpleMachine encryptPM=PurpleMachine.constructPurpleMachine(initialSwitch, defaultAlphabetLetters);
			PurpleMachine decryptPM=PurpleMachine.constructPurpleMachine(initialSwitch, defaultAlphabetLetters);
			while((inputLine =reader.readLine()) !=null)
			{
				System.out.println(inputLine);
				String ciphertext=encryptPM.purpleEncrypt(purpleMain.correctPlaintext(inputLine));
				System.out.println("Ciphertext:"+ciphertext);
				cipherWriter.write(ciphertext);
				String decryptedPlaintext=decryptPM.purpleDecrypt(ciphertext);
				decryptedWriter.write(decryptedPlaintext);
				
				System.out.println("decryptedPlaintext:"+decryptedPlaintext);
			}
		} catch (PurpleExceptionInvalidDimensions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
