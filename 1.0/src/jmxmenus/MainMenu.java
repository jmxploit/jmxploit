package jmxmenus;
import java.util.ArrayList;
import jmxattacks.*;
import jmxutils.*;

/*
 * This class is used to display the first application menu
 * and propose differents attack to users
 * 
 *
 * */

public class MainMenu {
	
	private static String version = "1.0";
	public static int nbr = 0;
	
	public static void displayBanner()
	{
		System.out.println("     ____.  _____  ____  ___      .__         .__  __   ");
		System.out.println("    |    | /     \\ \\   \\/  /_____ |  |   ____ |__|/  |_ ");
		System.out.println("    |    |/  \\ /  \\ \\     /\\____ \\|  |  /  _ \\|  \\   __\\");
		System.out.println("/\\__|    /    Y    \\/     \\|  |_> >  |_(  <_> )  ||  |  ");
		System.out.println("\\________\\____|__  /___/\\  \\   __/|____/\\____/|__||__|  ");
		System.out.println("                 \\/      \\_/__|                         ");
		System.out.println("Version : " + version);
		System.out.println("");
		
		
		
	}
	
	public void displayannounceChoice()
	{
		System.out.println("Please select a module to launch\n");
	}
	
	public void displayChoice(String position,String name, String description)
	{
	
		System.out.println("["+position+"]\t" + name +"\t"+ description +"\t" );
	}
	
	public void chooseModule()
	{
		boolean choice = false;
		Modules module = new Modules();
		ArrayList<String> modules = module.getList();
		Class<?> cl = null;
		int value = 0;
		while (choice == false)
		{
			// Display Menu
			displayannounceChoice();
			
			for (int i = 0;i<modules.size();i++)
			{	
				try {
					
					cl = Class.forName(modules.get(i));
					Attack myattack=(Attack) cl.newInstance();
					displayChoice(String.valueOf(i) ,myattack.getAttack_name(), myattack.getAttack_description());
					
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
				catch (InstantiationException e) {
					
					e.printStackTrace();
				} catch (IllegalAccessException e) {
				
					e.printStackTrace();
				}
			}
			//READ choice
			value = jmxutils.Read.readInt(modules.size());
			if ((value != -1))
			{
				choice = true;
			}
		}
		//Launch the module
		try {
			cl = Class.forName(modules.get(value));
			Attack myattack=(Attack) cl.newInstance();
			myattack.prepareExploit();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
	}

	public MainMenu()
	{
		
		chooseModule();
		System.exit(0);
	}
}
