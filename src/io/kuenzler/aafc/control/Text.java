package io.kuenzler.aafc.control;

import java.util.HashMap;

/**
 * Help for texts and commands TODO Move to file
 *
 * @date 30.07.15
 * @author Leohnhard Kuenzler
 */
public class Text {

	/**
	 * @return own available commands as string hashmap
	 */
	public static HashMap<String, Integer> getOwnCommands() {
		HashMap<String, Integer> owncommands;
		owncommands = new java.util.HashMap<String, Integer>();
		owncommands.put("cls", 1);
		owncommands.put("clear", 1);
		owncommands.put("exit", 2);
		owncommands.put("mode fastboot", 3);
		owncommands.put("mode adb", 4);
		owncommands.put("mode cmd", 5);
		owncommands.put("helpme", 6);
		owncommands.put("commandlist", 7);
		owncommands.put("mode", 7);
		owncommands.put("reset path", 8);
		owncommands.put("about", 9);
		owncommands.put("happy easter", 10);
		owncommands.put("forcekill", 11);
		return owncommands;
	}

	public static String[] getDriverDownloadUrl() {
		return new String[0];
	}


	public static String getDisclaimer() {
		return "This is your first run of the ADB/Fastboot Tool. Be aware\n"
				+ "that you are able to brick your phone with this tool. I\n"
				+ "take no responsibility for your actions and/or damage \n"
				+ "and/or malfunction on your device. If you don't know how\n"
				+ "to use ADB or Fastboot, read tuturials first.\n\n"
				+ "By clicking <<OK>>, you agree that you read the disclaimer\n"
				+ "and you will be responsible for you actions all alone.";
	}

	public static String getCrashMessage() {
		return ("Looks like this program crashed last time.\n"
				+ "Please report occurring errors to aafc@kuenzler.io");
	}

	public static String getWrongLocationMessage() {
		return ("Looks like you openend the ADB Fastboot Tool .jar\n"
				+ "out of another Application. To run propery,\n"
				+ "you have to start it from its parent folder.\n"
				+ "This bug is well known and going to be fixed\n" + "soon.\n"
				+ "The application will start, but the initialization\n"
				+ "of all path variables will fail. You can manually\n"
				+ "edit them or start the programm from your file\n"
				+ "manager.\n" + "Proceed with caution.");
	}

	public static String getOsError() {
		return ("Currently only Windows, Mac and Linux are supported by AAFC.\n"
				+ "Your OS is " + Utilities.getOsAsString() + ".\n"
				+ "If you've further questions, please write me to\n"
				+ "aafc@kuenzler.io or visit\n" + "aafc.kuenzler.io.");
	}

	public static String getAbout() {
		return "AAFC - Android ADB Fastboot Commander\nby Leonhard Künzler (2015)\n\n"
				+ "Feedback to aafc@kuenzler.io\n\nsee "
				+ "http://aafc.kuenzler.io/\nfor new versions and changelog.";

	}
}
