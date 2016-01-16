package io.kuenzler.aafc.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Screen Recording / Screenshots
 *
 * @author Leonhard Kuenzler
 * @version 0.1.0
 * @date 25.05.15 | works
 */

public class RecordScreen {

	Aafc main;
	io.kuenzler.aafc.view.RecordScreen view;
	private Process activeProcess;

	public RecordScreen(Aafc main) {
		this.main = main;
		this.view = new io.kuenzler.aafc.view.RecordScreen(this);
	}

	public void getScreenshot(final String tempDir) {
		if (!(main.getDeviceStatus() == 2)) {
			main.message("Connect your phone via ADB to work");
		} else {
			new Thread() {
				public void run() {
					String fs = Utilities.getFileSep();
					boolean show = false;
					try {
						view.b_screenshot.setEnabled(false);
						view.b_startRecording.setEnabled(false);
						activeProcess = null;
						view.updateLog("Taking screenshot...");
						activeProcess = main.runCommand("shell screencap -p "
								+ tempDir + "screen.png", Executer.ADB, false,
								show);
						activeProcess.waitFor();
						view.updateLog("Pulling screenshot...");
						activeProcess = main.runCommand("pull " + tempDir
								+ "screen.png", Executer.ADB, false, show);
						activeProcess.waitFor();
						main.runCommand("shell rm " + tempDir + "screen.png",
								Executer.ADB, false, show);
						view.updateLog("Showing Screenshot...");
						String path = main.getJarPath() + fs + "screen.png";
						if (new File(path).exists()) {
							view.updateLog("Screenshot existiert nicht.");
						} else {
							view.setPng(path);
							view.updateLog("Saved as " + path + "\n\nReady.");
						}
						view.b_screenshot.setEnabled(true);
						view.b_startRecording.setEnabled(true);
					} catch (InterruptedException e) {
						System.out.println(e);
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	public void startRecording(final String command) {
		new Thread() {
			public void run() {
				boolean show = false;
				String fs = Utilities.getFileSep();
				try {
					view.b_screenshot.setEnabled(false);
					view.b_startRecording.setEnabled(false);
					view.b_stopRecording.setEnabled(true);
					activeProcess = null;
					view.updateLog("Started screen recording...");
					activeProcess = main.runCommand(command, Executer.ADB,
							true, false);
					activeProcess.waitFor();
					view.b_stopRecording.setEnabled(false);
					sleep(100);
					view.updateLog("Stopped screen recording\nPulling video...");
					activeProcess = main.runCommand("pull /sdcard/screen.mp4",
							Executer.ADB, false, show);
					activeProcess.waitFor();
					main.runCommand("shell rm /sdcard/screen.mp4",
							Executer.ADB, false, show);
					String path = main.getJarPath() + fs + "screen.mp4";
					view.updateLog("Saved as " + path + "\n\nReady.");
					view.b_startRecording.setEnabled(true);
					view.b_screenshot.setEnabled(true);
				} catch (InterruptedException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void stopRecording() {
		activeProcess.destroy();
	}

	public void playVideo() {
		String fs = Utilities.getFileSep();
		try {
			File f = new File(main.getJarPath() + fs + "screen.mp4");
			if (!f.exists()) {
				view.updateLog("Video existiert nicht.");
				return;
			}
			Desktop.getDesktop().open(f);
		} catch (IOException e) {
			view.updateLog(e.toString());
		}
	}
}
