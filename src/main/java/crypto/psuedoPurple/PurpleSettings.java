package crypto.psuedoPurple;

public class PurpleSettings {
	
	 final static int[][] variableSwitchPerm={
		{6, 19, 23, 14, 25, 1, 10, 4, 2, 26, 7, 21, 13, 9, 22 ,8, 16, 3, 18, 15, 11, 5, 12, 24, 20, 17},
        {21, 4, 5, 24, 16, 17, 14, 23, 1, 26, 20, 22, 15, 3, 8, 18, 11, 12, 13, 10, 19, 25, 2, 6, 9, 7},
        {17, 23, 1, 13, 21, 6, 26, 15, 11, 19, 22, 12, 16, 18, 10, 3, 7, 14, 8, 20, 24, 4, 9, 25, 2, 5},
        {25, 3, 14, 21, 20, 4, 23, 6, 16, 24, 8, 19, 22, 2, 12, 17, 9, 5, 1, 11, 10, 7, 26, 13, 15, 18},
        {21, 19, 6, 22, 8, 23, 20, 13, 5, 24, 18, 4, 10, 3, 16, 25, 15, 14, 12, 7, 26, 2, 17, 11, 1, 9},
        {24, 2, 11, 9, 21, 14, 22, 7, 19, 6, 23, 3, 18, 13, 12, 8, 10, 25, 15, 16, 26, 17, 20, 4, 5, 1},
        {16, 22, 7, 24, 6, 18, 9, 25, 10, 13, 1, 21, 17, 2, 5, 4, 26, 11, 19, 20, 14, 8, 23, 15, 3, 12},
        {1, 20, 7, 26, 16, 12, 14, 25, 23, 5, 18, 15, 10, 13, 6, 8, 3, 24, 4, 9, 11, 17, 22, 19, 2, 21},
        {24, 17, 9, 26, 11, 8, 20, 21, 18, 7, 14, 22, 25, 1, 16, 15, 5, 19, 2, 6, 12, 4, 23, 10, 13, 3},
        {12, 8, 22, 17, 9, 3, 25, 20, 4, 10, 14, 26, 21, 5, 7, 18, 2, 16, 23, 13, 6, 1, 19, 15, 24, 11},
        {20, 23, 1, 26, 16, 22, 11, 2, 17, 9, 4, 8, 15, 24, 10, 13, 3, 18, 14, 5, 25, 6, 7, 12, 21, 19},
        {5, 4, 26, 15, 2, 13, 25, 19, 21, 6, 23, 16, 12, 14, 8, 24, 7, 17, 10, 18, 3, 9, 22, 1, 11, 20},
        {15, 23, 17, 21, 10, 19, 26, 22, 16, 2, 11, 8, 9, 7, 3, 14, 18, 24, 13, 12, 1, 5, 20, 25, 6, 4},
        {11, 12, 7, 22, 3, 8, 15, 24, 16, 6, 4, 20, 21, 2, 5, 1, 9, 25, 19, 18, 10, 23, 14, 17, 26, 13},
        {26, 12, 16, 24, 2, 7, 21, 4, 25, 8, 15, 19, 5, 1, 11, 9, 20, 17, 6, 23, 14, 13, 22, 3, 18, 10},
        {8, 21, 15, 18, 23, 1, 12, 11, 22, 17, 26, 14, 20, 16, 13, 19, 9, 7, 24, 3, 4, 2, 5, 25, 10, 6},
        {7, 25, 3, 23, 5, 18, 17, 26, 24, 13, 19, 20, 21, 14, 11, 9, 10, 2, 6, 1, 15, 22, 12, 16, 4, 8},
        {10, 13, 21, 4, 14, 24, 18, 3, 2, 25, 17, 11, 19, 20, 22, 1, 6, 12, 23, 9, 7, 15, 26, 8, 5, 16},
        {13, 21, 7, 9, 23, 12, 20, 16, 22, 14, 10, 19, 6, 24, 1, 2, 11, 25, 4, 5, 3, 18, 26, 17, 8, 15},
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26},
        {9, 20, 12, 5, 10, 21, 22, 24, 23, 17, 1, 13, 7, 15, 4, 25, 3, 16, 8, 18, 11, 26, 19, 2, 14, 6},
        {18, 22, 15, 21, 2, 13, 1, 7, 10, 5, 19, 17, 6, 20, 9, 23, 11, 12, 24, 8, 25, 3, 4, 26, 16, 14},
        {16, 25, 26, 18, 21, 19, 23, 10, 22, 11, 20, 5, 9, 24, 1, 4, 12, 13, 7, 6, 17, 2, 14, 15, 3, 8},
        {5, 21, 8, 1, 15, 25, 19, 9, 12, 23, 2, 6, 3, 14, 17, 22, 4, 20, 16, 13, 18, 24, 10, 7, 11, 26},
        {14, 21, 10, 4, 26, 24, 22, 8, 9, 12, 3, 25, 23, 11, 17, 20, 19, 6, 15, 5, 2, 18, 16, 7, 1, 13}
        };
		static int[][] decryptSwitch=variableSwitchPerm;
	 	static int[][] encryptSwitch= new  int[variableSwitchPerm.length][26];
		//builds a decrypt data using the initial settings
		static
		{
			constructDecryptReverseTable();
			buildEncryptSwitchesTable();
		}
		
		public static void constructDecryptReverseTable()
		{
			for(int i=0;i<decryptSwitch.length;i++)
			{
				for(int j=0;j<decryptSwitch[i].length;j++)
				{
					decryptSwitch[i][j]=decryptSwitch[i][j]-1;
				}
			}
		}
		public static int[][] buildEncryptSwitchesTable()
		{
			for(int i=0;i<decryptSwitch.length;i++)
			{
				int[] level = decryptSwitch[i];
				int[] reciprocal=new int[level.length];
				for(int k=0;k<level.length;k++)
				{
					reciprocal[k]=getIndex(level,k);
				}
				encryptSwitch[i]=reciprocal;
			}
			System.out.println("Encrypt switch..");
			for(int i=0;i<encryptSwitch.length;i++)
			{
				if(i==0)
				{
					System.out.print("    ");
					for(int k=0;k<26;k++)
					{
						System.out.print (k +",");
					}
					System.out.println("");
					
				}
				System.out.print(i+":  ");
				for(int j=0;j<encryptSwitch[i].length;j++)
				{
					
					System.out.print (encryptSwitch[i][j] +",");
				}
				System.out.println("");
			}
			return encryptSwitch;
		}
		public static int getIndex(int[] level, int key) {
			for (int i = 0 ; i < level.length; i ++) {
				 if (level[i] == key)
					 return i;
			}
			
			return -1;
		}

}
