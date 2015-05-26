package crypto.analyzer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import crypto.purple.PurpleExceptionInvalidDimensions;
import crypto.purple.PurpleMachine;


public class GeneticAnalyzer {
	
	private static final String inputPropertiesFile = "/Users/vishwa/sjsu/CS297/random-restarts/conf/geneticanalyzer.properties";
	private static int fitnessScoreFileCount=0;
	//private static String fitnessscoreFile = "/Users/vishwa/Documents/workspace/Tutorials/src/trials/properties/fitnessScore"+fitnessScoreFileCount+".properties";
	private static int newPopulationFileCount=0;
	//private static String newPopulationFile="/Users/vishwa/Documents/workspace/Tutorials/src/trials/properties/newPopulation"+newPopulationFileCount+".txt";
	private static Properties properties;
	

	
	private void initProperties() throws Exception {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(inputPropertiesFile));
		} catch (Exception e) {
			System.err.println("Exception reading properties e = " + e);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void analyze() throws Exception {
		initProperties();
		findScores();
		generateNewPopulations();
		
	}

	private void generateNewPopulations() throws IOException, PurpleExceptionInvalidDimensions
	{
		for(int j=0;j<10;j++)
		{
			for(int i=0;i<15;i++)
			{
				String childSwitch=getChildSwitch();
				String mutateSwitch=mutateSwitch(childSwitch);
				writeNewPopulation(mutateSwitch);
			}
			
			findNewPopulationScores();
		}
	}
	private void writeNewPopulation(String mutateSwitch) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File("/Users/vishwa/sjsu/CS297/random-restarts/conf/newPopulation"+newPopulationFileCount+".txt"),true));
		
		writer.append(mutateSwitch+"\n");
		writer.close();
	}

	private String mutateSwitch(String childSwitch) {
		// TODO Auto-generated method stub
		return Mutation.returnMutatedSwitch(childSwitch);
	}

	private void findScores() throws Exception {
		String originalPlainText =properties.getProperty("genetic.analyzer.plaintext");
		String originalCipherText = properties.getProperty("genetic.analyzer.cipher");
		
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File("/Users/vishwa/sjsu/CS297/random-restarts/conf/fitnessScore"+fitnessScoreFileCount+".properties")));
		int count=0;
		for(int i=0;i<15;i++)
		{
			
			String randomSwitch=generateRandomSwitch();
			PurpleMachine decryptPM = PurpleMachine.constructPurpleMachine(randomSwitch, PurpleMachine.plugboard);
			String putativePlaintext=decryptPM.purpleDecrypt(originalCipherText);
			float score=FitnessScore.getScore(originalPlainText, putativePlaintext);
			writer.write(count+"="+randomSwitch+" "+score+"\n");
			count++;
		}
		writer.close();
	}
	/*
	 * This method reads the new population and calculates the fitness score for each of the switches .
	 * The fitness score along with the switch is written to fitnessScore++number.properties file
	 */
	private void findNewPopulationScores() throws IOException, PurpleExceptionInvalidDimensions
	{
		String originalPlainText =properties.getProperty("genetic.analyzer.plaintext");
		String originalCipherText = properties.getProperty("genetic.analyzer.cipher");
		//System.out.println(fitnessscoreFile);
		fitnessScoreFileCount++;
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File("/Users/vishwa/sjsu/CS297/random-restarts/conf/fitnessScore"+fitnessScoreFileCount+".properties")));
		
		BufferedReader reader=new BufferedReader(new FileReader(new File("/Users/vishwa/sjsu/CS297/random-restarts/conf/newPopulation"+newPopulationFileCount+".txt")));
		String switchFromFile="";
		int switchNumber=0;
		while((switchFromFile=reader.readLine()) !=null)
		{
			System.out.println("switchFromFile:"+switchFromFile);
			PurpleMachine decryptPM = PurpleMachine.constructPurpleMachine(switchFromFile, PurpleMachine.plugboard);
			String putativePlaintext=decryptPM.purpleDecrypt(originalCipherText);
			float score=FitnessScore.getScore(originalPlainText, putativePlaintext);
			writer.write(switchNumber+"="+switchFromFile+" "+score+"\n");
			switchNumber++;
		}
		newPopulationFileCount++;
		writer.close();
		reader.close();
		
		//System.out.println("fitnessScoreFileCount:"+fitnessScoreFileCount);
	}
	private String getChildSwitch() throws IOException
	{
		List<String> parents=SelectionAndCrossover.fairRandomSelection("/Users/vishwa/sjsu/CS297/random-restarts/conf/fitnessScore"+fitnessScoreFileCount+".properties");
		String childSwitch=SelectionAndCrossover.crossover(parents);
		return childSwitch;
	}
	private static String generateRandomSwitch()
	{
		Random randomNum=new Random();
		
		//generates 1-25 (both inclusive)
		int sixesSwitchPos=randomNum.nextInt(25)+1; //25=max-min i.e 26-1
		int twentiesOnePos=randomNum.nextInt(25)+1;
		int twentiesTwoPos=randomNum.nextInt(25)+1;
		int twentiesThreePos=randomNum.nextInt(25)+1;
		
		int fastSwitch=randomNum.nextInt(3)+1;
		

		int middleSwitch=randomNum.nextInt(3)+1;
		
		while(middleSwitch == fastSwitch)
		{
			middleSwitch=randomNum.nextInt(3)+1;
		}
	
		String randomSwitch=sixesSwitchPos+"-"+twentiesOnePos+","+twentiesTwoPos+","+twentiesThreePos+"-"+fastSwitch+middleSwitch;
		//System.out.println(randomSwitch);
		return randomSwitch;
	}
	


	public static void main(String ...a) throws Exception {
		
		GeneticAnalyzer analyzer = new GeneticAnalyzer();
		analyzer.analyze();
	}
}
