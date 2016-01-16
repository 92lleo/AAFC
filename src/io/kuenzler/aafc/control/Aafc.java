package io.kuenzler.aafc.control;

import io.kuenzler.aafc.data.FileManager;
import io.kuenzler.aafc.data.ZipUtils;
import io.kuenzler.aafc.view.DisclaimerView;
import io.kuenzler.aafc.view.MainView;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main control class.
 * 
 * @author Leonhard Kuenzler
 * @version 0.8.5
 * @date 29.06.15 02:24 | cleanup
 */
public class Aafc {

	private static final String version = "| v0.8.5b/15-09-09 devbuild!"; // TODO

	private MainView frame;
	private String build = "2832,0.8.5"; // build for update check
	private int os;
	private final ExecutionHandler execHandler;
	private final SdkManager sdkManager;
	private int mode, deviceStatus;
	private boolean realSdk;
	FileManager fm;
	private String jarPath, propsPath, tempPath, sdkPath, tempId;
	private final ProcessManager processManager;
	private Preferences preferences;
	private DeviceChecker deviceChecker;
	private DeviceManager deviceManager;
	private ActionCreator actionCreator;

	/**
	 * creates new AAFC main instance
	 */
	public Aafc() {
		Log.getLogger().info("AAFC started");
		checkOs();
		mode = 1; // TODO needed?
		deviceStatus = 0;
		tempId = "aafc_" + (int) System.currentTimeMillis();
		Log.getLogger().info("TempID is " + tempId);
		jarPath = new File("").getAbsolutePath(); // TODO pack to setPaths()
		Log.getLogger().info("JarPath is " + jarPath);
		initPreferences();
		execHandler = new ExecutionHandler(this);
		processManager = new ProcessManager(this);
		sdkManager = new SdkManager(this);
		actionCreator = new ActionCreator(this);
		loadFrame();
		setPaths();
		fm = new FileManager(this, true); // extract sdk files
		showDisc();
		deleteOldLogs();
		runCommand("start-server", 1, false, false); // Starting adb server
		deviceManager = new DeviceManager(this);
		setSdkWorking(fm.checkFiles());
		callingHome();
		initDeviceCheck();
	}

	/**
	 * Requires internet connection, checks version and sends start heartbeat.
	 */
	private void callingHome() {
		Utilities.heartbeat(getFromKey("uid"), null, false,
				Boolean.parseBoolean(getFromKey("heartbeatOnStart")), false);
		if (Boolean.parseBoolean(getFromKey("checkForUpdate"))) {
			checkUpdate(true);
		}
	}

