package crypto.analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;


public class ResultReport {
	
    Properties properties;
	private static final String format = "%20s | %20s | %20s | %20s\n";
	private static final String bar = "     ----------------+----------------------+----------------------+---------------------\n";
	
	ResultReport(Properties properties) {
		this.properties = properties;
	}
	
	public void writeResult(int populationSize,int restarts,boolean isHeaderPresent) throws IOException {
		String resultFile=(String) properties.get("genetic.analyzer.result.directory");
		BufferedWriter writer=new BufferedWriter(new FileWriter(new File(resultFile+"/result.txt"),true));
		
		if(!isHeaderPresent)
		{
			writer
			.append(bar)
			.append(String.format(format, "Population Size", "Random Restart", "Score", "Switch"))
			.append(bar);
		}
		
		Entry<Float,String> scoreDetails = findOverallHighestScore();
		writer.append(String.format(format, populationSize,restarts,scoreDetails.getKey(),scoreDetails.getValue()));
		writer.close();
	}
	
	public Entry<Float, String> findHighestScoreOfEachPopulation(File filename) throws IOException
	{
		TreeMap<Float, String> treemap=new TreeMap<Float, String>();
		BufferedReader reader=new BufferedReader(new FileReader(filename));
		String line="";
		while((line =reader.readLine())!= null)
		{
			System.out.println("Line:"+line);
			String[] switchDetails=line.split("=");
			String secondPart=switchDetails[1];
			String[] values=secondPart.split(" ");
			//get the switch and its score
			
			treemap.put(Float.parseFloat(values[1]), values[0]);
		}
		reader.close();
		return treemap.lastEntry();
		
	}
	
	public Entry<Float,String> findOverallHighestScore() throws IOException
	{
		String outputDir = (String) properties.get("genetic.analyzer.output.directory");
		System.out.println(outputDir);
		TreeMap<Float, String> treemap=new TreeMap<Float, String>();
		File[] files=null;
		File outputDirF = new File(outputDir);
		if (outputDirF.exists()) {
			files=outputDirF.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith("fitnessScore") ;
				}
			});
		}
			
			for(File f:files)
			{
				Entry<Float,String> scoreDetails=findHighestScoreOfEachPopulation(f);
				treemap.put(scoreDetails.getKey(), scoreDetails.getValue());
				
			}
			
		return treemap.lastEntry();
	}
}
