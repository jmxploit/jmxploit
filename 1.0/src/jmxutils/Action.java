package jmxutils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;

import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;

import jmxmenus.MainMenu;
import jmxutils.Connexion;

public class Action {

	private static Connexion connexion;
	private static MBeanServerConnection mbsc;

	public static Connexion getConnexion() {
		return connexion;
	}

	public static void setConnexion(Connexion connexion) {
		Action.connexion = connexion;
	}

	public static MBeanServerConnection getMbsc() {
		return mbsc;
	}

	public static void setMbsc(MBeanServerConnection mbsc) {
		Action.mbsc = mbsc;
	}
	
	protected static  Set<ObjectName> queryNames(ObjectName name, QueryExp query){
		Set<ObjectName> end =null;
		
		try {
			end = new TreeSet<ObjectName>(getMbsc().queryNames(name, query));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return end;
		
	}
	
	protected static Object getAttribute(ObjectName name, String attribute){
		Object end = null;
		try {
			end = getMbsc().getAttribute(name, attribute);
		} catch (AttributeNotFoundException e) {
			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return end;
	}
	
	protected static Object invoke(ObjectName name, String operationName, Object[] params, String[] signature){
		
		Object end = null;
		
		try {
			end = getMbsc().invoke(name, operationName,params, signature);
			System.out.println("#### Operation " + operationName + " has been successfully invoked ####\n");
		} catch (InstanceNotFoundException e) {
			System.out.println(e.getMessage());	
		} catch (MBeanException e) {	
			System.out.println("Error when invoking module : " + e.getMessage() + "\n");	
			e.printStackTrace();
		} catch (ReflectionException e) {
			System.out.println(e.getMessage());	
		} catch (IOException e) {		
			System.out.println("Error when invoking module : " + e.getMessage() + "\n");	
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println("Error : User is not allowed to invoke operation " + operationName + " on " + name.toString() + " MBean");
		}catch (IllegalArgumentException e) {
			System.out.println("Error during object invocation the object is probably null"+ e.getMessage());
			e.printStackTrace();
		}
		catch (RuntimeOperationsException e){
			System.out.println("Error during object invocation the object is probably null" + e.getMessage());
			e.printStackTrace();
		}
		
		return end;
	
	}
	
	protected static void setAttribute(ObjectName name, Attribute attribute)
	{
		try {
			getMbsc().setAttribute(name, attribute);
		} catch (Exception e) {	
			System.out.println("ERROR " + e.getMessage()) ;
			MainMenu menu = new MainMenu();
		} 
	}
	
	public static boolean existObject(String name)
	{
		boolean end = false;
		ObjectName mbeanName = null;
		try {
			mbeanName = new ObjectName(name);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
        Set<ObjectName> names = new TreeSet<ObjectName>(queryNames(mbeanName, null));
        end = !names.isEmpty();
		return end;
	}
	
	protected static ObjectName createObject(String object_name)
	{
		ObjectName end = null;
		if (existObject(object_name))
		{
			try {
				end = new ObjectName(object_name);
			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {	
				e.printStackTrace();
			}
		
		}
		return end;
	}
	
	protected CommandLine attackMenu(Options options, String banner, String example )
	{
		System.out.println(banner);
		System.out.println(example);
		String s = "";
		boolean attack_options = false;
		CommandLine line = null;
		
		while (attack_options == false )
		{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    try {
				s = bufferRead.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		    if (s.equals("back") == false)
		    {        	
		    	Parser parser = new EasyBasicParser();
		    
			    try {
					line = parser.parse( options, StringtoArrayList(s));
			    	
					attack_options = true;
				} catch (Exception e) {
					System.out.println(e.getMessage());	
				}
		    }
		    else
		    {
		    	attack_options = true;
		    	MainMenu menu = new MainMenu();
		    }
   
		}
		 return line;	
	}
	
	public static String[] StringtoArrayList(String arg_string)
	{	
		String[] a = arg_string.split("--");
    	ArrayList<String> res=new ArrayList<String>();
    	for (int i = 1; i < a.length; i++) {
    		String[] b = a[i].split(" ");
    		String tempres="";
    		for (int j = 0; j < b.length; j++) {
    			//System.out.println(j);
				if (j==0) {
					res.add("--"+b[0]);
				}else{
					if (j != (b.length - 1))
					{
						tempres+=b[j]+" ";
					}
					else
					{
						tempres+=b[j];
					}
				}	
			}
    		res.add(tempres);	
		}
		return res.toArray(new String[res.size()]);
	}
}
