package jmxutils;
import java.io.IOException;
import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/*************************
 * 
 * Class used connect to MBean Server
 * 
 * *************************/


public class Connexion {

	private String host;
	private String port;
	private boolean state = false;
	private boolean need_creds = false;
	private String login;
	private String password;
	private JMXConnector connector; 
	
	public void displayInfo(String message)
	{
		System.out.println(message);
	}

	public Connexion (String  host, String port, String login, String password,Boolean verbose)
	{
		
		this.host = host;
		this.port = port;
		this.login = login;
		this.password=password;
		Map env = new HashMap();
		String[] creds = {this.login, this.password};
		env.put(JMXConnector.CREDENTIALS, creds);
		JMXServiceURL url = null;
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + this.host+":"+this.port+"/jmxrmi");
			if (verbose) displayInfo("Connexion to JMX service at " + this.host + ":" + this.port);
		} catch (MalformedURLException e) {
			if (verbose)System.out.println("Erreur de connexion " + e.getMessage());
		}
        try {
			JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
			this.setState(true);
			this.setConnector(jmxc);
		} catch (IOException e) {
			if (verbose)System.out.println("Connection error. Is the service is open ? \n");
			
		}catch (SecurityException e) {
			if (verbose)System.out.println("Connection error  " + e.getMessage());
			this.setNeed_creds(true);
		}

		
	}
	
	public Connexion (String  host, String port)
	{
		this.host = host;
		this.port = port;
		JMXServiceURL url = null;
		try {
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + this.host+":"+this.port+"/jmxrmi");
			displayInfo("Connexion to JMX service at " + this.host + ":" + this.port);
		} catch (MalformedURLException e) {
			
			System.out.println("Erreur de connexion " + e.getMessage());
		}
        try {
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			this.setState(true);
			this.setConnector(jmxc);	
		} catch (IOException e) {
			System.out.println("Connection error. Is the service is open ? \n");
			
		}catch (SecurityException e) {
			System.out.println("Connection error " + e.getMessage());
			this.setNeed_creds(true);
			
		}

	}
	
	public Connexion ()
	{
				
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public JMXConnector getConnector() {
		return connector;
	}

	public void setConnector(JMXConnector connector) {
		this.connector = connector;
	}

	public boolean isNeed_creds() {
		return need_creds;
	}

	public void setNeed_creds(boolean need_creds) {
		this.need_creds = need_creds;
	}
}
