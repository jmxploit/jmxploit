jmxploit
========

Jmxploit is written in Java to audit security level of JMX API.
This tool is dedicated to security auditors and researchers in order to analyse JMX API security level.


How to :
=========

java -jar jmxploit.jar --host yourserver --port XXXX --login LOGIN --password PASSWORD

If authentication succeeded, information about Java environment will appear and menu will ask you to choose a module to launch.

To list modules please use :

java -jar jmxploit.jar --host yourserver --port XXXX --login LOGIN --password PASSWORD --list

To launch one of them without interactive menu:

java -jar jmxploit.jar --host yourserver --port XXXX --login LOGIN --password PASSWORD --attack ModuleName --arg1_to_mod arg1 --arg2_to_mod arg2


Contact : jeremy.mousset@bt.com
