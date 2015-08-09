package crypto.hmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import crypto.psuedoPurple.PurpleExceptionInvalidDimensions;
import crypto.psuedoPurple.PurpleMachine;

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
		return buffer.toString().toUpperCase();
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
	public String generatePermuation()
	{
		Random random=new Random();
		String arrAlphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] charArray=arrAlphabet.toCharArray();
		for(int i=arrAlphabet.length()-1;i>0;i--)
		{
			int j=random.nextInt(i+1);//generates a random number from 0 to i (inclusive)
			char temp=charArray[i];
			charArray[i]=charArray[j];
			charArray[j]=temp;
			
		}
		return String.valueOf(charArray);
	}
	public void encryptObservations() throws PurpleExceptionInvalidDimensions
	{
		String randomKey=generatePermuation();
		PurpleMachine encryptPM=PurpleMachine.constructPurpleMachine(2, randomKey);
		String putativeCipherText=encryptPM.purpleEncrypt(plaintext);
		for(int i=0;i<this.T;i++)
		{
			Step step=steps.get(i);
			step.setOriginalObs(step.getObs());
			int obsValue=hmap.get(putativeCipherText.charAt(i));
			step.setObs(obsValue);
			//steps.add(step);//ask vishu if this is required
			steps.set(i, step);
		}
	}
	public void start() throws IOException, PurpleExceptionInvalidDimensions
	{
		this.plaintext=filterPlaintext();
		this.T=getT();
		getObservation();
		
		for(int perm=0;perm<numTestCases;perm++)
		{
			int maxRestarts=numRestarts;
			int factor=10;
			int sizeofScores=0;
			int temp=maxRestarts;
			while(temp>0)
			{
				sizeofScores++;
				temp /=factor;
			}
			ArrayList<ScoreUnit> scoreUnits=new ArrayList<ScoreUnit>();
			temp=maxRestarts;
			for(int i=0;i<sizeofScores;i++)
			{
				ScoreUnit score=new ScoreUnit();
				score.setLoc(0);
				score.setMaxCount(temp);
				score.setCurrCount(0);
				score.setSizeOfArray(maxRestarts/temp);
				score.setAvg(0.0);
				double[] scoresArray=new double[score.getSizeOfArray()];
				for(int j=0;j<score.getSizeOfArray();j++)
				{
					scoresArray[j]=0.0;
				}
				score.setScores(scoresArray);
				temp/=factor;
				scoreUnits.add(score);
			}
			int[] randomSeedArray = null;
			Random random=new Random();
			for(int rand=0;rand<numRestarts;rand++)
			{
				int num;
				boolean check;
				do
				{
					num=random.nextInt();
					check=true;
					
					for(int j=0;j<rand;j++)
					{
						if(num==randomSeedArray[j])
						{
							check=false;
							break;
						}
					}
					
				}while(!check);
				randomSeedArray[rand]=num;
			}//end of for rand< numRestarts
			encryptObservations();
		}//end of perm<numofTestCases loop
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
