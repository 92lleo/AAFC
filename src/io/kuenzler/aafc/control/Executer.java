package io.kuenzler.aafc.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Executes commands from gui user input or other objects
 *
 * @author Leonhard Kuenzler
 * @version 0.9.5
 * @date 03.10.14 17:30 | path management
 */
public class Executer extends Thread {

	private ProcessBuilder builder;
	protected Process process;
	private int mode;
	private String command;
	private boolean show, wait;
	private final Aafc main;
	private boolean shell;
	public final static int ADB = 1;
	public final static int FASTBOOT = 2;
	public final static int CMD = 3;

	/**
	 * sets the main variable on creation
	 *
	 * @see Aafc
	 * @param main
	 *            the main Adbfbtool object
	 */
	protected Executer(final Aafc main) {
		shell = false;
		this.main = main;
	}

	/**
	 * Threaded run methode. uses buildCommand and starts process from
	 * processBuilder reads output from process (depending on show) sets gui to
	 * working while executing (depending on wait)
	 */
	@Override
	public void run() {
		builder = buildCommand(command);
		process = null;
		try {
			process = builder.start();
		} catch (Exception ex) {
			if (show) {
				reportError(ex.toString());
			}
			return;
		}
		if (show) {
			show();
		}
		setWorking(false);
	}

	private void reportError(String error) {
		updateCommandline("--Crashed while starting process ("
				+ getModeString() + ", " + command + "):\n--" + error);

	}

	/**
	 * Calls main setWorking methode for gui
	 *
	 * @see Adbfbtool.setWorking(boolean working)
	 * @param working
	 *            true: gui set to work, false: gui set as ready
	 */
	private void setWorking(final boolean working) {
		main.setWorking(working);
	}

	void show() {
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		String current;
		long lastProgressBarUpdate = System.currentTimeMillis();
		try {
			while ((current = br.readLine()) != null) {
				line += current + "\n";
				long currentTime = System.currentTimeMillis();
				if (currentTime > lastProgressBarUpdate + 150) {
					lastProgressBarUpdate = currentTime;
					updateCommandline(line);
					line = "";
					/**
					 * Problem: wenn br.readLine blockiert wird line nicht
					 * ausgegeben
					 */
				}
			}
		} catch (IOException ex) {
			updateCommandline("--Crashed while reading cmd output:\n--" + ex);
		} finally {
			try {
				is.close();
				isr.close();
				br.close();
			} catch (IOException ex) {
				updateCommandline("--Crashed while closing stream/reader:\n--"
						+ ex);
			}
		}
		updateCommandline(line);
		main.setWorking(false);
	}

	/**
	 * Calls Main updateCommandline methode for gui
	 *
	 * @see Adbfbtool.updateCommandline(String update)
	 * @param update
	 *            Text to write into commandline
	 */
	private void updateCommandline(final String update) {
		main.updateCommandline(update);
	}

	/**
	 * Main execution methode Forwards command to processbuilder, sets flags and
	 * executes.
	 *
	 * @param command
	 *            command as string
	 * @param mode
	 *            mode to run: 0:nomode 1:adb 2:fastboot 3:cmd
	 * @param wait
	 *            true: wait for process, false:ignore process
	 * @param show
	 *            true: show update in gui, false: silent
	 */
	public Process runCommand(String command, int mode, boolean wait,
			boolean show) {
		setCommand(command, mode, wait, show);
		start();
		main.setWorking(true);
		try {
			sleep(25); // wait for process to start
		} catch (InterruptedException e) {
			Utilities.error(e, false);
		}
		return process;
	}

	protected void setCommand(String command, int mode, boolean wait,
			boolean show) {
		this.command = command;
		this.show = show;
		this.mode = mode;
		this.wait = wait;
	}

	/**
	 * Builds command, configures ProcessBuilder and returns pb with redicted
	 * error stream (outputt&err > output).
	 *
	 * @param command
	 *            command to execute
	 * @return processbuilder with commands ready to start
	 */
	ProcessBuilder buildCommand(String command) {
		String fs = Utilities.getFileSep();
		String[] cmdarray, progcmdarray;
		ProcessBuilder pb = new ProcessBuilder();
		cmdarray = command.split(" ");
		if (mode == 1 || mode == 2) {
			progcmdarray = new String[cmdarray.length + 1];
			String file = "";
			if (mode == 1) {
				if (main.getOs() == 1) {
					file = "adb.exe";
				} else {
					file = "adb";
				}
			} else {
				if (main.getOs() == 1) {
					file = "fastboot.exe";
				} else {
					file = "fastboot";
				}
			}
			progcmdarray[0] = main.getSdkPath() + fs + "platform-tools" + fs
					+ file;

			for (int i = 1; i < progcmdarray.length; i++) {
				progcmdarray[i] = cmdarray[i - 1];
			}
		} else if (mode == 3) {
			progcmdarray = new String[cmdarray.length + 2];
			progcmdarray[0] = "cmd";
			progcmdarray[1] = "/C";
			for (int i = 2; i < progcmdarray.length; i++) {
				progcmdarray[i] = cmdarray[i - 2];
			}
		} else {
			return null;
		}
		pb = pb.command(progcmdarray);
		return pb.redirectErrorStream(true);
	}

	/**
	 * Returns current mode as string for gui use
	 *
	 * @return current mode as string
	 */
	public String getModeString() {
		switch (mode) {
		case 1:
			return "ADB";
		case 2:
			return "Fastboot";
		case 3:
			return "CMD";
		default:
			return "null (nomode)";
		}
	}

	/**
	 * returns process for killing
	 *
	 * @return process as Process
	 */
	public Process getProcess() {
		return process;
	}

	protected void setShell(boolean newShell) {
		shell = newShell;
	}

	public void kill() {
		if (process != null) {
			process.destroy();
		}
	}

	/**
	 * Returns mode and command as String !!!only works if executer is set
	 * ready!!!
	 *
	 * @return String: "<mode> <command>"
	 */
	public String getCommand() {
		return getModeString() + " " + command;
	}

	public boolean getWait() {
		return wait;
	}

	public boolean isShell() {
		return shell;
	}

	public void runNextShell() {
		// TODO -
	}
}
