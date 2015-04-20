package crypto.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class FitnessScore {
	public static float getScore(String actualPlaintext,String cipherText)
	{
		
		
		//String actualPlaintext="INPOSTMEANSHOTYETHEREOUTHERCHILDSIRHISLIVEDDESIGNATUNEASYMESEASONOFBRANCHONPRAISEESTEEMABILITIESDISCOURSEBELIEVINGCONSISTEDREMAININGTONOMISTAKENNOMEDENOTINGDASHWOODASSCREENEDWHENCEORESTEEMEASILYHEONDISSUADEHUSBANDSATOFNOIFDISPOSAL";
		String actualBinaryPlaintext=getBinaryString(actualPlaintext);
		String binaryPutativePlaintext=getBinaryString(cipherText);
		
        float score=getHammingDistance(binaryPutativePlaintext,actualBinaryPlaintext);
       return score;
	}
	
	public static float getHammingDistance(String binaryPutativePlaintext,String actualBinaryPlaintext)
	{
		int distance=0;
		StringBuffer s=new StringBuffer();
		for(int i=0;i<binaryPutativePlaintext.length();i++)
		{
			char a=binaryPutativePlaintext.charAt(i);
			char b=actualBinaryPlaintext.charAt(i);
			int first=a - '0';
			int second=b - '0';
			if(first==second)
			{
				
				s.append(0);
			}
			else
			{
				
				s.append(1);
			}
		}
		String finalString=s.toString();
		/*System.out.println("len:"+binaryPutativePlaintext.length());
		System.out.println(finalString);
		System.out.println(finalString.length());*/
		
		for(int j=0;j<finalString.length();j++)
		{
			distance+=Integer.parseInt(finalString.charAt(j)+"");
		}
		
		return ((distance*1.0f)/finalString.length())*100;
	}
	
	public static String getBinaryString(String text)
	{
		byte[] b =null;
		String binaryString="";
		for (int a = 0; a < text.length(); a++) {
            b= new byte[1024];
            b = text.getBytes();
           
        }
        for(int i=0; i<b.length;i++){
            
        	binaryString+=Integer.toBinaryString(0x100 + b[i]).substring(1);
            
        }
        /*System.out.println(binaryString);
        System.out.println("");*/
        return binaryString;
	}
	/*public static void main(String[] args) {
		

		try {
			String entireFileText = new Scanner(new File("/Users/vishwa/Documents/workspace/Tutorials/src/GeneticAlgo/putative3.txt")).useDelimiter("\\n").next();
			//System.out.println("Entire:"+entireFileText);
		
			float score=getScore(entireFileText);
			System.out.println("Score ::"+score+" %");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}*/
}
