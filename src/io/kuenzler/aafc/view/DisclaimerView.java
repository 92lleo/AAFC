package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Aafc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Disclaimer - shown on first start
 * 
 * @author Leonhard Kuenzler
 * @version 0.7
 * @date 13.10.14 16:40 | default properties file update
 */
public class DisclaimerView extends JFrame {

	private static final long serialVersionUID = 3350048366675497405L;
	private JButton b_accept;
	private JCheckBox cb_read;
	private JTextPane tp_disc;
	private JCheckBox cb_showAgain;
	private MainView mainFrame;
	private final Aafc main;

	public DisclaimerView(Aafc aafc, MainView frame) {
		main = aafc;
		mainFrame = frame;
		mainFrame.setVisible(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 365) / 2, (dim.height - 292) / 2);
		setAlwaysOnTop(true);
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);
		setTitle("Disclaimer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DisclaimerView.class.getResource("/io/kuenzler/aafc/res/icon.jpeg")));
		initComponents();
		setVisible(true);
	}

	/**
	 * Sets accept button enabled if read checkbox is checked
	 */
	private void checkboxChanged() {
		b_accept.setEnabled(cb_read.isSelected());
	}

	/**
	 * When clicked on accept, propertiesfile is updated, frame becomes visible
	 * and disc disposes
	 */
	private void clickOnAccept() {
		if (cb_showAgain.isSelected()) {
			System.out.println("disc selected");
			main.setFromKey("showDisc", "false");
		}
		mainFrame.setVisible(true);
		dispose();
		main.checkLegitStart();
	}

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		getContentPane().setLayout(null);

		cb_read = new JCheckBox("I've read and understood the disclaimer ");
		cb_read.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				checkboxChanged();
			}
		});
		cb_read.setBackground(Color.WHITE);
		cb_read.setBounds(59, 165, 255, 23);
		getContentPane().add(cb_read);

		tp_disc = new JTextPane();
		tp_disc.setEditable(false);
		tp_disc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tp_disc.setBackground(UIManager.getColor("Button.background"));
		tp_disc.setBounds(0, 0, 349, 158);
		StyledDocument doc = tp_disc.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		tp_disc.setText("Attention!\r\nThis is your first run of the ADB/Fastboot Tool. Be aware\r\nthat you are able to brick your phone with this tool. I\r\ntake no responsibility for your actions and/or damage \r\nand/or malfunction on your device. If you don't know how\r\nto use ADB or Fastboot, read tuturials first.\r\nAlso please note that this is a beta release - Some actions may not\r\nwork as expected. Use at your own risk\r\n\r\nBy clicking <<Accept>>, you agree that you read the disclaimer\r\nand you will be responsible for you actions all alone.");
		getContentPane().add(tp_disc);

		b_accept = new JButton("Accept");
		b_accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clickOnAccept();
			}
		});
		b_accept.setBackground(Color.WHITE);
		b_accept.setBounds(75, 218, 200, 50);
		b_accept.setEnabled(false);
		getContentPane().add(b_accept);

		cb_showAgain = new JCheckBox("Don't show this next time");
		cb_showAgain.setBackground(Color.WHITE);
		cb_showAgain.setBounds(98, 188, 255, 23);
		getContentPane().add(cb_showAgain);

		setSize(new Dimension(365, 308));
	}
}
