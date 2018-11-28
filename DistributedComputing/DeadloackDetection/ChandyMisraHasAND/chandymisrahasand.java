/*
 * @author: Manish Singh
 * 
 */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.text.ChangedCharSetException;

public class chandymisrahasand {

	public static void main(String[] args) {

		// Read config of nodes

		BufferedReader br = null;
		File fFile = new File("");
		String cwd = fFile.getAbsolutePath();

		File nodes = new File(cwd + "\\cmhadd_nodes.config");
		try {
			br = new BufferedReader(new FileReader(nodes));
			int number_of_nodes = 0;
			String node_addr = br.readLine();
			ArrayList<String> node_table = new ArrayList<String>();

			while (node_addr != null) {
				node_table.add(node_addr);
				number_of_nodes++;
				node_addr = br.readLine();

			}
			br.close();
			// System.out.println(number_of_nodes);

			int[] siteNumber = new int[number_of_nodes];
			String[] ip_addr = new String[number_of_nodes];
			int[] port = new int[number_of_nodes];

			String[] tmpAddress = null;

			for (int counter = 0; counter < number_of_nodes; counter++) {

				tmpAddress = node_table.get(counter).split(" ");
				siteNumber[counter] = Integer.parseInt(tmpAddress[0]);
				ip_addr[counter] = tmpAddress[1];
				port[counter] = Integer.parseInt(tmpAddress[2]);

			}

			// preparing this site

			Scanner scan = new Scanner(System.in);

			int thissiteNumber = 0;

			int inFlag = 0;

			do {

				System.out.print("Enter site number (1-" + number_of_nodes + "): ");
				thissiteNumber = Integer.parseInt(scan.nextLine());

				if (thissiteNumber >= 1 && thissiteNumber <= number_of_nodes) {
					inFlag = 1;
				} else {
					System.out.println("Please enter the correct site number i.e. from 1 to  " + number_of_nodes);
				}

			} while (inFlag == 0);

			// Preparing processes
			File process_site = new File(cwd + "\\process.config");

			BufferedReader br1 = new BufferedReader(new FileReader(process_site));
			int process_count = 0;
			String process = br1.readLine();
			ArrayList<String> process_site_table = new ArrayList<String>();

			while (process != null) {
				process_site_table.add(process);
				process_count++;
				process = br1.readLine();

			}
			// System.out.println(process_count);

			// Initializing process per site

			String[] process_site_map = null;

			String process_numbers = "";
			int site_process_count = 0;

			Integer[][] process_map = new Integer[process_count][2];

			for (int i = 0; i < process_count; i++) {

				process_site_map = process_site_table.get(i).split(",");

				process_map[i][0] = Integer.parseInt(process_site_map[0]);
				process_map[i][1] = Integer.parseInt(process_site_map[1]);

				if (thissiteNumber == Integer.parseInt(process_site_map[1])) {
					site_process_count++;

				}

			}

			process[] processes = new process[site_process_count];

			int j = 0;

			for (int i = 0; i < process_count; i++) {

				process_site_map = process_site_table.get(i).split(",");

				if (thissiteNumber == Integer.parseInt(process_site_map[1])) {

					process_numbers += process_site_map[0] + " ";

					processes[j] = new process(Integer.parseInt(process_site_map[0]),
							Integer.parseInt(process_site_map[1]), process_count, site_process_count);
					j++;

				}

				// System.out.println(process_map[i][0]+" "+process_map[i][1]);

			}

			// System.out.println("....................................");

			// Constructing WFG

			String[] siteProcesses = process_numbers.split(" ");

			File dep_wfg = new File(cwd + "\\dependency.config");

			BufferedReader br2 = new BufferedReader(new FileReader(dep_wfg));

			ArrayList<String> dep_edge = new ArrayList<String>();

			String dependecy = br2.readLine();

			String[] depVertex = null;

			while (dependecy != null) {

				depVertex = dependecy.split(",");

				for (int i = 0; i < site_process_count; i++) {

					if (depVertex[0].equalsIgnoreCase(siteProcesses[i])) {
						// System.out.println(dependecy);
						processes[i].addDependOn(depVertex[1], 1);

					}

				}

				dependecy = br2.readLine();

			}

			// for(int i=0;i<site_process_count;i++) {
			// System.out.println("Process: "+processes[i].getProcessNo());
			// processes[i].printDepGraph();
			// }

			// Open a socket

			listenToBroadcast listenBcst = new listenToBroadcast(processes, port[thissiteNumber - 1], process_map,
					ip_addr, port, siteNumber);

			listenBcst.start();

			String input_query = "";

			System.out.println("This site has " + site_process_count + " process in total. Process numbers are: "
					+ process_numbers);

			while (!input_query.equalsIgnoreCase("quit")) {
				System.out.print("\n\nEnter process number for intitating deadlock check (" + process_numbers + "): ");

				Scanner scan_query = new Scanner(System.in);
				input_query = scan_query.nextLine();
				int process_flag = 0;
				int process_ref = 0;

				for (int i = 0; i < site_process_count; i++) {

					if (processes[i].getProcessNo() == Integer.parseInt(input_query)) {
						process_flag = 1;
						process_ref = i;
						break;
					}
				}

				if (process_flag == 1) {

					char result = processes[process_ref].checkDeadlock(processes, process_map);

					if (result == 'D') {
						System.out.println("Local deadlock detected.");
					}

					if (result == 'L') {
						System.out.println("The process is not dependent on any othe process, hence no deadloack .");
					}

					if (result == 'N') {
						System.out.println("No Local deadlock detected.");
						processes[process_ref].checkRemoteDeadlock(processes, process_map, ip_addr, port, siteNumber,
								Integer.parseInt(input_query));
					}

				} else {
					System.out.println(
							"It seems this process does not belong this site. The process numbers belonging to this site (site number: "
									+ thissiteNumber + ") are: " + process_numbers);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class process {
	int processNo = 0;
	int siteNo = 0;
	boolean[] dependent = null;
	int blocked = 0;
	int localProcessCount = 0;
	int totalProcesses = 0;
	int probeSent = 0;
	int detected = 0;
	ArrayList<Integer> dependedOn = new ArrayList<Integer>();

	public process(int processNum, int siteNum, int totalProcess, int localProcessNumber) {
		this.processNo = processNum;
		this.siteNo = siteNum;
		this.localProcessCount = localProcessNumber;
		this.dependent = new boolean[totalProcess];
		this.totalProcesses = totalProcess;
	}

	void addDependOn(String processNumber, int isBlocked) {
		dependedOn.add(Integer.parseInt(processNumber));
		this.blocked = isBlocked;
	}

	void printDepGraph() {
		System.out.println(dependedOn.toString());
	}

	int getProcessNo() {
		return processNo;
	}

	void checkRemoteDeadlock(process[] allProcess, Integer[][] process_map, String[] ip, int[] port, int[] sitelist,
			int initiator) {

		int DependSize = dependedOn.size();

		for (int i = 0; i < DependSize; i++) {

			// System.out.println(initiator+" "+dependedOn.get(i));
			sendProbe(initiator, dependedOn.get(i), processNo, process_map, ip, port, sitelist, allProcess, "");

		}

	}

	void sendProbe(int initiator, int node, int lastnode, Integer[][] process_map, String[] ip, int[] port,
			int[] sitelist, process[] allProcess, String visitedNodes) {

		// if(initiator==node) {
		// System.out.println("Deadloack detected.");
		// return;
		// }

		process procRef = null;

		int nextNode = 0;
		Integer nextSite = 0;

		for (int i = 0; i < totalProcesses; i++) {

			if (node == process_map[i][0]) {

				if (siteNo != process_map[i][1]) {

					nextSite = process_map[i][1];
					String probe = "probe," + initiator + "," + lastnode + "," + node;
					System.out.println("Sending probe: "+probe+", to site "+nextSite);

					int sizeOfsiteList = sitelist.length;
					int sitePosition = -1;
					for (int d = 0; d < sizeOfsiteList; d++) {
						if (sitelist[d] == nextSite) {
							sitePosition = d;
							// System.out.println(nextSite,sitelist[d],);
							break;
						}
					}

					try {
						// System.out.println(ip[sitePosition]+":"+port[sitePosition]);
						Socket skt = new Socket(ip[sitePosition], port[sitePosition]);

						// System.out.println(skt.getPort());

						OutputStream os = skt.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						BufferedWriter bw = new BufferedWriter(osw);
						bw.write(probe);
						bw.flush();
						skt.close();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					// local process
					if(initiator==node) {
						System.out.println("Process "+initiator+" is in a deadlock.");
						return;
					}
					lastnode = node;
					visitedNodes += String.valueOf(node) + " ";
					for (int k = 0; k < localProcessCount; k++) {

						if (allProcess[k].getProcessNo() == node) {
							procRef = allProcess[k];
						}
					}

					int depSize = procRef.dependedOn.size();

					for (int m = 0; m < depSize; m++) {
						int nextnode = procRef.dependedOn.get(m);
						String[] checkVisitedNodes = visitedNodes.split(" ");
						int sizeVisitedNodes = checkVisitedNodes.length;
						for (int g = 0; g < sizeVisitedNodes; g++) {
							if (Integer.parseInt(checkVisitedNodes[g]) == nextnode) {
								System.out.println(
										"A local deadlock has been detected on this site (Site Number:" + siteNo + ")");
								return;
							}
						}

						sendProbe(initiator, nextnode, lastnode, process_map, ip, port, sitelist, allProcess,
								visitedNodes);
					}

				}
				break;
			}

		}

	}

	char checkDeadlock(process[] allProcess, Integer[][] process_map) {
		int DependSize = dependedOn.size();

		if (DependSize == 0) {
			// System.out.println("There is no deadlock.");
			return 'L';
		}

		int depVert = 0;
		//ArrayList<Integer> otherSites = new ArrayList<Integer>();
		ArrayList<Integer> sameSites = new ArrayList<Integer>();
		for (int i = 0; i < DependSize; i++) {

			depVert = dependedOn.get(i);
			for (int j = 0; j < this.totalProcesses; j++) {
				if (depVert == process_map[j][0]) {

					if (siteNo == process_map[j][1]) {
						sameSites.add(depVert);
					} else {
						//otherSites.add(depVert);
					}
					break;
				}

			}

		}

//		System.out.println(sameSites.toString());
//		System.out.println(otherSites.toString());
		int sameSiteSize = sameSites.size();
		if (sameSiteSize > 0) {

			for (int i = 0; i < sameSiteSize; i++) {

				if (checkLocalDep(processNo, sameSites.get(i), "", process_map, allProcess) == 'D') {
					// System.out.println("Local Deadloack detected.");
					return 'D';
				}
			}

		}

		return 'N';

	}

	char checkLocalDep(int initiator, int checkingFor, String lastnodes, Integer[][] process_map,
			process[] allProcess) {

		if (initiator == checkingFor) {
			return 'D';
		}

		if (checkingFor == 0) {
			return 'N';
		}

		int processNumber = 0;
		process procRef = null;

		for (int i = 0; i < localProcessCount; i++) {

			if (allProcess[i].getProcessNo() == checkingFor) {
				procRef = allProcess[i];
			}

		}

		if (procRef == null) {
			return 'N';
		}

		int depSize = procRef.dependedOn.size();

		for (int k = 0; k < depSize; k++) {
			checkingFor = procRef.dependedOn.get(k);
			for (int j = 0; j < this.totalProcesses; j++) {
				if (checkingFor == process_map[j][0]) {

					if (siteNo == process_map[j][1]) {
						processNumber = checkingFor;
					}
					break;
				}

			}

			if (processNumber > 0) {

				String[] visitedNodes = lastnodes.split(" ");
				int sizeVisitedNodes = visitedNodes.length;
				if (visitedNodes[0] != "") {
					for (int h = 0; h < sizeVisitedNodes; h++) {
						if (Integer.parseInt(visitedNodes[h]) == processNumber) {
							return 'D';
						}

					}
				}

				lastnodes += String.valueOf(processNumber) + " ";
				if (checkLocalDep(initiator, processNumber, lastnodes, process_map, allProcess) == 'D') {
					return 'D';
				}
			}

		}

		return 'N';

	}

	void processProbe(int initiator, process[] allProcess, Integer[][] process_map, String[] ip, int[] port,
			int[] sitelist) {

		if (blocked == 1) {
			if (dependent[initiator - 1] == false) {
				dependent[initiator - 1] = true;
				if (processNo == initiator) {
					System.out.println("Process " + processNo + " is in a deadlock");
					return;
				}

				checkRemoteDeadlock(allProcess, process_map, ip, port, sitelist, initiator);
			} else {
				System.out.println("Process " + initiator + " is already marked as dependent on " + processNo
						+ ", Therefore, no probe will be sent further.");
			}
		}

	}

}

class listenToBroadcast extends Thread {

	int port = 0;
	process[] localprocess = null;
	Integer[][] process_map = null;
	String[] ip = null;
	int[] ports = null;
	int[] sitelist = null;

	public listenToBroadcast(process[] local_process, int port, Integer[][] processMap, String[] ip_addr,
			int[] allports, int[] sitelists) {
		this.port = port;
		this.localprocess = local_process;
		this.process_map = processMap;
		this.ip = ip_addr;
		this.ports = allports;
		this.sitelist = sitelists;

	}

	public void run() {

		try {
			ServerSocket serverSckt = new ServerSocket(port);
			while (true) {
				Socket skt = serverSckt.accept();
				new processRq(skt, localprocess, process_map, ip, ports, sitelist).start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class processRq extends Thread {

	Socket lSocket = null;
	process[] localprocess = null;
	Integer[][] process_map = null;
	String[] ip = null;
	int[] port = null;
	int[] sitelist = null;

	public processRq(Socket lSocket, process[] local_process, Integer[][] processMap, String[] ip_addr, int[] ports,
			int[] sitelists) {
		this.lSocket = lSocket;
		this.localprocess = local_process;
		this.process_map = processMap;
		this.ip = ip_addr;
		this.port = ports;
		this.sitelist = sitelists;
	}

	public void run() {

		BufferedReader in = null;

		// System.out.println(lSocket.getInetAddress().getHostAddress());

		try {
			in = new BufferedReader(new InputStreamReader(lSocket.getInputStream()));

			String command = "";
			String[] message = null;

			command = in.readLine();
			System.out.println("\n"+command);
			if (null != command) {
				if (command.charAt(0) == 'p') {

					message = command.split(",");
					int totallocalProcess = localprocess.length;
					for (int i = 0; i < totallocalProcess; i++) {
						if (localprocess[i].getProcessNo() == Integer.parseInt(message[3])) {
							localprocess[i].processProbe(Integer.parseInt(message[1]), localprocess, process_map, ip,
									port, sitelist);
						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				in.close();
				// out.close();
				lSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
