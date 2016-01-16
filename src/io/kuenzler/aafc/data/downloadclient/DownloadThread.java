package io.kuenzler.aafc.data.downloadclient;

import io.kuenzler.aafc.data.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadThread extends Thread {

    private final URL url;
    private final File file;
    private DownloadFrameThreaded frame; //referenz auf den frame

    public DownloadThread(URL url, File file, DownloadFrameThreaded frame) throws IOException
    {
        this.url=url; 
        this.file=file;
        this.file.createNewFile(); //erstellen der neuen datei
        this.frame=frame;        
    }

    @Override
    public void run() {        
        //vorgegebner quellcode aus DownloadFrame
        InputStream in = null;
        FileOutputStream out = null;
        try {
            // Try to determine the size of the file at `url`.
            final int size = FileManager.sizeOfFileAtURL(url);
            //frame referenz von oben nutzen, um progressbar zu veraendern
            frame.setProgressIndeterminate(size <= 0);

            try {
                // Open corresponding input- and output-streams.
                in = url.openStream();
                out = new FileOutputStream(file);

                // Use a 4kb buffer for transferring the bytes.
                byte[] buffer = new byte[4096];
                int totalBytes = 0;
                long lastProgressBarUpdate = System.currentTimeMillis();
                while (!this.isInterrupted()) {
                    // Read bytes from the input stream and store them in
                    // the buffer.
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    // Write the buffer to disk.
                    out.write(buffer, 0, bytesRead);
                    // Keep track of the number of bytes.
                    totalBytes += bytesRead;
                    // Update the progress every once in a while.
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > lastProgressBarUpdate + 250 && size > 0) {
                        lastProgressBarUpdate = currentTime;
                        frame.setProgress((int)(totalBytes * 100L / size));
                    }
                }
                if (size > 0) {
                    frame.setProgress((int)(totalBytes * 100L / size));
                }
            } finally {
                // Make sure the streams are closed.
                if (in != null) { in.close(); }
                if (out != null) { out.close(); }
            }
        } catch (IOException ex) {
            frame.error(ex.toString());
        }
        frame.done(); //schliesst das fenster
    } 
}
