package io.kuenzler.aafc.control;

import io.kuenzler.aafc.data.PreferencesManager;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

/**
 * Manager class for preferences
 * 
 * @author Leonhard Kuenzler
 * @version 0.3
 * @date 01.10.14 19:30 | created
 * 
 */
public class Preferences {

	private String dir;
	private boolean firstStart;
	private final static String preferences = "aafc.props";
	// TODO private final static String devices = "devices.props";
	private final Aafc main;
	private final PreferencesManager pm;

	public Preferences(final Aafc main) {
		this.main = main;
		firstStart = true;
		// dir = this.main.getJarPath() + fs + "adbfbdata" + fs;
		dir = Utilities.getAafcDir();
		pm = new PreferencesManager(dir + preferences, 0);
	}

	@SuppressWarnings("finally")
	public String getFromKey(String key) {
		String s = null;
		try {
			s = pm.getProp(key);
		} catch (Exception e) {
			main.message("Preferences error", "Key <" + key + "> not found",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			return s;
		}
	}

	public void setFromKey(String key, String value) {
		pm.addProp(key, value);
	}

	public void check() {
		boolean success = pm.check();
		firstStart = !success;
	}

	public boolean delete(){
		return pm.delete();
	}

	public void refactor() {
		try {
			pm.refactor();
		} catch (FileNotFoundException e) {
			main.message("Error", e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @return
	 */
	public boolean isFirstStart() {
		return firstStart;
	}
}
