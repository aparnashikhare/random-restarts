package crypto.hmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HMMStarter {
	
	String fname;
	int maxIteartions;
	int numRestarts;
	int numTestCases;
	String outputFile;
	int keyBasedScoreOnly;
	int T;
	String plaintext;
	public String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public HashMap<Character,Integer> hmap=new HashMap<Character,Integer>();
	ArrayList<Step> steps=new ArrayList<Step>();
	
	
	public HMMStarter(String fname, int maxIteartions, int numRestarts,
			int numTestCases, String outputFile, int keyBasedScoreOnly) {
		this.fname = fname;
		this.maxIteartions = maxIteartions;
		this.numRestarts = numRestarts;
		this.numTestCases = numTestCases;
		this.outputFile = outputFile;
		this.keyBasedScoreOnly = keyBasedScoreOnly;
		
		for(int i=0;i<alphabet.length();i++)
		{
			hmap.put(alphabet.charAt(i),i);
		}
	}
	public String filterPlaintext() throws IOException
	{
		BufferedReader reader=new BufferedReader(new FileReader(new File(fname)));
		
		int value=0;
		StringBuffer buffer=new StringBuffer();
		while((value=reader.read()) !=-1)
		{
			char c=(char) value;
			if(c!=' ' && Character.isLetter(c))
			{
				buffer.append(c);
			}
			
		}
		return buffer.toString();
	}
	public int getT() throws IOException
	{
		return plaintext.length();
	}
	public void getObservation()
	{
		for(int i=0;i<plaintext.length();i++)
		{
			int obsValue=hmap.get(plaintext.charAt(i));
			Step step=new Step();
			step.setObs(obsValue);
			steps.add(step);
		}
	}
	
	public void start() throws IOException
	{
		this.plaintext=filterPlaintext();
		this.T=getT();
	}
	




	public static void main(String[] args) {
		
		if(args.length<6)
		{
			System.out.println("Please enter :");
			System.out.println("filename= fname");
			System.out.println("max iterations for HMM=maxIterations");
			System.out.println("number of random restarts=numRestarts");
			System.out.println("number of test cases=numTestCases");
			System.out.println("filename to store output scores=outputFile");
			System.out.println("keybasedScore only=1 else 0");
			
			return;
		}
		String fname=args[0];
		int maxIterations=Integer.parseInt(args[1]);
		int numRestarts=Integer.parseInt(args[2]);
		int numTestCases=Integer.parseInt(args[3]);
		String outputFile=args[4];
		int keyBasedScoreOnly=Integer.parseInt(args[5]);
		
		
		
		if(numRestarts <=0 || maxIterations<=0 || numTestCases<=0)
		{
			return;
		}
		if(keyBasedScoreOnly!=1 && keyBasedScoreOnly !=0)
		{
			return;
		}
		
	}
}
