package io.kuenzler.aafc.data.downloadclient;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JFileChooser;

public class FileTools {

    /**
     * Display a "Save as..." dialog.
     * @param parent the parent component of this dialog, can be null
     * @param dirName the default directory, can be null
     * @param baseName the default filename, can be null
     * @return a new instance of File, or null if the user canceled
     */
    public static File showSaveDialog(Component parent, String dirName, String baseName) {
        String path = "";

        if (dirName == null) {
            path = path + System.getProperty("user.home");
        }

        if (baseName != null) {
            path = path + System.getProperty("file.separator") + baseName;
        }

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(path));
        if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }

        return null;
    }

    /**
     * @see FileTools#showSaveDialog(java.awt.Component, java.lang.String, java.lang.String)
     */
    public static File showSaveDialog(String dirName, String baseName) {
        return showSaveDialog(null, dirName, baseName);
    }

    /**
     * @see FileTools#showSaveDialog(java.awt.Component, java.lang.String, java.lang.String)
     */
    public static File showSaveDialog(String baseName) {
        return showSaveDialog(null, null, baseName);
    }

    /**
     * @see FileTools#showSaveDialog(java.awt.Component, java.lang.String, java.lang.String)
     */
    public static File showSaveDialog() {
        return showSaveDialog(null, null, null);
    }

    /**
     * @param fullPath
     * @return the directory portion of the given {@code fullPath}
     */
    public static String dirName(String fullPath) {
        int idx = fullPath.lastIndexOf(System.getProperty("file.separator"));
        return idx >= 0 ? fullPath.substring(0, idx) : "";
    }

    /**
     * @param fullPath
     * @return the filename portion of the given {@code fullPath}
     */
    public static String baseName(String fullPath) {
        int idx = fullPath.lastIndexOf(System.getProperty("file.separator"));
        return idx >= 0 ? fullPath.substring(idx + 1) : "";
    }

    /**
     * @param url
     * @return the filename portion of the full path of the given {@code url}
     * @see java.net.URL#getPath()
     */
    public static String baseName(URL url) {
        return baseName(url.getPath());
    }

    /**
     * @param url
     * @return the size of the resource at the given {@code url}, or -1 if the
     *  size cannot be determined
     * @throws IOException
     * @see java.net.URLConnection#getContentLength()
     */
    public static int sizeOfFileAtURL(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        int result = conn.getContentLength();
        conn.getInputStream().close();
        return result;
    }

}
