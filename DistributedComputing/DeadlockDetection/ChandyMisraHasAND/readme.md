# Chandy-Misra-Hass AND Algorithm #


This program uses Java scoket programming and threads to execute. This program will implement Chandy-Misra-Hass AND algorithm over the network or on a local computer using different ports. The assumption here is that all ip's and ports are accessible to each node and no firewall or network partition issue occurs.


## How to give input? ##

1. The program requires the follwing files

	a) **cmhadd_nodes.config** - It contain the site number, its ip address and it's port number.
	
	b) **process.config** - Thsi files contains the process mapping to the site. A site can have multiple processes
	
	c) **dependency.config** - This contains the dependency of processes on each other

2. All of the above files should be kept at the same level from which we are running the program (jar)

### cmhadd_nodes.config: ###

1. The assumption here is that all sites id's would be defined sequentially, in a continous manner starting from 1
2. Each line would contain only one site infomration

Format of cmhadd_nodes.config file:

```<site_id> <Ip_of_site> <port>```

Eg:<br/>
1 127.0.0.1 3450<br/>
2 127.0.0.1 3451<br/>
3 127.0.0.1 3452<br/>
4 127.0.0.1 3453<br/>

You can add or subtract sites from the list but the sequence numbers should be continous and no gap in site number should be there.

### process.config: ###

1. Each process number should be unique
2. Each line would contain only one process-site mapping
3. Each site can have many processes
4. Sequence of process-site mapping does not matter

Format of process.config file:

```<process_number> <site_number>```

eg:<br/>
1,1<br/>
2,1<br/>
4,2<br/>
5,2<br/>
6,3<br/>
7,3<br/>
8,4<br/>
9,4<br/>
10,1<br/>
11,1<br/>


### dependency.config: ###

1. Each line would contain only one process-process mapping (WFG)
2. Process to process mapping would be unique in the file
3. Sequence of Process to process mapping does not matter
4. No need to give the sitenumber where the processs relies

Format of dependency.config file:

```<from_process_no> <to_process_number>```

Eg:

5,4
4,8
8,9
9,7
7,10



## How to run the program? ##

This program is written in Java 1.8 (build 1.8.0_172-b11). So you would need the same JDK version as this is not tested against other JVM versions

To start the program, simply type [0] from the location where the jar and config files is placed. Both the jar and config files should be placed at the same level.

[0] ```java -jar chandymisrahas_and.jar```

Once the program starts, it will ask the site number like[1]:

[1] ```Enter site number (1-4):```

Enter the site  number. Once entered. It will give the prompt [2]

[2] ```Enter process number for intitating deadlock check (<all process numbers belonging to that site>)```

Before entering a process number please ensure all sites are initialized (by running jar on each computer or prompt (eg: 4 cmd prompts for 4 sites on the same computer)). This program assumes that all sites are up and running all the time. If you get any exception while initializing the program, simply quit the program and restart it.

Once all sites are up, press the process number on any site and the algorthm will work.

The program uses threads to handle the probe. So, on the terminal you would see all the logs printed like 

```

Enter site number (1-4): 4
This site has 2 process in total. Process numbers are: 8 9


Enter process number for intitating deadlock check (8 9 ): 9
No Local deadlock detected.


Enter process number for intitating deadlock check (8 9 ):
probe,9,4,8
Process 9 is in a deadlock.

```


The program has been enahnced to detect remote local deadlocks when a proble travels the graph. For eg: consider this

Process 8,9 lives on site 4<br/>
Process 7 on site 3<br/>
Process 2,10,13,14 on site 1<br/>
Process 4,5 on site 2<br/>

Now consider this deadlock graph:<br/>
```9->7->10->2->5->4->8->9```

and

```2->13->14->2```

When we probe for 9, the program will detect that 9 is blocked, but while it had sent a probe to site 1, it also detected that there is a local deadlock and prints a message[4] on the site where this happens. This functionality does not hinder with the chandy-misra-hass algorithm and the probe will work as required irrespective of this detection

[4] ```A local deadlock has been detected on this site (Site Number:1)```

Eg log:

```

probe,5,7,10
Sending probe: probe,5,2,5, to site 2
A local deadlock has been detected on this site (Site Number:1)

```


## Quiting the program ##
To quit the program, simply press Ctrl+C


## Compiling the program ##

**Step1:** Place chandymisrahasand.java and all config files (cmhadd_nodes.config, process.config, dependency.config ) in a folder at the same level

**Step2:** compile the code using: javac chandymisrahasand.java

**Step3:** Run the code using: java chandymisrahasand

