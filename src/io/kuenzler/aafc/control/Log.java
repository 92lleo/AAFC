package io.kuenzler.aafc.control;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

/*
 * Global Logger for AAFC.
 * 
 * @author Leonhard Kuenzler 
 * @version 1.1
 * @date 17.08.15
 */
public class Log {

	private static Logger logger;
	private Handler fileHandler;
	private Formatter plainText;
	private static String logPath = null;

	/**
	 * Returns the global logger
	 * 
	 * @return logger as java.util.logging.Logger
	 */
	public static Logger getLogger() {
		if (logger == null) {
			new Log();
		}
		return logger;
	}

	/**
	 * Logs a message to global logger
	 * 
	 * @param level
	 *            A Java Level
	 * @param msg
	 *            Log message as String
	 */
	public static void log(Level level, String msg) {
		getLogger().log(level, msg);
	}

	public static void exception(Exception e) {
		getLogger().log(Level.WARNING, Utilities.exToString(e));
	}

	/**
	 * 
	 * @param msg
	 */
	public static void quickLog(String msg) {
		log(Level.INFO, msg);
	}

	public static void setLevel(Level level) {
		logger.setLevel(level);
	}

	/**
	 * Creates Log Object, creates path if not given
	 * 
	 * @throws IOException
	 */
	private Log() {
		try {
			logger = Logger.getLogger(Log.class.getName());
			if (logPath == null) {
				createLogPath();
			}
			fileHandler = new FileHandler(logPath, true);
			plainText = new SimpleFormatter();
			fileHandler.setFormatter(plainText);
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
			logger.log(Level.INFO, "Logger initialized");
		} catch (IOException e) {
			// File not write/readable or sth like that
			e.printStackTrace();
		}
	}

	public static void deleteOldLogs(int days) {
		String fs = Utilities.getFileSep();
		String path = System.getProperty("user.home") + fs + ".aafc" + fs;
		File directory = new File(path);
		int logFilesDeleted = 0;
		if (directory.exists()) {
			File[] listFiles = directory.listFiles();
			long purgeTime = System.currentTimeMillis()
					- (days * 24 * 60 * 60 * 1000);
			for (File listFile : listFiles) {
				if (listFile.lastModified() < purgeTime
						&& listFile.getName().endsWith(".log")) {
					if (!listFile.delete()) {
						System.err
								.println("Unable to delete file: " + listFile);
					} else {
						logFilesDeleted++;
					}
				}
			}
			quickLog("Deleted " + logFilesDeleted + " old logfiles.");
		} else {
			quickLog("Files were not deleted, directory .aafc doesn't exist!");
		}
	}

	/**
	 * Creates log path with current timestamp as name
	 */
	private void createLogPath() {
		String fs = Utilities.getFileSep();
		String path = System.getProperty("user.home") + fs + ".aafc" + fs;
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
		Date date = new Date();
		logPath = path + dateFormat.format(date) + ".log";
	}

	/**
	 * 
	 * @param path
	 */
	public static void setLogPath(String path) {
		logPath = path;
	}
}
