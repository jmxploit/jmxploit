package jmxinfos;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class Hostinfos extends Info{
	
	private String mbean = "Catalina:type=Host";
	private  ArrayList<String> hosts_list = new ArrayList<String> ();
	private ObjectName object ;

	public Hostinfos() throws IOException
	{
		
		 try {
			
		        this.object = new ObjectName(this.mbean+",host=*");
		        Set<ObjectName> names = new TreeSet<ObjectName>(queryNames(object, null));
		        for (ObjectName name : names) {
		           this.getHosts_list().add(name.toString().split("\\=")[2]);
		        }
		      
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			
			e.printStackTrace();
		}
		
	}

	public ArrayList<String> getHosts_list() {
		return hosts_list;
	}

	public void setHosts_list(ArrayList<String> hosts_list) {
		this.hosts_list = hosts_list;
	}
	
	
}
