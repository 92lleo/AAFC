package io.kuenzler.aafc.control;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Helper class
 *
 * @author Leonhard Kuenzler
 * @version 0.1.2 Added logging, changed domain
 * @date 28.07.15 | 00:15
 */

public class Utilities {

	/**
	 * Throws a test exception, now just logs it
	 */
	public static void crash() {
		Exception e = new Exception("This is a test exception");
		Log.getLogger().log(Level.INFO, "Triggered Test Exception");
		error(e, false);
	}

	/**
	 * 
	 * @return os file separator
	 */
	public static String getFileSep() {
		return java.io.File.separator;
	}

	/**
	 * Independent JDialog showing occuring exceptions
	 * 
	 * @param e
	 *            Exception as Java exception
	 * @param critical
	 *            ture: critical, false: warning
	 */
	public static void error(Exception e, boolean critical) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		final JDialog errDiag = new JDialog();
		errDiag.setTitle("Error - please report to leo@bwk-technik.de");
		String text;
		if (critical) {
			text = "This is a critical bug, do not continue!";
		} else {
			text = "This is a less critical bug, you may continue.";
		}
		final JTextArea ta = new JTextArea(text + "\n\n" + sw.toString());
		JScrollPane sc = new JScrollPane(ta);
		ta.setWrapStyleWord(true);
		ta.setForeground(Color.RED);
		ta.setEditable(false);
		JButton b_kill = new JButton("Kill AAFC");
		b_kill.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(1);
			}
		});
		b_kill.setVisible(true);
		JButton b_dispose = new JButton("Close Message");
		b_dispose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				errDiag.dispose();
			}
		});
		b_dispose.setVisible(true);
		JButton b_send = new JButton("Send Errorreport");
		b_send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String mail, body, subject;
				mail = "mailto:aafc@kuenzler.io?";
				body = "body=" + ta.getText();
				subject = "subject=AAFC_ERROR";
				mail += subject + "  ";
				try {
					Desktop.getDesktop().browse(new URI(mail));
				} catch (IOException | URISyntaxException e) {
					Log.exception(e);
				}
			}
		});
		b_send.setVisible(true);

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.add(sc);
		pan.add(b_dispose);
		pan.add(b_kill);
		pan.add(b_send);

		errDiag.add(pan);
		errDiag.pack();
		errDiag.setModal(true);
		errDiag.setVisible(true);
	}

	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String exToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionDetails = sw.toString();
		return exceptionDetails;
	}

	/**
	 * /** Sends usage heartbeat to aafc server
	 * 
	 * @param uid
	 *            the unique installation id
	 * @param device
	 *            device serial, if null: start count will be incremented
	 *
	 * @param sendSerial
	 *            true: sends serial when sending dev hb
	 * @param hb
	 *            true: send hb on start
	 * @param hbdev
	 *            true: sends hb on new dev connect
	 */
	public static void heartbeat(String uid, String device, boolean sendSerial,
			boolean hb, boolean hbdev) {
		String domain, url, serial, os, secret, params, type, content;
		URL myURL;
		os = getOsAsString();
		os = os.replace(" ", "_");
		os = os.replace("&", "_");
		os = os.replace("?", "_");
		// ******************
		secret = "*********************";
		if (device == null) {
			serial = "null";
			type = "start";
		} else {
			type = "device";
			if (device.startsWith("emulator")) {
				serial = "sdkEmulator";
			} else if (sendSerial) {
				serial = device.substring(0, device.length() / 2);
				for (int i = 0; i < device.length() / 2; i++) {
					serial += "X";
				}
			} else {
				serial = "x" + Math.abs(device.hashCode());
			}
		}

		if ((device == null && hb) || (device != null && hbdev)) {
			domain = "http://aafc.kuenzler.io/count.php";
			params = "uuid=" + uid + "&serial=" + serial + "&os=" + os
					+ "&secret=" + secret + "&type=" + type;
			url = domain + "?" + params;
			content = "";
			try {
				myURL = new URL(url);
				URLConnection myURLConnection = myURL.openConnection();
				myURLConnection.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						myURLConnection.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					content += inputLine;
				in.close();
				if (Integer.parseInt(content) == 1) {
					Log.getLogger().log(Level.INFO,
							"Heartbeat sent successful: " + url);
				}
			} catch (UnknownHostException e) {
				Log.getLogger().warning(
						"No internet connection/Host could not be reached");
			} catch (IOException e) {

			} catch (NumberFormatException e) {
				Log.getLogger().log(
						Level.WARNING,
						"Heartbeat failed, server returned: " + content
								+ ", query was " + url);
			}
		} else {
			// not allowed
			Log.getLogger().log(Level.WARNING, "Heartbeat not allowed");
		}

	}

	/**
	 * Convert a String, so it suits an http post (simple)
	 * 
	 * @param toConvert
	 *            string to post
	 * @return convertet string
	 */
	static public String convertToPostString(String toConvert) {
		String tmp = toConvert;
		tmp = tmp.replace(" ", "_");
		tmp = tmp.replace("&", "_");
		tmp = tmp.replace("?", "_");
		return tmp;
	}

	/**
	 * Checks aafc.kuenzler.io/version.txt for update. Will notify user about
	 * new versions
	 * 
	 * @param main
	 *            AdbFbTool for output
	 * @param silent
	 *            true: notification on new versions, false: always notification
	 */
	public static void checkUpdate(Aafc main, boolean silent) {
		URL versionFile;
		String build = main.getBuild();
		try {
			versionFile = new URL("http://aafc.kuenzler.io/version.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					versionFile.openStream()));
			String version;
			version = in.readLine();
			in.close();

			String[] oldV = build.split(",");
			String[] newV = version.split(",");

			if (version.equals(build)) {
				if (!silent) {
					main.message("AAFC is up to date (" + newV[1] + ", build "
							+ newV[0] + ")");
				}
			} else {
				if (Integer.parseInt(oldV[0]) < Integer.parseInt(newV[0])) {
					String message = "New version available: " + newV[1]
							+ ", build " + newV[0] + ".\nYour version is "
							+ oldV[1] + ", build " + oldV[0]
							+ ".\n\nSwitch to download page?";
					int openDlSite;
					openDlSite = JOptionPane.showConfirmDialog(null, message,
							"New Version", JOptionPane.YES_NO_OPTION);
					if (openDlSite == JOptionPane.YES_OPTION) {
						URI dl = new URI("http://aafc.kuenzler.io");
						Desktop.getDesktop().browse(dl);
					}
				} else {
					main.message("You use a newer build then published:"
							+ "\nAvailable version: " + newV[1] + ", build "
							+ newV[0] + ".\nYour version is " + oldV[1]
							+ ", build " + oldV[0] + ".");
				}
			}
		} catch (IOException | URISyntaxException e) {
			Log.exception(e);
		}
	}

	/**
	 * Returns current os
	 * 
	 * @return OS as string
	 */
	public static String getOsAsString() {
		return System.getProperty("os.name");
	}

	/**
	 * Returns current os as int.
	 * 
	 * @return 1: win, 2: mac, 3: unix, 0 unknown
	 */
	public static int getOs() {
		String os = getOsAsString().toLowerCase();
		if (os.indexOf("win") >= 0) {
			return 1;
		} else if (os.indexOf("mac") >= 0) {
			return 2;
		} else if (os.indexOf("nux") >= 0) {
			return 3;
		} else {
			return 0;
		}
	}

	/**
	 * @return
	 */
	public static String getAafcDir() {
		return System.getProperty("user.home") + getFileSep() + ".aafc"
				+ getFileSep();
	}
}
