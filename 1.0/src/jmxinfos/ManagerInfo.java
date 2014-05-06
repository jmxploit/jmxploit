package jmxinfos;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class ManagerInfo extends Info{
	
	private String mbean = "Catalina:type=NamingResources,context=/manager";
	private static boolean isPresent ; 
	private  ArrayList<String> hosts_list = new ArrayList<String> ();
	private ObjectName object ;

	
	public ManagerInfo (){
		 
		try {
				
		        this.object = new ObjectName(this.mbean+",host=*");
		        Set<ObjectName> names = new TreeSet<ObjectName>(queryNames(object, null));
		        for (ObjectName name : names) {
		           this.getHosts_list().add(name.toString().split("=")[3]);		        	
		        }      
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		
		if (this.hosts_list.isEmpty() == false)
		{
			this.setPresent(true);		
		}
		else
		{
			this.setPresent(false);
		}
		
	}


	public ArrayList<String> getHosts_list() {
		
		return this.hosts_list;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		ManagerInfo.isPresent = isPresent;
	}

}
