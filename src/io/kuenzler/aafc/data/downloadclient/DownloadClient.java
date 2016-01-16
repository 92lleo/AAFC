package io.kuenzler.aafc.data.downloadclient;

import io.kuenzler.aafc.control.Aafc;

import javax.swing.JFrame;

/**
 *
 * @author  
 */
public class DownloadClient {

    public DownloadClient(final Aafc main) {
        MainFrame mf = new MainFrame(main);
        mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mf.setVisible(true);
    }
}