	/**
	 * 
	 */
	private void initDeviceCheck() {
		deviceChecker = new DeviceChecker(this);
		// TODO check prefs here
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					Log.exception(e);
				}
				startChecking();
			}
		}.start();
	}

	private void initPreferences() {
		preferences = new Preferences(this);
		preferences.check();
		if (preferences.isFirstStart()) {
			// TODO ALERT SETTINGS!
			message("firststart");
			JOptionPane.showConfirmDialog(null, null);
			new io.kuenzler.aafc.view.Preferences(this);
		}
	}

	/**
	 * 
	 * @param hworking
	 *            Is ADB/FB busy?
	 */
	public void setWorking(final boolean hworking) {
		// TODO this.working = hworking;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setWorking(hworking);
			}
		});
	}

	public void deleteOldLogs() {
		try {
			int days = Integer.parseInt(preferences.getFromKey("keepOldLogs"));
			if (days < 1) {
				return;
			}
			Log.deleteOldLogs(days);
		} catch (Exception e) {
			Log.getLogger().warning(
					"Could not delete old logs: " + e.toString());
		}
	}

	/**
	 * Checks current os for compability. 1=win,2=mac,3=linux,0=unknown
	 */
	private void checkOs() {
		os = Utilities.getOs();
		Log.getLogger().log(Level.INFO, "OS is " + Utilities.getOsAsString());
		if (os == 1) { // windows
			// TODO entfernen
			message("Dev info message",
					"Your os is " + Utilities.getOsAsString()
							+ " and currently in BETA development state.\n"
							+ "Everything shown should work as expected :)",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (os == 2) { // osx
			// TODO entfernen
			message("Dev info message",
					"Your os is "
							+ Utilities.getOsAsString()
							+ " and currently in ALPHA development state.\n"
							+ "User commands should work, but be careful with gui pre-build commands.",
					JOptionPane.WARNING_MESSAGE);
		} else if (os == 3) { // linux
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e) {
				Log.exception(e);
			}
			// TODO entfernen
			message("Dev info message",
					"Your os is "
							+ Utilities.getOsAsString()
							+ " and currently in ALPHA development state.\n"
							+ "User commands should work, but be careful with gui pre-build commands.",
					JOptionPane.WARNING_MESSAGE);
		} else {
			message("AAFC Error - OS not supported,\nplease contact me!",
					Text.getOsError(), JOptionPane.ERROR_MESSAGE);
			exit();
		}
	}

	public ActionCreator getActionCreator() {
		return actionCreator;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	/**
	 * Checks if the program was ended properly.
	 */
	public void checkLegitStart() {
		if (preferences.getFromKey("running").equals("true")) {
			message("Illegal start", Text.getCrashMessage(),
					JOptionPane.ERROR_MESSAGE);
		} else {
			preferences.setFromKey("running", "true");
		}
	}

	/**
	 * Sets paths for jar, sdk and temporary dir
	 */
	private void setPaths() {
		// TODO constructor problem jarPath = new File("").getAbsolutePath();
		String fs = Utilities.getFileSep();
		propsPath = jarPath + fs + "adbfbdata" + fs;
		tempPath = new File(System.getProperty("java.io.tmpdir"))
				.getAbsolutePath() + fs + tempId;
		new File(tempPath).deleteOnExit();
		try {
			if (preferences.getFromKey("realSdk").equals("true")) {
				realSdk = true;
				sdkPath = preferences.getFromKey("sdkPath");
			} else {
				realSdk = false;
				sdkPath = tempPath;
			}
		} catch (NullPointerException e) {
			message("Error while setting paths",
					Text.getWrongLocationMessage(), JOptionPane.ERROR_MESSAGE);
			preferences.refactor();
			Log.exception(e);
		}
	}

	/**
	 * Sets the SDK label in gui (internal/sdk/not working)
	 * 
	 * @param working
	 *            true: working sdk
	 */
	public void setSdkWorking(boolean working) {
		frame.setSdkWorking(working, realSdk);
	}

	public String getBuild() {
		return build;
	}

	public void checkUpdate(boolean silent) {
		Utilities.checkUpdate(this, silent);
	}

	/**
	 * return OS as int. 1=win,2=mac,3=linux
	 * 
	 * @return 1=win,2=mac,3=linux
	 */
	public int getOs() {
		return os;
	}

	/**
	 * Starts selected Manager/monitor from Android sdk
	 * 
	 * @see SdkManager
	 * @param whatToStart
	 *            deviceMonitor,sdkMngr,AvdMngr
	 */
	public void startSdk(int whatToStart) {
		if (realSdk) {
			sdkManager.start(whatToStart);
		} else {
			// TODO no real sdk
			message("No real SDK installed");
		}
	}

	/**
	 * 
	 */
	public void startAvd() {
		// TODO testing code
		sdkManager.startFstAvd();
	}

	/**
	 * 
	 */
	private void showDisc() {
		try {
			String bool = preferences.getFromKey("showDisc");
			if (bool.equals("true")) {
				new DisclaimerView(this, frame);
			} else {
				frame.setVisible(true);
				checkLegitStart();
			}
		} catch (NullPointerException ex) {
			preferences.setFromKey("showDisc", "true");
			new DisclaimerView(this, frame);
		}
	}

	/**
	 * Reboots the phone to desired target.
	 * 
	 * @param target
	 *            0=os,1=bootloader,2=recovery
	 */
	public void reboot(int target) {
		// Target: 0=os, 1=bootloader, 2=recovery
		if (mode == 1 || mode == 2) {
			if (target == 2 && mode == 2) {
				updateCommandline("--Booting into recovery is only possible from ADB\n");
			}
			String command;
			switch (target) {
			case 0:
				command = "reboot";
				break;
			case 1:
				command = "reboot-bootloader";
				break;
			case 2:
				command = "reboot recovery";
				break;
			default:
				command = "";
				// TODO error
			}
			runCommand(command, mode, true, true);
			if (mode == 1 && target == 1) {
				setMode(Executer.FASTBOOT);
			} else if (mode == 2 && target == 0) {
				setMode(Executer.ADB);
			}
		} else {
			updateCommandline("--Reboot only works in ADB/Fastboot mode");
		}
	}

	/**
	 * Opens new MainGui after setting nimbus lookandfeel
	 */
	private void loadFrame() { // TODO lookandfeel
		// lookandfeel only in main
		frame = new MainView(this, version);
	}

	/**
	 * Calls updateCommandline methode in gui.
	 * 
	 * @param update
	 *            String containing the update
	 */
	public void updateCommandline(final String update) {
		frame.updateCommandline(update);
	}

	/**
	 * Small version of run_command for direct input use. current mode is used.
	 * wait and show set true
	 * 
	 * @param command
	 *            command as string
	 */
	public Process runCommand(String command) {
		return runCommand(command, mode, true, true);
	}

	/**
	 * Forwards command to execution handler.
	 * 
	 * @param command
	 *            command as string
	 * @param mode
	 *            mode to run: 0:nomode 1:adb 2:fastboot 3:cmd
	 * @param wait
	 *            true:join, false:ignore
	 * @param show
	 *            true:show update in gui, false:silent
	 */
	public Process runCommand(String command, int mode, boolean wait,
			boolean show) {
		return execHandler.runCommand(command, mode, wait, show);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getFromKey(String key) {
		return preferences.getFromKey(key);
	}

	protected void setDeviceSerial(String serial) {
		frame.setDeviceSerial(serial);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setFromKey(String key, String value) {
		preferences.setFromKey(key, value);
	}

	/**
	 * 
	 * @param text
	 * @param file
	 */
	public void writeToFile(String text, File file) { // TODO move to
														// filemanager
		Writer fw = null;
		try {
			fw = new FileWriter(file);
			String temp = text;
			temp = temp.replaceAll("\n", System.getProperty("line.separator"));
			// fw.append(System.getProperty("line.seperator")); // e.g. "\n"
			fw.write(temp);
			updateCommandline("--Commands saved as " + file.toString()
					+ ", ready to clear");
		} catch (IOException e) {
			updateCommandline("--error writing batch file (" + e + ")");
			Log.exception(e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					updateCommandline("--error closing output stream (" + e
							+ ")");
					Log.exception(e);
				}
			}
		}
	}

	/**
	 * Simple version of void message (string, string, int).
	 * 
	 * For easy use. Title: Info, Type: Information. Only Messagetext is needed
	 * 
	 * @see message(title, message, type)
	 * @param message
	 *            Message Test
	 */
	public void message(String message) {
		JOptionPane.showMessageDialog(frame, message, "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Send message to user via dialog.
	 * 
	 * @param title
	 *            Title of message window
	 * @param message
	 *            Message Text
	 * @param type
	 *            Mesaage Type (Error/Information)
	 */
	public void message(String title, String message, int type) {
		JOptionPane.showMessageDialog(frame, message, title, type);
	}

	/**
	 * 
	 * @param update
	 * @param clear
	 */
	public void updateExecuteline(String update, boolean clear) {
		frame.updateExecuteline(update, clear);
	}

	/**
	 * 
	 * @param update
	 */
	public void updateBatch(String update) {
		frame.updateBatch(update);
	}

	/**
	 * 
	 * @param title
	 * @param nofiles
	 * @param button
	 * @param filename
	 * @return
	 */
	public String chooseFileString(String title, boolean nofiles,
			String button, String filename) {
		File file = chooseFile(title, nofiles, button, filename);
		if (file == null) {
			return "<No file selected>";
		} else {
			return file.toString();
		}
	}

	/**
	 * 
	 * @param title
	 * @param nofiles
	 * @param button
	 * @param filename
	 * @return
	 */
	public File chooseFile(String title, boolean nofiles, String button,
			String filename) {
		JFileChooser chooser = new JFileChooser(new File("C:"));
		// chooser.setCurrentDirectory(new
		// File(System.getProperty("user.home")));
		chooser.setDialogTitle(title);
		chooser.setAcceptAllFileFilterUsed(nofiles);
		chooser.setApproveButtonText(button);
		chooser.setCurrentDirectory(new File(jarPath.toString()));
		chooser.setSelectedFile(new File(filename));
		if (nofiles) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	/**
	 * Kills selected Process 1:adb: uses kill-server command 2:fastboot: uses
	 * taskkiller from cmd
	 * 
	 * @param wtk
	 *            what to kill: 1=adb, 2=fastboot, default=error
	 */
	public void kill(final int wtk) { // 1: adb, 2: fastboot
		String processName;
		if (wtk == Executer.ADB) {
			processName = "adb";
		} else if (wtk == Executer.FASTBOOT) {
			processName = "fastboot";
		} else {
			return;
		}
		try {
			processManager.kill(processName);
		} catch (Exception e) {
			Log.exception(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getMode() {
		return mode;
	}

	/**
     *
     */
	public void reset() {
		mode = 0;
		// exec = new Executer(this);
		frame.setWorking(false);
		kill(Executer.ADB);
		kill(Executer.FASTBOOT);
		fm = new io.kuenzler.aafc.data.FileManager(this, false);
		fm = new io.kuenzler.aafc.data.FileManager(this, true);
		updateCommandline("--reset done");
	}

	/**
	 * Returns the working mode as String.
	 * 
	 * @return current mode as String
	 */
	public String getModeString() {
		switch (mode) {
		case Executer.ADB:
			return "ADB";
		case Executer.FASTBOOT:
			return "Fastboot";
		case Executer.CMD:
			return "CMD";
		default:
			return "null";

		}
	}

	/**
	 * kills adb and fastboot, deletes the files and exits
	 * 
	 * @see kill(int)
	 * @see exefiles.FileManager(Frame, boolean)
	 */
	public void exit() {
		Log.getLogger().info("Exit routine started");
		execHandler.killAllRunning();
		kill(1);
		kill(2);
		fm = new FileManager(this, false);
		preferences.setFromKey("running", "false");
		Log.getLogger().info("Exit routine finnished ...bye");
		System.exit(0);
	}

	/**
	 * Sets current working mode in main and frame object.
	 * 
	 * @param mode
	 *            mode to set as int
	 */
	public void setMode(final int mode) {
		if (mode > 0 && mode < 4) {
			this.mode = mode;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					frame.setMode(mode);
				}
			});
		} else {
			updateCommandline("--Wrong mode set(" + mode + ")");
		}
	}

	/**
	 * Returns gui frame as Frame.
	 * 
	 * @return main gui as Frame
	 */
	public MainView getFrame() {
		return frame;
	}

	/**
	 * 
	 * @return
	 */
	public String getJarPath() {
		return jarPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getTempPath() {
		return tempPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getPropsPath() {
		return propsPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getSdkPath() {
		return sdkPath;
	}

	/**
	 * 
	 * @param path
	 */
	public void setSdkPath(String path) {
		if (path == null) {
			sdkPath = tempPath;
			realSdk = false;
			preferences.setFromKey("realSdk", "false");
			updateCommandline("--SDK path changed to internal adb/fb");
		} else {
			sdkPath = path;
			realSdk = true;
			preferences.setFromKey("sdkPath", path);
			preferences.setFromKey("realSdk", "true");
			updateCommandline("--SDK path changed to " + path);
		}
		sdkManager.checkSdk();

	}

	public boolean checkFiles() {
		return fm.checkFiles();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getTempId() {
		return tempId;
	}

	public void exportMiniSdk() {
		// TODO missing
		JFileChooser chooser = new JFileChooser(getJarPath());
		chooser.setDialogTitle("Select path to save MiniSDK to");
		chooser.setSelectedFile(new File("MiniSDK.zip"));
		chooser.setApproveButtonText("Save here");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			new ZipUtils(new File(getTempPath()), chooser.getSelectedFile());
		} else {
			// TODO log here
		}
	}

	/**
	 * 
	 */
	public void changeSdkPath() {
		sdkManager.changePath();
	}

	/**
	 * Returns if real SDK is used
	 * 
	 * @return true:real sdk
	 */
	public boolean getRealSdk() {
		return realSdk;
	}

	public void stopChecking() {
		if (deviceChecker.isAlive()) {
			while (deviceChecker.isAlive()) {
				deviceChecker.interrupt();
			}
		}
	}

	public void startChecking() {
		if (!deviceChecker.isAlive()) {
			deviceChecker = new DeviceChecker(this);
			deviceChecker.start();
		}
	}

	/**
	 * Starts check for connected devices
	 * 
	 * @return
	 */
	public boolean checkDevices() {
		if (deviceChecker.isAlive()) {
			stopChecking();
		} else {
			startChecking();
		}
		return deviceChecker.isAlive();
	}

	/**
	 * setting device status
	 * 
	 * @param status
	 *            (0 nothing, 1 fastboot, 2 adb os, 3 adb sideload, 4 adb
	 *            unauth)
	 */
	public void setDeviceStatus(int status) {
		String newDeviceStatus = "";
		switch (status) {
		case 0:
			newDeviceStatus = "No device";
			break;
		case 1:
			newDeviceStatus = "Fastboot connected";
			break;
		case 2:
			newDeviceStatus = "ADB (OS) connected";
			break;
		case 3:
			newDeviceStatus = "ADB (Sideload) connected";
			break;
		case 4:
			newDeviceStatus = "ADB (OS) connected (unauthorized)";
			break;
		case 5:
			newDeviceStatus = "ADB (Recovery) connected";
			break;
		case 6:
			newDeviceStatus = "ADB (OS) connected (unauthorized)";
			break;
		case 7:
			newDeviceStatus = "ADB (OS) connected (offline)";
			break;
		default:
			newDeviceStatus = "No device";
		}
		deviceStatus = status;
		frame.setDeviceStatus(newDeviceStatus);

	}

	public int getDeviceStatus() {
		return deviceStatus;
	}

	public void openSdkLocation() {
		String fs = Utilities.getFileSep();
		File f = new File(sdkPath + fs + "platform-tools");
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.open(f);
			} catch (IOException e) {
				message(e.toString());
				Log.exception(e);
			}
		}
	}

	public void error(String message) {
		message("Error", message, JOptionPane.ERROR_MESSAGE);
	}

	public boolean getAutoChange() {
		return frame.getAutoChange();
	}

	/**
	 * Creates new AAFC. Exceptions are caught for beta tests
	 * 
	 * @param args
	 *            *not used*
	 */
	public static void main(String args[]) {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// nimbus look and feel only for Linux later
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new Aafc();
		} catch (Exception e) {
			Log.exception(e);
			Utilities.error(e, true);
		}
	}
}
