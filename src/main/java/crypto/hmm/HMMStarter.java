package crypto.hmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	int startSwitch;
	int T;
	String plaintext;
	public String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	int N=26;
	int M=26;
	double[][][] threeA;
	double EPSILON =0.00001;
	int startSwitchForPurple=2;
	boolean debug=false;
	
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
	public String encryptObservations() throws PurpleExceptionInvalidDimensions
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
		}
		return randomKey;
	}
	public void initMatrices(HMMData data,int seed)
	{
		Random random=new Random(seed);
		
		//initialize pi
		double prob=1.0/(double)N;
		double ftemp=prob/10.0;
		double ftemp2=0.0;
		double[] pi=new double[N];
		
		for(int i = 0; i < N; ++i)
		{
			if((random.nextInt() & 0x1) == 0)
		    {
		        pi[i] = prob + (double)(random.nextInt() & 0x7) / 8.0 * ftemp;
		    }
		    else
		    {
		        pi[i] = prob - (double)(random.nextInt()  & 0x7) / 8.0 * ftemp;
		    }
		    ftemp2 += pi[i];
		        
		}// next i
		for(int i = 0; i < N; ++i)
	    {
	        pi[i] /= ftemp2;
	    }
		data.setPi(pi);
		//initialize B[][]
		
		double[][] B=new double[N][M];
		prob = 1.0 / (double)M;
	    ftemp = prob / 10.0;
	    for(int i = 0; i < N; ++i)
	    {
	        ftemp2 = 0.0;
	        for(int j = 0; j < M; ++j)
	        {
	            if((random.nextInt() & 0x1) == 0)
	            {
	                B[i][j] = prob + (double)(random.nextInt() & 0x7) / 8.0 * ftemp;
	            }
	            else
	            {
	                B[i][j] = prob - (double)(random.nextInt() & 0x7) / 8.0 * ftemp;
	            }
	            ftemp2 += B[i][j];
	            
	        }// next j
	        
	        for(int j = 0; j < M; ++j)
	        {
	            B[i][j] /= ftemp2;
	        }
	        
	    }// next i
	    data.setB(B);
		
		
	}
	public int getAMatrixRow(int startswitch)
	{
		startswitch=(startswitch+1)%25;
		return startswitch;
	}
	public void alphaPass(HMMData data,int startSwitch)
	{
		double ftemp=0.0;
		double[] pi=data.getPi();
		double[][] B=data.getB();
		
		//get the step object at zero position
		Step step0=steps.get(0);
		
		double[] alpha=new double[N];
		
		for(int i=0;i<N;++i)
		{
			 alpha[i] = pi[i] * B[i][step0.getObs()];
		     ftemp += alpha[i];
		}
		step0.setC(1.0/ftemp);
		
		 // scale alpha[0]'s
	    for(int i = 0; i < N; ++i)
	    {
	        alpha[i] /= ftemp;
	    }
	    
	    step0.setAlpha(alpha);
	 // alpha pass
	    for(int t = 1; t < T; ++t)
	    {
	        ftemp = 0.0;
	        int row=getAMatrixRow(startSwitch);
	        double[][] A = threeA[row];
	        double[] alphas=new double[N];
	        for(int i = 0; i < N; ++i)
	        {
	            alphas[i] = 0.0;
	            double[] prevAlpha=steps.get(t-1).getAlpha();
	            for(int j = 0; j < N; ++j)
	            {
	                alphas[i] += prevAlpha[j] * A[j][i];
	            }
	            alphas[i] *= B[i][steps.get(t).getObs()];
	            ftemp += alphas[i];
	        }
	      
	        
	        steps.get(t).setC(1.0/ftemp);
	        
	        // scale alpha's
	        for(int i = 0; i < N; ++i)
	        {
	            alphas[i] /= ftemp;
	        }
	        steps.get(t).setAlpha(alphas);
	    
	    }// next t
	}
	public void betaPass(HMMData data,int startSwitch)
	{
		
		double[][] B=data.getB();
		  // compute scaled beta[T - 1]'s
		double[] beta=new double[N];
	    for(int i = 0; i < N; ++i)
	    {
	        beta[i] = 1.0 * steps.get(T-1).getC();
	    }
	    steps.get(T-1).setBeta(beta);
	    
	    // beta pass
	    for(int t = T - 2; t >= 0; --t)
	    {
	    	int row=getAMatrixRow(startSwitch);
	    	double[][] A = threeA[row];
	    	double[] betas=new double[N];
	        for(int i = 0; i < N; ++i)
	        {
	            betas[i] = 0.0;
	            double[] prevBeta=steps.get(t+1).getBeta();
	            for(int j = 0; j < N; ++j)
	            {
	                betas[i] += A[i][j] * B[j][steps.get(t+1).getObs()] * prevBeta[j];
	            }
	            
	            // scale beta's (same scale factor as alpha's)
	            betas[i]*=steps.get(t).getC();
	           
	        }
	        steps.get(t).setBeta(betas);

	    }// next t
	}
	
	public void computeGammas(HMMData data,int startSwitch)
	{
		double ftemp;
		double ftemp2;
		double denom;
		double[][] B=data.getB();
		// compute gamma's and diGamma's
	    for(int t = 0; t < T - 1; ++t)
	    {
	        denom = 0.0;
	        double[] alpha=steps.get(t).getAlpha();
	        double[] betaCurrentStep=steps.get(t).getBeta();
	        Step nextStep=steps.get(t+1);
	        double[] beta=nextStep.getBeta();
	        int row=getAMatrixRow(startSwitch);
	        double[][] A = threeA[row];
	        
	        for(int i = 0; i < N; ++i)
	        {
	            
	        	for(int j = 0; j < N; ++j)
	            {
	                denom += alpha[i] * A[i][j] * B[j][nextStep.getObs()] * beta[j];
	            }
	        }
	        ftemp2 = 0.0;
	        double[] gamma=new double[N];
	        double[][] diGamma=new double[N][N];
	        for(int i = 0; i < N; ++i)
	        {
	            gamma[i] = 0.0;
	            for(int j = 0; j < N; ++j)
	            {
	                diGamma[i][j] = (alpha[i] * A[i][j] * B[j][nextStep.getObs()] * beta[j])
	                                        / denom;
	                gamma[i] += diGamma[i][j];
	            }

	
	            // verify that gamma[i] == alpha[i]*beta[i] / sum(alpha[j]*beta[j])
	            ftemp2 += gamma[i];
	            ftemp = 0.0;
	            for(int j = 0; j < N; ++j)
	            {
	                ftemp += alpha[j] * betaCurrentStep[j];
	            }
	            ftemp = (alpha[i] * betaCurrentStep[i]) / ftemp;
	            if(DABS(ftemp - gamma[i]) > EPSILON)
	            {
	                System.out.println("gamma["+i+"] = "+gamma[i]+" "+ftemp );
	                System.out.println("********** Error !!!\n");
	            }
	

	        }// next i
	        steps.get(t).setGamma(gamma);
	        steps.get(t).setDiGamma(diGamma);
	        if(DABS(1.0 - ftemp2) > EPSILON)
	        {
	            System.out.println("sum of gamma's = "+ftemp2+" (should sum to 1.0)\n");
	        }
	    }
	}
	public void reEstimatePi(HMMData data)
	{
		double[] piBar=new double[N];
		double[] step0Gamma=steps.get(0).getGamma();
		 // reestimate pi[]        
	    for(int i = 0; i < N; ++i)
	    {
	        piBar[i] = step0Gamma[i];
	    }
	    data.setPiBar(piBar);  
	}
	public void reEstimateB(HMMData data)
	{
		double numer,denom;
        double[][] Bbar=new double[N][M];
		// reestimate B[][]
		for(int i = 0; i < N; ++i)
		{
			for(int j = 0; j < M; ++j)
			{
				numer = denom = 0.0;
				
				// t = 0,1,2,...,T-1
				for(int t = 0; t < T - 1; ++t)
				{
					Step currentStep=steps.get(t);
					double[] currentGamma=currentStep.getGamma();
					if(currentStep.getObs() == j)
					{
						numer += currentGamma[i];
					}
					denom += currentGamma[i];
	
				}// next t
	
				Bbar[i][j] = numer / denom;
	       
			}// next j
	       
		}// next i
       data.setBbar(Bbar);
	}
	public double DABS(double x)
	{
		return ((x) < (0.0) ? (-x) : (x));
	}
	public int[] computeKeyFromB(HMMData data)
	{
		int[] keyGeneratedFromB=new int[N];
		// The rows correspond to the observations.
		// The columns correspond to the inner states.
		// Get the highest probability that maps the observation symbol to the inner state
		double[][] B=data.getB();
		for (int i = 0; i < N; i++)
		{

			double maxProb = 0.0;
			int obsIndex = -1;

			for (int j = 0; j < M; j++)
			{
				if (B[j][i] > maxProb)
				{
					maxProb = B[j][i];
					obsIndex = j;
				}

			}

			// Ok, at this point we have the observation that most likely corresponds to the internal state.
			// Store this as an integer. The index 'i' corresponds to the observation. The 'obsIndex' corresponds to the internal state.
			keyGeneratedFromB[i] = obsIndex;
			//printf("%d   ",keyGeneratedFromB[i]);
		}
		return keyGeneratedFromB;
		//printf("\n");
	}
	public int[] computeEncryptionKey(String random)
	{
		// This will be used to substitute the plaintext symbol using the index.
		int[] encryptionKey=new int[N];
		
		char[] key=random.toCharArray();
		
		// Init the required encryption key
		for (int j = 0; j < M; j++)
		{
			encryptionKey[j] = -1;
		}
		

		// Find the matching alphabet from the encryption key and substitute the index from the plaintext symbol.
		for (int i = 0; i < M; i++)
		{
			
			encryptionKey[i]=hmap.get(key[i]);
			//printf("%d   ",encryptionKey[i]);
		}
		//printf("\n");
		return encryptionKey;
	}
	public double decryptObservationUsingComputedKeyAndComputeScore(int[] keyGeneratedFromB)
	{
		int numMatches = 0;

		String arrAlphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] charArray=arrAlphabet.toCharArray();
		int line = 50;

		if(debug)
		{
			System.out.println("\nEncrypted observations:\n");
	
			for (int i = 0; i < T; i++)
			{
				Step currentStep=steps.get(i);
	
				if (i % line == 0)
				{
					System.out.println("\n");
				}
				System.out.println(charArray[currentStep.getObs()]);
			}
		}
	

		if(debug){
			System.out.println("\n\nDecrypted observations:\n");
		}
	

		
		for (int i = 0; i < T; i++)
		{
			Step currentStep=steps.get(i);
			int originalObs = currentStep.getOriginalObs();
			int encryptdObs = currentStep.getObs();

			// Use 'keyGeneratedFromB' to decrypt the encryptedOservation.
			
			// Decrypt the observation using the key generated from the B matrix.
			int decryptedObs = keyGeneratedFromB[encryptdObs];

			// It means the decryption key for this character was correct
			if (originalObs == decryptedObs)
			{
				numMatches++;
			}

			if(debug){
				if (i % line == 0)
				{
					System.out.println("\n");
				}
	
				// Print the decrypted observations
				if (decryptedObs == -1)
				{
					System.out.println("?");
				}
				else
				{
					
					System.out.println(charArray[decryptedObs]);
				}
			}
		}

		if(debug)
		{
			System.out.println("\nOriginal observations:\n");
	
			for (int i = 0; i < T; i++)
			{
				Step currentStep=steps.get(i);
				if (i % line == 0)
				{
					System.out.println("\n");
				}
				System.out.println(charArray[currentStep.getOriginalObs()]);
			}
		}

		double score = (double)numMatches / (double)T;

		return score;
	}
	public void start() throws IOException, PurpleExceptionInvalidDimensions
	{
		this.plaintext=filterPlaintext();
		this.T=getT();
		getObservation();
		// Total scores will be stored in this variable, will be used to calculate normalized score
		double totalDataScore = 0.0;
		double totalKeyScore = 0.0;
		
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
			int[] randomSeedArray = new int[numRestarts];
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
			String randomKey=encryptObservations();
			int restartBeginIter=1;
			int restartEndIter=numRestarts;
			// These variables are required for storing the highest values ( Applicable for Random restarts )
			double highestSuccessRateBasedOnData = 0.0;
			long seedForHighestBasedOnData = 0;

			double highestSuccessRateBasedOnKey = 0.0;
			long seedForHighestBasedOnKey = 0;

			int[] keyFromBForHighestDataScore=new int[26];
			int[] keyFromBForHighestKeyScore=new int[26];
			
			while(restartBeginIter<=restartEndIter) //number of random restarts loop
			{
				System.out.println("\nTest case "+perm+1+" of "+numTestCases+" Restart "+restartBeginIter+" of "+restartEndIter);
				double logProb,newLogProb;
				int iter;
				HMMData data=new HMMData();
				int seed=randomSeedArray[restartBeginIter-1];
				
				initMatrices(data,seed);
				// initialization
				iter = 0;
				logProb = -1.0;
				newLogProb = 0.0;
				// main loop (This deals with the HMM re-estimation)
				while ((iter < maxIteartions) && (newLogProb > logProb))
				{
					
					int startSwitch=startSwitchForPurple-2;
					logProb=newLogProb;
					
					alphaPass(data,startSwitch);
					betaPass(data,startSwitch);
					computeGammas(data,startSwitch);
					reEstimatePi(data);
					reEstimateB(data);
					/**????****/
					// assign pi and B corresponding "bar" values
					double[] pi=new double[N];
					double[][] B=new double[N][M];
					double[] piBar=data.getPiBar();
					double[][] Bbar=data.getBbar();
					for (int i = 0; i < N; ++i)
					{
						pi[i] = piBar[i];
						
						for (int j = 0; j < M; ++j)
						{
							B[i][j] = Bbar[i][j];
						}

					}// next i
					data.setPi(pi);
					data.setB(B);

					// compute log [P(observations | lambda)], where lambda = (A,B,pi)
					newLogProb = 0.0;
					for (int i = 0; i < T; ++i)
					{
						Step currentStep=steps.get(i);
						newLogProb += Math.log(currentStep.getC());
					}
					newLogProb = -newLogProb;
					// a little trick so that no initial logProb is required
					if (iter == 0)
					{
						logProb = newLogProb - 1.0;
					}
					iter++;
					
				}//end of while HMM re-estimation
				if(debug)
				{
					System.out.println("T="+T+" N="+N+" M="+M+" iter="+iter);
					System.out.println("final pi\n"+Arrays.toString(data.getPi()));
					System.out.println("final B\n"+Arrays.toString(data.getB()));
					System.out.println("new log prob\n"+newLogProb);
				}
				int[] keyGeneratedFromB;
				
				// Computing decryption key
				// So, in this key the ith location would contain the equivalent decrypted symbol.
				// If the value is -1, then no information could be obtained from the B matrix for that specific symbol.
				keyGeneratedFromB=computeKeyFromB(data);
				
				int[] encryptionKey;
				encryptionKey=computeEncryptionKey(randomKey);
				
				// Computing score based on the key
				int keyMatch = 0;

				for (int loc = 0; loc < N; loc++)
				{
					if (keyGeneratedFromB[loc] != -1)
					{
						if (encryptionKey[keyGeneratedFromB[loc]] == loc)
						{
							keyMatch++;
						}
					}
				}

				double keyScore = (double)keyMatch / (double)N;

				System.out.println("Num Matched: "+keyMatch);
				
				// Decrypt text and score (based on data)
				double dataScore = 0;

				// Now, if KeyScore is 0 then do not attempt to compute the score based on the data.

				if (keyBasedScoreOnly == 0)	// If true, implies that Data Score is also required.
				{
					if (keyScore > 0) {

						dataScore = decryptObservationUsingComputedKeyAndComputeScore(keyGeneratedFromB);
					}
					else 
					{
						System.out.println("\nKey Score was 0. Data Score not computed.");
					}
				}
				System.out.println("\nSuccess Rate (Data):"+ dataScore);
				System.out.println("\nSuccess Rate (Key):"+ keyScore);
				System.out.println("\nSeed Used:%d"+ seed);
				System.out.println("\n------------Test Case:"+perm+1+" of "+numTestCases +" , End of Restart: "+restartBeginIter+" of "+restartEndIter+"  Data Size: "+ T);

				// Calculating test case specific scores (will be used later to calculate averages)

				for (int i = 0; i < sizeofScores; i++)

				{
					ScoreUnit scoreUnit=scoreUnits.get(i);
					double[] scores=scoreUnit.getScores();
					if(dataScore > scores[scoreUnit.getLoc()])
					{
						scores[scoreUnit.getLoc()] = dataScore;
					}
					scoreUnit.setScores(scores); //????? required ?
					scoreUnit.setCurrCount(scoreUnit.getCurrCount()+1);
					// Counter reached limit. Reset counter and increase location

					if (scoreUnit.getCurrCount() == scoreUnit.getMaxCount())
					{
						scoreUnit.setCurrCount(0);
						scoreUnit.setLoc(scoreUnit.getLoc()+1);

					}

				}
				// End
				
				// Set the highest values
				if (keyScore > highestSuccessRateBasedOnKey )
				{
					//printf("\nHigher score found !!!");
					highestSuccessRateBasedOnKey = keyScore;
					seedForHighestBasedOnKey = seed;

				
					for (int k = 0; k < 26; k++) {
						keyFromBForHighestKeyScore[k] = keyGeneratedFromB[k];
					}
				}

				if (dataScore > highestSuccessRateBasedOnData)
				{
					//printf("\nHigher score found !!!");
					highestSuccessRateBasedOnData = dataScore;
					seedForHighestBasedOnData = seed;

					for (int k = 0; k < 26; k++) {
						keyFromBForHighestDataScore[k] = keyGeneratedFromB[k];
					}
				}

				// Report iterative progress
				if (restartBeginIter % 50 == 0 || restartBeginIter == restartEndIter)
				{
					// Calculate temp averages
					double n_key = 0.0;
					double n_data = 0.0;

					if (perm > 0)
					{
						n_key = totalKeyScore / perm;
						n_data = totalDataScore / perm;
					}

					//fprintf(fpProgress, "%d\t%d\t%d\t%f\t%d\t%f\t%d\t%f\t%f\n", perm, RESTART_ITER, T, highestSuccessRateBasedOnKey, seedForHighestBasedOnKey, highestSuccessRateBasedOnData, seedForHighestBasedOnData, n_key, n_data);
					//fflush(fpProgress);
				}

				restartBeginIter++;
				// This will make sure that the next iteration is not executed

				if (highestSuccessRateBasedOnData >= 1 || highestSuccessRateBasedOnData >= 1.0)
				{
					// The best possible score was computed for this test case.
					System.out.println("Highest Score found. Breaking out of loop.");
					break;
				}
				
			}//end of while random restarts loop

			
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
