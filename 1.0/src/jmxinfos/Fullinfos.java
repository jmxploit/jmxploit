package jmxinfos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Fullinfos extends Info{
	
	public Fullinfos()
	{
		ArrayList<String> list;
		System.out.println("\n\nPlease find below information about server environment");
		
		/*************************/
		/*   Print Hosts Infos   */
		/*************************/
		
		try {
			Hostinfos hostinfo;
			hostinfo = new Hostinfos();
			list = hostinfo.getHosts_list();
			Iterator<String> it = list.iterator();
			System.out.print("\nHosts available : ");
			while(it.hasNext()) {
			     Object element = it.next();
			     System.out.print(element + ", ");
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*************************/
		/*   Print OS Infos      */
		/*************************/
		Osinfos os = new Osinfos();
		System.out.print("\nOS Information : " + os.getInfos());	
		
		/*************************/
		/*   Print Java Infos    */
		/*************************/
		JavaInfos java = new JavaInfos();
		System.out.println("\nVersion : " + java.getVersion());
		System.out.println("ClassPath and Arguments : " + java.getPath());
		
		ManagerInfo manager = new ManagerInfo();
		if (manager.isPresent() == true)
		{
			list = manager.getHosts_list();
			Iterator<String> it = list.iterator();
			System.out.print("\n#####################################################################################\n Manager seems to be available  on following hosts    : ");
			
			while(it.hasNext()) {
			     Object element = it.next();
			     System.out.print(element + ", ");
			  }
			System.out.println("\n#####################################################################################\n");
		}
		
		
		
	}

}
