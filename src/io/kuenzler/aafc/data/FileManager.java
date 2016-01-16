package io.kuenzler.aafc.data;

import io.kuenzler.aafc.control.Aafc;
import io.kuenzler.aafc.control.Utilities;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Exporter class for adb and fastboot files.
 * 
 * @author Leonhard Kuenzler
 * @version 0.9.2
 * @date 03.10.14 17:00 | new file check
 */
public final class FileManager {

	static private String dirParent = "io/kuenzler/aafc/res";
	static private String dir = "dirParent";
	// TODO get Files trough properties file

	public String[] files;

	static public final String[] FILES_1 = { "adb.exe", "fastboot.exe",
			"AdbWinApi.dll", "AdbWinUsbApi.dll" };
	static public final String[] FILES_2 = { "adb", "fastboot" };

	private final Aafc main;

	/**
	 * @param frame
	 *            main frame (for updates)
	 * @param todo
	 *            true: unzip files, false: delete files
	 */
	public FileManager(Aafc main, boolean todo) {
		this.main = main;
		try {
			if (main.getOs() == 1) {
				files = FILES_1;
				dir = dirParent+"/1";
			} else {
				files = FILES_2;
				if (main.getOs() == 2) {
					dir = dirParent+"/2";
				} else {
					String arch = System.getProperty("os.arch");
					if (arch.contains("64")) {
						dir = dirParent+"/3/64";
					} else {
						dir = dirParent+"/3/32";
					}
				}
			}
			if (todo) {
				extract(files);
			} else {
				delete(files);
			}
		} catch (URISyntaxException | IOException ex) {
			main.updateCommandline(ex.toString());
		}
	}

	/**
	 * @param url
	 * @return the size of the resource at the given {@code url}, or -1 if the
	 *         size cannot be determined
	 * @throws IOException
	 * @see java.net.URLConnection#getContentLength()
	 */
	public static int sizeOfFileAtURL(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		int result = conn.getContentLength();
		conn.getInputStream().close();
		return result;
	}

	/**
	 * 
	 * @param files
	 *            array with file names
	 * @throws URISyntaxException
	 * @throws ZipException
	 * @throws IOException
	 */
	public void extract(String[] files) throws URISyntaxException,
			ZipException, IOException {
		URI uri;
		for (String f : files) {
			uri = getJarURI();
			getFile(uri, f);
		}
		// boolean success = checkFiles(FILES);
		// if (success) {
		// main.updateCommandline("--All necessary files found. They'll be deleted on exit.");
		// } else {
		// main.updateCommandline("--Some files are missing, please restart or change SDK path");
		// }
	}

	/**
	 * 
	 * @param files
	 * @return
	 */
	public boolean checkFiles() {
		String fs = Utilities.getFileSep();
		int actual, desired;
		actual = 0;
		desired = files.length;
		for (String x : files) {
			if (new File(main.getSdkPath() + fs + "platform-tools" + fs + x)
					.exists()) {
				actual++;
			}
		}
		if (actual == desired) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * deletes the files from "files" array
	 * 
	 * @param files
	 *            array with file names
	 */
	public void delete(String[] files) {
		String fs = Utilities.getFileSep();
		String path = main.getJarPath();
		new File(path).mkdirs();
		for (String f : files) {
			File file = new File(path + fs + f);
			if (file.exists()) {
				file.delete();
				main.updateCommandline(f + " deleted");
			} else {
				// TODO main.updateCommandline(f + " not found");
			}
			file = new File(path);
			file.delete();
		}
	}

	/**
	 * 
	 * @return @throws URISyntaxException
	 */
	private URI getJarURI() throws URISyntaxException {
		final ProtectionDomain domain;
		final CodeSource source;
		final URI uri;

		domain = FileManager.class.getProtectionDomain();
		source = domain.getCodeSource();
		uri = source.getLocation().toURI();

		return (uri);
	}

	/**
	 * 
	 * @param where
	 * @param fileName
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 */
	private URI getFile(final URI where, final String fileName)
			throws ZipException, IOException {
		final File location;
		final URI fileURI;

		location = new File(where);

		// not in a JAR, just return the path on disk
		if (location.isDirectory()) {
			fileURI = URI.create(where.toString() + fileName);
		} else {
			final ZipFile zipFile;

			zipFile = new ZipFile(location);

			try {
				fileURI = extract(zipFile, fileName);
			} finally {
				zipFile.close();
			}
		}
		return (fileURI);
	}

	/**
	 * 
	 * @param zipFile
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private URI extract(final ZipFile zipFile, final String fileName)
			throws IOException {
		String fs = Utilities.getFileSep();
		final File tempFile;
		final ZipEntry entry;
		final InputStream zipStream;
		OutputStream fileStream;

		String tempDir = main.getTempPath();
		File f = new File(tempDir);
		if (!f.isDirectory()) {
			f.mkdirs();
			f.mkdir();
		}
		File adbTempDir = new File(tempDir + fs + "platform-tools" + fs);
		adbTempDir.mkdirs();
		adbTempDir = new File(adbTempDir.getAbsolutePath() + fs + fileName);
		System.err.println(adbTempDir);
		tempFile = adbTempDir;
		// tempFile = File.createTempFile(fileName,
		tempFile.deleteOnExit();
		entry = zipFile.getEntry(dir + "/" + fileName);

		if (entry == null) {
			throw new FileNotFoundException("File not in resources: "
					+ fileName + " in archive: " + zipFile.getName());
		}

		zipStream = zipFile.getInputStream(entry);
		fileStream = null;

		try {
			final byte[] buf;
			int i;

			fileStream = new FileOutputStream(tempFile);
			buf = new byte[1024];
			i = 0;

			while ((i = zipStream.read(buf)) != -1) {
				fileStream.write(buf, 0, i);
			}
		} finally {
			close(zipStream);
			close(fileStream);
		}
		if (main.getOs() != 1) {
			Runtime.getRuntime().exec("chmod 777 " + tempFile);
		}
		return (tempFile.toURI());
	}

	/**
	 * closes specified stream
	 * 
	 * @param stream
	 *            stream to close
	 */
	private void close(final Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (final IOException ex) {
				main.updateCommandline(ex.toString());
			}
		}
	}
	
	public void exportMiniSdk(){

		try {
			FileOutputStream fos = new FileOutputStream("atest.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			String file1Name = "file1.txt";
			String file2Name = "file2.txt";
			String file3Name = "folder/file3.txt";
			String file4Name = "folder/file4.txt";
			String file5Name = "f1/f2/f3/file5.txt";

			addToZipFile(file1Name, zos);
			addToZipFile(file2Name, zos);
			addToZipFile(file3Name, zos);
			addToZipFile(file4Name, zos);
			addToZipFile(file5Name, zos);

			zos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}