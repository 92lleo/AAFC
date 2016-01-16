package io.kuenzler.aafc.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 
 * @author Leonhard Kuenzler
 * @version 0.2
 * @date 03.10.14 21:00 | working
 */
public class SdkExecuter extends Executer {

	private ArrayList<String> output;

	protected SdkExecuter(Aafc main) {
		super(main);
		// run command , show, build command, report error
	}

	public void runCommand(String command, boolean show) {
		runCommand(command, ADB, false, show);
	}

	ProcessBuilder buildCommand(String command) {
		String[] cmdarray;
		cmdarray = command.split(" ");
		ProcessBuilder pb = new ProcessBuilder();
		pb = pb.command(cmdarray);
		return pb.redirectErrorStream(true);
	}

	void show() {
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		ArrayList<String> list = new ArrayList();
		String current;
		try {
			while ((current = br.readLine()) != null) {
				list.add(current);
			}
		} catch (IOException e) {
			reportError(e.toString());
			e.printStackTrace();
		} finally {
			try {
				is.close();
				isr.close();
				br.close();
			} catch (IOException ex) {
				reportError("--Crashed while closing stream/reader:\n--" + ex);
			}
		}
		output = list;
	}

	private void reportError(String error) {

	}

	public Object getOutput() {
		return output;
	}

}
