package crypto.analyzer;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import crypto.purple.PurpleExceptionInvalidDimensions;
import crypto.purple.PurpleMachine;


public class GeneticAnalyzer {
	
	private static final String propertiesFile = "/Users/other/sjsu/random-restarts/conf/geneticanalyzer.properties";
	private static final String inputFile = "/Users/other/sjsu/random-restarts/conf/random-restarts.input";
	private static int fitnessScoreFileCount;
	private static int newPopulationFileCount;
	//private static int fitnessScoreFileCount=0;
	//private static int newPopulationFileCount=0;
	private static Properties properties;
	
	private int switchesPerIteration;
	private int iterations;
	private String outputDir;
	
	GeneticAnalyzer(int iterations, int switchesPerIteration) {
		fitnessScoreFileCount=0;
		newPopulationFileCount=0;
		this.iterations = iterations;
		this.switchesPerIteration = switchesPerIteration;
	}
	
	private void initProperties() throws Exception {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(propertiesFile));
		} catch (Exception e) {
			System.err.println("Exception reading properties e = " + e);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void analyze() throws Exception {
		initProperties();
		cleanAndSetup();
		findFirstPopulationScores();
		generateNewPopulations();
	}
	
	private void cleanAndSetup() {
		this.outputDir = (String) this.properties.get("genetic.analyzer.output.directory");
		System.out.println(this.outputDir);
		
		File outputDirF = new File(this.outputDir);
		
		System.out.println("File exists"+outputDirF.exists());
		if (outputDirF.exists()) {
			File[] files = outputDirF.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith("fitnessScore") || name.startsWith("newPopulation");
				}
			});
			
			for (File f : files) {
				boolean deleted = f.delete();

				if (!deleted)
					System.err.println("Couldn't delete file : " + f.getPath());
			}
		}
		
		if (!outputDirF.exists()) {
			outputDirF.mkdirs();
		}
	}
	
	private void generateNewPopulations() throws IOException, PurpleExceptionInvalidDimensions
	{
		for(int j=0;j<this.iterations;j++)
		{
			
			for(int i=0;i<this.switchesPerIteration;i++)
			{
				String childSwitch=getChildSwitch();
				String mutateSwitch=mutateSwitch(childSwitch);
				writeNewPopulation(mutateSwitch);
			}
			
			findNewPopulationScores();
			System.out.println("End of "+j);
		}
	}
	private void writeNewPopulation(String mutateSwitch) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File(this.outputDir + "/newPopulation"+newPopulationFileCount+".txt"),true));
		
		writer.append(mutateSwitch+"\n");
		writer.close();
	}

	private String mutateSwitch(String childSwitch) {
		// TODO Auto-generated method stub
		return Mutation.returnMutatedSwitch(childSwitch);
	}

	private void findFirstPopulationScores() throws Exception {
		String originalPlainText =properties.getProperty("genetic.analyzer.plaintext");
		String originalCipherText = properties.getProperty("genetic.analyzer.cipher");
		
		generateFirstPopulationSwitches();
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File(this.outputDir + "/fitnessScore"+fitnessScoreFileCount+".properties")));
		
		BufferedReader reader=new BufferedReader(new FileReader(new File(this.outputDir + "/newPopulation"+newPopulationFileCount+".txt")));
		int count=0;
		String switchFromFile="";
		while((switchFromFile=reader.readLine()) !=null)
		{
		
			PurpleMachine decryptPM = PurpleMachine.constructPurpleMachine(switchFromFile, PurpleMachine.plugboard);
			String putativePlaintext=decryptPM.purpleDecrypt(originalCipherText);
			float score=FitnessScore.getScore(originalPlainText, putativePlaintext);
			writer.write(count+"="+switchFromFile+" "+score+"\n");
			count++;
		}
		newPopulationFileCount++;
		writer.close();
		reader.close();
	}
	private void generateFirstPopulationSwitches() throws IOException {
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File(this.outputDir + "/newPopulation"+newPopulationFileCount+".txt"),true));
		for(int i=0;i<this.switchesPerIteration;i++)
		{
			String randomSwitch=generateRandomSwitch();
			writer.append(randomSwitch+"\n");
			
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
		
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File(this.outputDir + "/fitnessScore"+fitnessScoreFileCount+".properties")));
		
		BufferedReader reader=new BufferedReader(new FileReader(new File(this.outputDir + "/newPopulation"+newPopulationFileCount+".txt")));
		String switchFromFile="";
		int switchNumber=0;
		while((switchFromFile=reader.readLine()) !=null)
		{
			//System.out.println("switchFromFile:"+switchFromFile);
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
		List<String> parents=SelectionAndCrossover.fairRandomSelection(this.outputDir + "/fitnessScore"+fitnessScoreFileCount+".properties");
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
		
		if (a == null || a.length < 2) {
			
			File inf = new File(inputFile);
			System.out.println("inputFile exists"+inf.exists());
			if (inf.exists()) {
				System.out.println("Inside file exists");
				List<String> lines = getFileContents(inf);
				boolean isHeaderPresent=false;
				for (String line : lines) {
					System.out.println("Lines:"+line);
					if (line.indexOf(",") > 0) {
						String iterKv = line.split(",")[0];
						String switchKv = line.split(",")[1];
						
						if (iterKv.indexOf("=") > 0 && switchKv.indexOf("=") > 0) {
							String iter = iterKv.split("=")[1];
							String switches = switchKv.split("=")[1];
							
							try {
								
								System.out.println("Starting analyzer with " + iter 
										+ " iterations with " + switches + " switches");
								
								GeneticAnalyzer analyzer = new GeneticAnalyzer(Integer.parseInt(iter), Integer.parseInt(switches));
								analyzer.analyze();
								ResultReport result=new ResultReport(properties);
								result.writeResult(Integer.parseInt(switches), Integer.parseInt(iter), isHeaderPresent);
								isHeaderPresent=true;
							} catch (Exception e) {
								
								System.err.println("Exception analyzing iterations : " + iter + ", no-of-switches = " + switches);
								e.printStackTrace();
							}
						}
					}
				}
			}
			
		} else if (a.length == 2) {
			System.out.println(Integer.parseInt(a[0]));
			System.out.println( Integer.parseInt(a[1]));
			System.out.println("Coming in else");
			GeneticAnalyzer analyzer = new GeneticAnalyzer(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
			analyzer.analyze();
			
		}
	}

	private static List<String> getFileContents(File inf) throws Exception {
		FileInputStream fs = null;
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			fs = new FileInputStream(inputFile);
			reader = new BufferedReader(new InputStreamReader(fs));
			String line = null; 
			
			while( (line = reader.readLine()) != null ) {
				lines.add(line);
			}
			
		} finally {

			if (reader != null)
				reader.close();
			if (fs != null)
				fs.close();
		}
		
		return lines;
	}
}
