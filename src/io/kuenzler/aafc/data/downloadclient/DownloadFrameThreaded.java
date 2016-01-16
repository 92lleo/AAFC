package io.kuenzler.aafc.data.downloadclient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class DownloadFrameThreaded extends JFrame {

    private final DownloadThread dt;
    private final JProgressBar progressBar;

    public DownloadFrameThreaded(URL url, File file) throws IOException {
        super(url.toString());//url als fenstertitel
        //neuer downloadthread (mit url,file, und dem dlFrameThreaded als referenz
        //fuer anderungen an der gui
        dt = new DownloadThread(url, file, this);     // possible IOException

        // User interface setup.
        JLabel lbl = new JLabel("Progress:");
        add(lbl, BorderLayout.WEST);

        progressBar = new JProgressBar(0, 100);
        add(progressBar, BorderLayout.CENTER);

        JButton button = new JButton("Cancel");
        add(button, BorderLayout.SOUTH);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cancel();
            }
        });

        pack();

    }

    public void startDownload() {
        setVisible(true); //sichtbarmachen des JFrames
        dt.start(); //starten des downloads
    }

    //die folgenden methoden werden durch invokeLater methode, da swing nicht
    //threadsicher ist
    public void setProgressIndeterminate(final boolean indeterminate) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setIndeterminate(indeterminate);
            }
        });
    }

    //update fuer die prograssbar, setzen des fortschritts
    public void setProgress(final int percent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (percent < 0 || percent > 100) {
                    throw new IllegalArgumentException("0 <= percent <= 100");
                }
                progressBar.setValue(percent);
            }
        });
    }

    //schliesst das fenster nach fertigem download
    public void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dispose();
            }
        });
    }

    public void cancel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dt.interrupt(); //unterbricht den download(-thread)
            }
        });
    }

    //gibt eine fehlermeldung aus
    public void error(final String errorMsg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(DownloadFrameThreaded.this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
