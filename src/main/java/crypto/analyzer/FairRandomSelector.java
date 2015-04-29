package crypto.analyzer;

import java.util.Arrays;
import java.util.Random;

public class FairRandomSelector
{
    private double[] fitnessList;
    private int bestFit;
    private int[] slots;
    
    private Random random = new Random(System.currentTimeMillis());
    private int current = -1;
    
    public FairRandomSelector(double[] fitnessList)
    {
        this.fitnessList = fitnessList;
        slots = new int[fitnessList.length * 10];
        allocate();
    }
    
    public void allocate()
    {
        double totalFitness = 0.0;
        double best = 0.0;
        
        int i=0;
        for (double fitness : fitnessList) 
        {
            slots[i] = i;
            totalFitness += fitness;
            
            if (best < fitness) {
                best = fitness;
                bestFit = i;
            }
                
            i++;
        }
        
        int freeSpace = slots.length - fitnessList.length;
        int fitnessIndex = 0;
        for (double fitness : fitnessList) 
        {
            double percentage = fitness/totalFitness;
            // allocate the percentage of freespace to this 
            // item based on the fitness
            double allocatableSlots = Math.round (percentage * freeSpace);
            while (allocatableSlots > 0 && i < slots.length)
            {
                slots[i++] = fitnessIndex;
                allocatableSlots --;
            }
            
            fitnessIndex++;
        }
        
        while ( i < slots.length )
        {
            slots[i++] = bestFit;
        }
    }
    
    public int getNextRandom()
    {
        int r = current;
        while (r == current)
            r = random.nextInt (slots.length);
        
        current = r;
        return slots[r];
    }
    
    private int[] slots()
    {
        return slots;
    }
    
    public static void main (String ...a)
    {
        double[] fitnessList = {1,2,5,4,9,6};
        FairRandomSelector selector = new FairRandomSelector(fitnessList);
        System.out.print ("slots = " );
        for (int i : selector.slots())
            System.out.print(i +",");
            
        System.out.println ("");
    }
}
