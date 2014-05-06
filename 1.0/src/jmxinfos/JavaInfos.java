package jmxinfos;


import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


public class JavaInfos extends Info{
	private String mbean = "java.lang:type=Runtime";
	private String[] runtime = {"BootClassPath","ClassPath","InputArguments"};
	private String[] java = {"VmVendor","VmName","VmVersion"};
	private String version = "";
	private String path = "";
	
	public JavaInfos()
	{
		int att;
		
			ObjectName mbeanName;
			try {
				mbeanName = new ObjectName(mbean);
				for (att=0;att<java.length;att++){
					this.version = this.version+getAttribute(mbeanName, java[att]).toString()+ " ";
					}
				for (att=0;att<runtime.length - 1;att++){
					this.path = this.path+getAttribute(mbeanName, runtime[att]).toString()+ "\n";
					}
				String[] arg= (String[]) getAttribute(mbeanName,runtime[att]);
				for (att=0;att<arg.length;att++)
				{
					this.path = this.path + arg[att] + " ";
				}
				this.path=this.path+"\n";
				
			} catch (MalformedObjectNameException e) {				
				e.printStackTrace();
			} catch (NullPointerException e) {				
				e.printStackTrace();
			} 
			
	}

	public String getVersion() {
		
		return this.version;
	}
	public String getPath(){
		
		return this.path;
	}
}
