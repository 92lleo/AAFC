package io.kuenzler.aafc.data.downloadclient;

import io.kuenzler.aafc.control.Aafc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private final JTextField txtFieldURL;
    @SuppressWarnings("unused")
	private final Aafc main;

    public MainFrame(Aafc main) {
        super("Simple Download Client");
        JLabel lbl = new JLabel("Enter URL:");
        add(lbl, BorderLayout.WEST);
        this.main = main;

        txtFieldURL = new JTextField(
                "http://vmschlichter9.informatik.tu-muenchen.de/pgdp/Chuck_Testa.mp4",
                30);
        add(txtFieldURL, BorderLayout.CENTER);

        JButton btn = new JButton("Start download");
        add(btn, BorderLayout.SOUTH);
        // Add a new instance of ActionListener to the button which we have
        // just created so that we can handle button presses.
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newDownload();
            }
        });

        pack();
    }

    private void newDownload() {
        try {
            URL url = new URL(txtFieldURL.getText());

            File file = FileTools.showSaveDialog(
                    MainFrame.this, null, FileTools.baseName(url));
            if (file != null) {
                //neuer verweis auf DownloadFrameThreaded
                DownloadFrameThreaded df = new DownloadFrameThreaded(url, file);
                df.startDownload();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    MainFrame.this,
                    ex.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
