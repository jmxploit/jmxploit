package jmxutils;


import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.jar.*;
import java.util.*;

import jmxattacks.Attack;

/*************************
 * 
 * Class used to find all modules availables on Jmxploit
 * 
 * *************************/
public class Modules {
		
	//static String module_liste[] ;
	private ArrayList<String> list ;
	
	
	public Modules()
	{
		this.list = new ArrayList<String> ();
		String packageName = "jmxattacks";
		File file = new File(Modules.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		// Test if program is launched from jar file
		String path =  file.getPath();
		boolean test = path.matches(".*\\.jar$");
		if (test == true)
		{
			packageName = packageName.replaceAll("\\." , "/");
			try
			{
					JarInputStream jarFile = new JarInputStream(new FileInputStream (path));
				    JarEntry jarEntry;
				    while(true)
				    {
				       jarEntry=jarFile.getNextJarEntry ();
				       if(jarEntry == null)
				       {
				    	   break;
				       }
					   if((jarEntry.getName ().startsWith (packageName)) && (jarEntry.getName ().endsWith (".class")) )
					   {
					         //this.list.add (jarEntry.getName().replaceAll("/", "\\.").substring(0, jarEntry.getName().lastIndexOf(".")).split("\\.")[1]);
						   this.list.add (jarEntry.getName().replaceAll("/", "\\.").substring(0, jarEntry.getName().lastIndexOf(".")));
					   }
				    }
			  }
			  catch( Exception e)
			  {
			     e.printStackTrace ();
			  }
		}else
		{
			
			String targetPackage = "jmxattacks";
			String targetPackagePath = targetPackage.replace('.', '/');
			String targetPath = "/"+targetPackagePath;
			URL resourceURL = Modules.class.getResource(targetPath);
			String packageRealPath = resourceURL.getFile();
			File packageFile = new File(packageRealPath);
			for (File classFile : packageFile.listFiles())
			{
			   String fileName = classFile.getName();
			   if (fileName.endsWith(".class")) 
			   {
			      String className = fileName.substring(0, fileName.lastIndexOf("."));
			      className="jmxattacks."+className;			      
			      this.list.add(className);			    
			   }
			}
		}
		if (this.list.contains("jmxattacks.Attack")) this.list.remove("jmxattacks.Attack");
	}
	
	public void listModules()
	{
		for (int i =0; i< this.list.size();i++ )
		{
			try{
			
			Class<?> cl = Class.forName(this.list.get(i));
			Attack myattack=(Attack) cl.newInstance();
			System.out.println(myattack.getAttack_name() +"\t\t" + myattack.getAttack_description());
			}catch(Exception e)
			{
				
			}
			
		}	
	}
	
	public void listModuleName()
	{
		for (int i =0; i< this.list.size();i++ )
		{
			System.out.println(this.list.get(i).split("\\.")[1]);
			
		}	
	}
	
	public ArrayList<String> getList()
	{
		return this.list;
	}
}
