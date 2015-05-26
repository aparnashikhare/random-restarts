package crypto.analyzer;

import java.util.Random;

public class Mutation {
	
	public static String returnMutatedSwitch(String childSwitch)
	{
		//[0-25]-[0-25],[0-25],[0-25]-[1-3][1-3][1-3]
		//System.out.println("ChildSwitch:"+childSwitch);
		String mutatedSwitch="";
		String swString[]=SelectionAndCrossover.getSwitches(childSwitch);
		String sixes=String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(swString[0]))));
		String twenties1=String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(swString[1]))));
		String twenties2=String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(swString[2]))));
		String twenties3=String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(swString[3]))));
		
		Random random=new Random();
		int randValue=random.nextInt(4);
	
		String valuefromFun="";
		switch (randValue) {
		case 0:valuefromFun=mutate(sixes.toCharArray()); 
				//return mutated value for example 00110 -> random value 2 then value at index 2 i.e 1 will be replaced by 0 and
				//it will be 00010
		
				while(Integer.parseInt(valuefromFun, 2) >25 || Integer.parseInt(valuefromFun,2) ==0)
				{
					valuefromFun=mutate(sixes.toCharArray());
				}
		break;
		case 1:valuefromFun=mutate(twenties1.toCharArray() );
			while(Integer.parseInt(valuefromFun, 2) >25 || Integer.parseInt(valuefromFun,2) ==0)
			{
				valuefromFun=mutate(twenties1.toCharArray());
			}
		break;
		case 2:valuefromFun=mutate(twenties2.toCharArray());
			while(Integer.parseInt(valuefromFun, 2) >25 || Integer.parseInt(valuefromFun,2) ==0)
			{
				valuefromFun=mutate(twenties2.toCharArray());
			}
		break;
		case 3:valuefromFun=mutate(twenties3.toCharArray() );
			while(Integer.parseInt(valuefromFun, 2) >25 || Integer.parseInt(valuefromFun,2) ==0)
			{
				valuefromFun=mutate(twenties3.toCharArray());
			}
		break;

		default:
			break;
		}
		//System.out.println("Mutated Switch:"+valuefromFun);
		String switchFromBinary=Integer.parseInt(valuefromFun,2)+"";
		//System.out.println("Text Switch:"+switchFromBinary);
		switch (randValue) {
		case 0:mutatedSwitch=switchFromBinary+"-"+swString[1]+","+swString[2]+","+swString[3]+"-"+swString[4];
		break;
		case 1:mutatedSwitch=swString[0]+"-"+switchFromBinary+","+swString[2]+","+swString[3]+"-"+swString[4];
		break;
		case 2:mutatedSwitch=swString[0]+"-"+swString[1]+","+switchFromBinary+","+swString[3]+"-"+swString[4];
		break;
		case 3:mutatedSwitch=swString[0]+"-"+swString[1]+","+swString[2]+","+switchFromBinary+"-"+swString[4];
		break;

		default:
			break;
		}
		//System.out.println("mumatedSwitch:"+mutatedSwitch);
		
		return mutatedSwitch;
		
	}
	public static String mutate(char[] value)
	{
		Random random=new Random();
		int randomBit=random.nextInt(5); //selects randomly 0 to 4
		
		value[randomBit]=value[randomBit]=='0' ?'1':'0';
		return String.valueOf(value);
		
	}
	
}
