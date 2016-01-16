package io.kuenzler.aafc.control;

import java.io.File;

/**
 * This class creates actions from gui command builders
 * 
 * @author Leonhard Künzler
 * @version 0.1
 * @date 17.06.15
 *
 */

public class ActionCreator {

	private final Aafc main;

	public ActionCreator(Aafc main) {
		this.main = main;
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	public Process backupAdb(String command) {
		return null;
	}

	/**
	 * Installs an apk with given flags (lrtsd)
	 * 
	 * @param apk
	 *            The apk to install as file
	 * @param flags
	 *            install flags in order l,r,t,s,d
	 * @return Executing command as process
	 */
	public Process installApk(File apk, boolean[] flags) {
		if (apk == null || !apk.isFile()) {
			// TODO error message
			return null;
		}
		String command;
		Process process;
		command = "install";
		if (flags[0]) {
			command += " -l";
		}
		if (flags[1]) {
			command += " -r";
		}
		if (flags[2]) {
			command += " -t";
		}
		if (flags[3]) {
			command += " -s";
		}
		if (flags[4]) {
			command += " -d";
		}
		command += " ";
		command += apk.getAbsolutePath();
		process = main.runCommand(command, 1, true, true);
		return process;
	}

	/**
	 * Removes app package from device
	 * 
	 * @param appName
	 *            package to remove (eg com.whatsapp)
	 * @param keep
	 *            true: keeps applications cache and data
	 * @return execution command as process
	 */
	public Process removeApk(String appName, boolean keep) {
		String command;
		Process process;

		command = "uninstall";
		if (keep) {
			command += " -k";
		}
		command += " ";
		command += appName;
		process = main.runCommand(command, 1, true, true);
		return process;
	}

}
