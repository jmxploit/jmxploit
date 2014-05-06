package jmxutils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*************************
 * 
 * Class used to read user choice in main menu
 * 
 * *************************/

public class Read {

	public static int readInt(int size)
	{
		System.out.println("\nSelect ID : ");
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();
		    if ((Integer.parseInt(s) >= 0) && (Integer.parseInt(s) < size))
		    {
		    	return  Integer.parseInt(s);
		    }
		    else
		    {
		    	System.out.println("ERROR select an integer corresponding to one attack\n");
		    }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			
			System.out.println("ERROR select an integer corresponding to one attack\n");
		}
		return (-1);
	
	}
	
	
}
