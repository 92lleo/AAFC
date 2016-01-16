package io.kuenzler.aafc.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 
 * @author Leonhard Kuenzler
 * @version 0.2
 * @date 01.10.14 | no gui process output
 */
public class ProcessManager {

	private final Aafc main;

	public ProcessManager(Aafc main) {
		this.main = main;
	}

	private int isProcessRunning(String serviceName) throws Exception {
		Process p = Runtime.getRuntime().exec("tasklist");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		int counter = 0;
		String line;
		while ((line = reader.readLine()) != null) {			
			if (line.contains(serviceName)) {
				counter++;
			}
		}
		return counter;
	}

	private void killProcess(String serviceName) throws Exception {
		Runtime.getRuntime().exec("tskill " + serviceName);
	}

	public void kill(final String serviceName) throws Exception {
		try {
			int counter = isProcessRunning(serviceName);
			if (counter != 0) {
				killProcess(serviceName);
				if (counter == 1) {
					main.updateCommandline("--Killed 1 instance of "
							+ serviceName + ".exe");
				} else {
					main.updateCommandline("--Killed " + counter
							+ " instances of " + serviceName + ".exe");
				}
			} else {
				main.updateCommandline("--No instance of " + serviceName
						+ ".exe running.");
			}

		} catch (Exception ex) {
			main.updateCommandline("--Killer killed itself :(");
		}
	}
}
