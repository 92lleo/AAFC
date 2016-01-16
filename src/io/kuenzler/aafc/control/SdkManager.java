package io.kuenzler.aafc.control;

import io.kuenzler.aafc.data.FileManager;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 
 * @author Leonhard Kuenzler
 * @version 0.5
 * @date 03.10.14 21:05 | final beta
 *
 */
public class SdkManager {

	public static final int SDKMANAGER = 0;
	public static final int AVDMANAGER = 1;
	public static final int DEVICEMONITOR = 2;

	private final Aafc main;
	private SdkExecuter exec;
	private ArrayList<String> avds;
	private long lastSdkError = 0;

	public SdkManager(Aafc main) {
		this.main = main;
	}

	public void changePath() {
		String fs = Utilities.getFileSep();
		String path;
		path = null;
		JFileChooser chooser = new JFileChooser(main.getJarPath());
		chooser.setDialogTitle("Choose SDK directory \"android-sdk\"");
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setApproveButtonText("Select directory");
		// chooser.setCurrentDirectory(new File(main.getJarPath()));
		// chooser.setSelectedFile(new File(filename));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(main.getFrame()) == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().toString();
		} else {
			// la problema
		}
		if (path == null) {
			main.setSdkPath(main.getTempPath());
			main.updateCommandline("--no selection - SDK changed to internal");
			lastSdkError = System.currentTimeMillis();
			return;
		}
		if (new File(path).isDirectory()
				&& (new File(path + fs + "platform-tools" + fs + "adb.exe")
						.exists() || new File(path + fs + "platform-tools" + fs
						+ "adb").exists())) {
			main.setSdkPath(path);
		} else {
			main.updateCommandline("No SDK change");
		}
	}

	protected void checkSdk() {
		if (lastSdkError != 0
				&& (System.currentTimeMillis() < lastSdkError + 500)) {
			Utilities.error(new Exception("SDK Support crashed"), true);
			return;
		}
		if (main.getOs() == 1) {
			boolean working = main.checkFiles();
			if (working == false) {
				main.setSdkPath(null);
				main.message("SDK error, changed to internal SDK");
			}
			working = main.checkFiles();
			if (working) {
				main.setSdkWorking(working);
			} else {
				main.message(
						"SDK Error",
						"Internal SDK doesnt work either\nPlease reset the application",
						JDialog.ERROR);
			}
		} else {
			main.setSdkWorking(true);
			main.updateCommandline("--no check for files in alpha (mac,linux)");
		}
	}

	public void start(int whatToStart) {
		if (main.getRealSdk()) {
			String command, fs;
			fs = Utilities.getFileSep();
			command = fs + "tools" + fs;
			switch (whatToStart) {
			case SDKMANAGER:
				command += "android.bat";
				break;
			case AVDMANAGER:
				command += "android.bat avd";
				break;
			case DEVICEMONITOR:
				command += "monitor.bat";
				break;
			default:
				main.message("wrong input: " + whatToStart);
			}
			exec = new SdkExecuter(main);
			exec.runCommand(main.getSdkPath() + command, false);
		} else {
			main.message("Only available in Android SDK");
		}
	}

	public void listAvds() {
		String fs = Utilities.getFileSep();
		exec = new SdkExecuter(main);
		exec.runCommand(main.getSdkPath() + fs + "tools" + fs
				+ "android list avd", true);
		try {
			exec.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<String> list;
		list = null;
		while (list == null) {
			@SuppressWarnings("unchecked")
			ArrayList<String> output = (ArrayList<String>) exec.getOutput();
			list = output;
		}
		ArrayList<String> filtered = new ArrayList<String>();
		for (String x : list) {
			if (x.contains("Name: ")) {
				filtered.add(x.substring(x.indexOf(':') + 1).trim());
			}
		}
		avds = filtered;
	}

	public void startAvd(String name) {
		String fs = Utilities.getFileSep();
		exec = new SdkExecuter(main);
		exec.runCommand(main.getSdkPath() + fs + "tools" + fs + "emulator @"
				+ name, false);
	}

	public void startFstAvd() {
		// listAvds();
		// if (!avds.isEmpty()) {
		String name = JOptionPane.showInputDialog(null, "Enter AVD name");
		// startAvd(avds.get(0));
		startAvd(name);
		// }
	}
}
