import java.io.IOException;

import jmxutils.*;
import jmxattacks.Attack;
import jmxattacks.Bruteforce;
import jmxinfos.*;
import jmxmenus.*;

public class jmxploit {

	
	public static void main(String[] args) {
		
		MainMenu.displayBanner();

	// In all case a connexion to the server will be attempt
		
		Connexion connexion = Args.setArgs(args);
		
	// Test if the server is accessible without credentials
		
		 if((Args.attack != null) && (Args.attack.equals("Bruteforce")))
		 {
			 if (connexion.isState() && (connexion.isNeed_creds()==false))
			 {
				 System.out.println("No credentials are required to access the Mbean Server");
				 System.out.println("The module will be launch to find others accounts");
			 }
			 Bruteforce bruteforce = new Bruteforce();
			 StringBuilder builder = new StringBuilder();
				for(String s : args) {
				    builder.append(s);
				    builder.append(" ");
				}
			 bruteforce.prepareExploit(builder.toString());
			 System.exit(0);
		 }
		 
	// If connexion is open but no valids credentials the bruteforce is automatically launched
			 
	  if (connexion.isState() == false)
	  {
		  if (connexion.isNeed_creds() == false)
		  {
			  System.exit(-1);
		  }
		  else
		  {
			  System.out.println("Bruteforce module will be launch");
			  Bruteforce bruteforce = new Bruteforce();
			  bruteforce.prepareExploit();
			  System.exit(0);
		  }
	  }
	  
	  // Connexion attribution to Action Class
	  
	  Action.setConnexion(connexion);
	  try
	  {
		Action.setMbsc(Action.getConnexion().getConnector().getMBeanServerConnection());
	  } catch (IOException e) 
	  {
		  System.out.println(e.getMessage());
	  }
	  
	// If the user choosed an attack to launch
	// In case of additional dev each module has to be present in jmxattack package
	  if(Args.attack != null)
	  {
			try 
			{
				Class<?> cl;
				cl = Class.forName("jmxattacks."+Args.attack);
				Attack myattack=(Attack) cl.newInstance();	
				StringBuilder builder = new StringBuilder();
				for(String s : args) {
				    builder.append(s);
				    builder.append(" ");
				}
				System.out.println(myattack.getAttack_name()+" will be launch");
				myattack.prepareExploit(builder.toString());
				
			}
			catch (Exception e1) 
			{
				System.out.println("Error loading module : " + e1.getMessage());
				}
	  }
	  else
	  {
		  // Launch Main menu  
		  Fullinfos infos = new Fullinfos();
		  MainMenu menu = new MainMenu();
	  }
	  	
	}

}
