package io.kuenzler.aafc.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.Random;

/**
 * For saving/loading Java Properties files
 * 
 * @author Leonhard Kuenzler
 * @see io.kuenzler.aafc.control.Preferences
 * @version 0.5.1
 * @date 14.10.14 14:45 | refactoring
 */
public class PreferencesManager {

	public final static int ADBFBINI = 0;
	public final static int DEVICES = 1;
	public final static int DEVICE = 2;

	private Properties props;
	private String file;
	private int type;

	/**
	 * 
	 * @param file
	 * @param type
	 */
	public PreferencesManager(String file, int type) {
		this.type = type;
		this.file = file;// TODO "adbfbtool/adbfbdata.properties";
		props = new Properties();
		readProps();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addProp(String key, String value) {
		props.setProperty(key, value);
		writeProps(); // TODO timer?
	}

	/**
	 * get Property from key
	 * 
	 * @param key
	 * @return
	 */
	public String getProp(String key) {
		String prop;
		prop = props.getProperty(key);
		return prop;
	}

	/**
	 * 
	 */
	private void writeProps() {
		check();
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			props.store(output, null);
		} catch (IOException e) {
			System.err.println("Props store");
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	private void readProps() {
		check();
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			props.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param type
	 */
	public void readDefaultProps(int type) {
		String resPath = "";
		switch (type) {
		case ADBFBINI:
			resPath = "/io/kuenzler/aafc/res/default.properties";
			break;
		case DEVICE:
			resPath = "/io/kuenzler/aafc/res/device.properties";
			break;
		case DEVICES:
			resPath = "/io/kuenzler/aafc/res/devices.properties";
		}
		try {
			props.load(PreferencesManager.class.getResourceAsStream(resPath));
		} catch (IOException e) {
			System.err.println("ReadDefaultError");
			e.printStackTrace();
		}
	}

	/**
	 * Check for valid props file
	 */
	public boolean check() {
		File f = new File(file);
		if (!f.exists()) {
			createPropsFile();
			return false;
		} 
		return true;

		// TODO check here for old version of properties file
	}

	/**
	 * Create a new props file with default props
	 * 
	 * @see readDefaultProps(int type)
	 * @see writeProps()
	 */
	private void createPropsFile() {
		File f = new File(file);
		try {
			BigInteger uid = new BigInteger(128, new Random());
			f.getParentFile().mkdirs();
			f.createNewFile();
			readDefaultProps(type);
			addProp("uid", uid.toString());
			writeProps();
		} catch (IOException e) {
			System.err.println("PreferencesManager.check() Error");
			e.printStackTrace();
		}
	}

	/**
	 * Updates to new prop file
	 * 
	 * @throws FileNotFoundException
	 */
	public void refactor() throws FileNotFoundException {
		readProps();
		if (delete()) {
			createPropsFile();
		} else {
			// TODO error
		}

	}

	public boolean delete() {
		File f = new File(file);
		props.clear();
		if (f.delete()) {
			props.clear();
			return true;
		} else {
			return false;
		}
	}
}
