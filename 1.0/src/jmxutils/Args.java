package jmxutils;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Args {

	public static String attack;
	
	public static Options getOptions()
	{
		Options options = new Options();
		   options.addOption("h", "help", false, "prints the help content");
		   options.addOption(OptionBuilder
			       .withArgName("host")
			       .withLongOpt("host")
			       .hasArg()
			       .isRequired()
			       .withDescription("JMX server")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("port")
			       .hasArg()
			       .isRequired()
			       .withDescription("JMX port")
			       .withLongOpt("port")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("login")
			       .hasArg()
			       .withDescription("JMX login")
			       .withLongOpt("login")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("password")
			       .hasArg()
			       .withDescription("JMX password")
			       .withLongOpt("password")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("login_file")
			       .hasArg()
			       .withDescription("File containing logins to bruteforce")
			       .withLongOpt("login_file")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("pass_file")
			       .hasArg()
			       .withDescription("File containing passwords to bruteforce")
			       .withLongOpt("pass_file")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("list")
			       .withDescription("list-attack")
			       .withLongOpt("list")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("attack")
			       .hasArg()
			       .withDescription("attack to launch")
			       .withLongOpt("attack")
			       .create());
		   
		   return options;
	}
	
	public static Connexion setArgs(String[] args)
	{
		String host = "host";
		String port = "port";
		String login = "login";
		String password = "password";
		Connexion connexion = null;
		
		Options options = getOptions();  
		   CommandLineParser parser = new EasyBasicParser();
		
		    try 
		    {
		        CommandLine line = parser.parse( options, args );
		        if( line.hasOption( "list" ) )
		        {
		        	System.out.println("List of availables modules");
		        	Modules mod = new Modules();
		        	mod.listModules();
		        	System.exit(0);
			    }
		        else
		        {
		        	if (line.hasOption("attack"))
		        	{
		        		attack = line.getOptionValue("attack");
		        	}
		        	if ((line.hasOption(login)) && (line.hasOption(password)))
		        	{
		        	    // Return Connexion with login and password
		        		return (new Connexion(line.getOptionValue(host),line.getOptionValue(port),line.getOptionValue(login),line.getOptionValue(password),true));
		        	}
		        	else
		        	{
		        		//Return Connexion without login and password
		        		return (new Connexion(line.getOptionValue(host),line.getOptionValue(port)));
		        	}
		        }
		    }
		    catch( ParseException exp ) 
		    {
		        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
			    HelpFormatter formatter = new HelpFormatter();
			    formatter.printHelp( "jmxploit.jar", options );
			    System.exit(-1);
		    }
		    
			return connexion;
		    
	}
}
