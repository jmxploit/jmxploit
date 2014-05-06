package jmxinfos;


import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


public class Osinfos extends Info {
	
	private String mbean = "java.lang:type=OperatingSystem";
	private String[] attributes = {"Name","Version"};
	private String osinfo ="";
	private ObjectName mbeanName ;
	
	public Osinfos()
	{
			try {
				this.mbeanName = new ObjectName(this.mbean);
			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {			
				e.printStackTrace();
			}
			int att;
			for (att=0;att<attributes.length;att++){
			this.osinfo = this.osinfo+getAttribute(mbeanName, attributes[att]).toString()+ " ";
			}
		
	}

	public String getInfos() {
		
		return this.osinfo;
	}

}
