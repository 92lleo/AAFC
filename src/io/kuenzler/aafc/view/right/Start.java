package io.kuenzler.aafc.view.right;

import io.kuenzler.aafc.control.Aafc;

import javax.swing.JTextArea;
import javax.swing.JLabel;

public class Start extends Model {

	private static final long serialVersionUID = -1951239431266534186L;

	public Start(Aafc main) {
		super(main);
		working = false;
		init();
	}

	@Override
	public void init() {
		JTextArea ta_text = new JTextArea();
		ta_text.setEditable(false);
		ta_text.setWrapStyleWord(true);
		ta_text.setLineWrap(true);
		ta_text.setText("Welcome to AAFC\r\n\r\n09.09.15 | 00:30\r\nVersion 0.8.5b\r\nBuild 2832\r\n\r\nThis is a beta release.\r\nCheck out XDA thread by \r\nclicking Start>HowTo\r\n\r\nPlease report problems\r\nto xda-thread or \r\naafc@kuenzler.io\r\n\r\nChangelog:\r\n[added] support for mac & linux including their binaries\r\n[added] md5/sha-1/256 calc\r\n[added] device name&vendor\r\n[fixed] deviceCheck\r\n\r\n");
		ta_text.setBounds(10, 36, 224, 377);
		add(ta_text);

		JLabel lblWelcome = new JLabel("Welcome!");
		lblWelcome.setBounds(10, 11, 110, 14);
		add(lblWelcome);
	}
}
