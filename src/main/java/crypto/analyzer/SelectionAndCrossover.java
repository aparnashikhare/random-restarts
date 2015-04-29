package crypto.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

public class SelectionAndCrossover {
	
    public static List<String> fairRandomSelection(String fitnessScoreFile) throws IOException
    {
        int i = 0;
        
        List<String> selectedChromosomes = new ArrayList<String>();
        Properties properties=new Properties();
        properties.load(new FileInputStream(fitnessScoreFile));
        
        double[] scores = new double[properties.size ()]; 
        for (Object k : properties.keySet ()) {
            scores[i++] = Double.parseDouble (getScore(properties,""+k));
        }
        
        FairRandomSelector selector = new FairRandomSelector (scores);
        selectedChromosomes.add (getSwitch(properties, "" + selector.getNextRandom ()));
        selectedChromosomes.add (getSwitch(properties, "" + selector.getNextRandom ()));
        
        return selectedChromosomes;
    }
    
    private static String getSwitch(Properties properties, String key)
    {
        String val = properties.getProperty (key);
        String[] items = val.split(" ");
        return items[0];
    }

    private static String getScore(Properties properties, String key)
    {
        String val = properties.getProperty (key);
        String[] items = val.split(" ");
        return items[1];
    }
    
	public static List<String> randomSelection(String fitnessScoreFile) throws IOException
	{
		BufferedReader reader=new BufferedReader(new FileReader(new File(fitnessScoreFile)));
		
		String line=null;
		int count=0;
		while((line = reader.readLine()) !=null)
		{
			count++;
			
		}
		
		List<String> selectedChromosomes=new ArrayList<String>();
		Properties properties=new Properties();
		properties.load(new FileInputStream(fitnessScoreFile));
		for(int i=0;i<2;i++)
		{
			Random rand=new Random();
			int random1=rand.nextInt(count);
			int random2=rand.nextInt(count);
			
			while(random2 ==random1)
			{
				random2=rand.nextInt(count);
			}
			//System.out.println("Rand1:"+random1);
			//System.out.println("Rand2:"+random2);
			
			
			String valueOne=properties.getProperty(random1+"");
			String valueTwo=properties.getProperty(random2+"");
			
			String[] parentOne=valueOne.split(" ");
			String[] parentTwo=valueTwo.split(" ");
			
			String switchOne=parentOne[0];
			String switchTwo=parentTwo[0];
			
			
			Float fitnessScoreOne=Float.parseFloat(parentOne[1]);
			Float fitnessScoreTwo=Float.parseFloat(parentTwo[1]);
			
			if(fitnessScoreOne < fitnessScoreTwo)
			{
				selectedChromosomes.add(switchOne);
				//System.out.println("SwitchOne:"+switchOne);
			}
			else
			{
				selectedChromosomes.add(switchTwo);
				//System.out.println("SwitchTwo:"+switchTwo);
			}
		}
	
		return selectedChromosomes;
	
	}
	
	public static String crossover(List<String> parents)
	{
		
		
		
		Iterator<String> iterator=parents.iterator();
		
		String switchOne="";
		String switchTwo="";
		if(iterator.hasNext())
			switchOne=iterator.next();
		
		if(iterator.hasNext())
			switchTwo=iterator.next();
			
		//System.out.println("Switch1::"+switchOne);
		//System.out.println("Switch2::"+switchTwo);
		String[] switchOneVal=getSwitches(switchOne);
		String[] switchTwoVal=getSwitches(switchTwo);
		
		
		String childSwitch=switchOneVal[0]+"-"+switchOneVal[1]+","+switchOneVal[2]+","+switchTwoVal[3]+"-"+switchTwoVal[4];
		//System.out.println(childSwitch);
		return childSwitch;
	}
	
	public static String[] getSwitches(String swt)
	{
		String[] retStrings=new String[5];
		//System.out.println("Switch in getSwitches:"+swt);
		String[] switchParts=swt.split("-");
		retStrings[0]=switchParts[0];
		String[] twenties=switchParts[1].split(",");
		retStrings[1]=twenties[0];
		retStrings[2]=twenties[1];
		retStrings[3]=twenties[2];
		retStrings[4]=switchParts[2];
		
		return retStrings;
		
	}
}
