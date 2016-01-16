package io.kuenzler.aafc.view;

import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class RecordScreen extends Model {

	private JPanel contentPane;
	private JLabel l_png;
	public JButton b_screenshot;
	private JTextArea ta_info;
	private final io.kuenzler.aafc.control.RecordScreen recordScreen;
	public JButton b_startRecording;
	public JButton b_stopRecording;
	private JLabel l_screenshotHeader;
	private JButton b_saveScreenshot;
	private JSeparator separator;
	private JLabel lblVideo;
	private JTextArea ta_log;
	private JSeparator separator_1;
	private JScrollPane scrollPane_1;
	private JSlider s_bitrate;
	private JLabel l_lBitrate;
	private JLabel l_bitrate;
	private JSlider s_timelimit;
	private JLabel l_lTimelimit;
	private JLabel l_timelimit;
	private int timelimit, bitrate;
	private JCheckBox cb_bugreport;
	private JCheckBox cb_rotate;
	private JCheckBox cb_size;
	private JTextField t_width;
	private JLabel lblX;
	private JTextField t_heigh;
	private JButton b_saveVideo;
	private Process recording;
	private JButton b_playVideo;
	private JLabel lblPhoneTempStorage;
	private JComboBox cb_tempDir;

	/**
	 * Create the frame.
	 */
	public RecordScreen(final io.kuenzler.aafc.control.RecordScreen recordScreen) {
		super(691, 489);
		this.recordScreen = recordScreen;
		setResizable(false);
		setTitle("Record Device Screen");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				RecordScreen.class.getResource("/res/icon.jpeg")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		l_png = new JLabel();
		try {
			BufferedImage img = ImageIO.read(RecordScreen.class
					.getResource("/res/screen.jpeg"));
			Image dimg = img.getScaledInstance(245, 444, Image.SCALE_SMOOTH);
			ImageIcon screenshot = new ImageIcon(dimg);
			l_png.setIcon(screenshot);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		l_png.setBounds(430, 5, 245, 444);
		contentPane.add(l_png);

		b_screenshot = new JButton("Get Screenshot");
		b_screenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tempDir = (String) cb_tempDir.getSelectedItem();
				if (!tempDir.startsWith("/")) {
					tempDir = "/" + tempDir;
				}
				if (!tempDir.endsWith("/")) {
					tempDir += "/";
				}
				recordScreen.getScreenshot(tempDir);
			}
		});
		b_screenshot.setBounds(10, 213, 183, 23);
		contentPane.add(b_screenshot);

		ta_info = new JTextArea();
		ta_info.setLineWrap(true);
		ta_info.setText("ADB allows you to to get screenshots from your \r\ndevices screen and even record your screen. \r\nHere you're able to get screenshots and records from your phone display and save them.\r\nNote that taking a screenshot takes from 2 to 10\r\nseconds due slow connections.\r\nVideo max duration is 3 min due adb limitations.\r\nIf custom size is nan, it wont get passed.");
		ta_info.setEditable(false);
		ta_info.setBounds(10, 10, 389, 148);
		contentPane.add(ta_info);

		b_startRecording = new JButton("Start Recording");
		b_startRecording.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startRecording();
			}
		});
		b_startRecording.setBounds(10, 268, 183, 23);
		contentPane.add(b_startRecording);

		b_stopRecording = new JButton("Stop Recording");
		b_stopRecording.setEnabled(false);
		b_stopRecording.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recordScreen.stopRecording();
			}
		});
		b_stopRecording.setBounds(216, 268, 183, 23);
		contentPane.add(b_stopRecording);

		l_screenshotHeader = new JLabel("Screenshot");
		l_screenshotHeader.setBounds(10, 197, 94, 14);
		contentPane.add(l_screenshotHeader);

		b_saveScreenshot = new JButton("Save as");
		b_saveScreenshot.setEnabled(false);
		b_saveScreenshot.setBounds(216, 213, 183, 23);
		contentPane.add(b_saveScreenshot);

		separator = new JSeparator();
		separator.setBounds(20, 247, 366, 2);
		contentPane.add(separator);

		lblVideo = new JLabel("Video ");
		lblVideo.setBounds(10, 255, 46, 14);
		contentPane.add(lblVideo);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 400, 389, 49);
		contentPane.add(scrollPane_1);

		ta_log = new JTextArea();
		scrollPane_1.setViewportView(ta_log);
		ta_log.setBackground(SystemColor.menu);
		ta_log.setText("Ready.");
		ta_log.setLineWrap(true);
		ta_log.setEditable(false);

		separator_1 = new JSeparator();
		separator_1.setBounds(20, 393, 366, 2);
		contentPane.add(separator_1);

		l_lBitrate = new JLabel("Bitrate");
		l_lBitrate.setBounds(10, 302, 46, 14);
		contentPane.add(l_lBitrate);

		l_bitrate = new JLabel("4.0 Mb/s");
		l_bitrate.setHorizontalAlignment(SwingConstants.RIGHT);
		l_bitrate.setBounds(99, 302, 94, 14);
		contentPane.add(l_bitrate);

		l_timelimit = new JLabel("3 min");
		l_timelimit.setHorizontalAlignment(SwingConstants.RIGHT);
		l_timelimit.setBounds(305, 302, 94, 14);
		contentPane.add(l_timelimit);

		l_lTimelimit = new JLabel("Time limit");
		l_lTimelimit.setBounds(216, 302, 46, 14);
		contentPane.add(l_lTimelimit);

		s_timelimit = new JSlider();
		s_timelimit.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				timelimit = s_timelimit.getValue();
				int calc = (timelimit / 6);
				double result = (double) calc / 10;
				l_timelimit.setText(result + " min (" + timelimit + " sec)");
			}
		});
		s_timelimit.setValue(180);
		s_timelimit.setMinimum(1);
		s_timelimit.setMaximum(180);
		s_timelimit.setBounds(216, 319, 183, 14);
		contentPane.add(s_timelimit);

		s_bitrate = new JSlider();
		s_bitrate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				bitrate = s_bitrate.getValue() * 500000;
				l_bitrate.setText(((double) bitrate / 1000000) + " Mb/s");
			}
		});
		s_bitrate.setValue(8);
		s_bitrate.setMaximum(20);
		s_bitrate.setMinimum(1);
		s_bitrate.setBounds(10, 319, 183, 14);
		contentPane.add(s_bitrate);

		cb_bugreport = new JCheckBox("Add date, time, fps and device info");
		cb_bugreport.setBounds(10, 340, 199, 23);
		contentPane.add(cb_bugreport);

		cb_rotate = new JCheckBox("Rotate output 90\u00B0");
		cb_rotate.setBounds(216, 340, 183, 23);
		contentPane.add(cb_rotate);

		cb_size = new JCheckBox("Use custom size:");
		cb_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean enabled = cb_size.isSelected();
				t_heigh.setEditable(enabled);
				t_width.setEditable(enabled);
			}
		});
		cb_size.setBounds(10, 366, 107, 23);
		contentPane.add(cb_size);

		t_width = new JTextField();
		t_width.setEditable(false);
		t_width.setText("0");
		t_width.setBounds(123, 367, 31, 20);
		contentPane.add(t_width);
		t_width.setColumns(10);

		lblX = new JLabel("  x");
		lblX.setBounds(155, 370, 24, 14);
		contentPane.add(lblX);

		t_heigh = new JTextField();
		t_heigh.setEditable(false);
		t_heigh.setText("0");
		t_heigh.setColumns(10);
		t_heigh.setBounds(175, 367, 31, 20);
		contentPane.add(t_heigh);

		b_saveVideo = new JButton("Save as");
		b_saveVideo.setEnabled(false);
		b_saveVideo.setBounds(305, 366, 94, 23);
		contentPane.add(b_saveVideo);

		b_playVideo = new JButton("Play");
		b_playVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recordScreen.playVideo();
			}
		});
		b_playVideo.setBounds(216, 366, 79, 23);
		contentPane.add(b_playVideo);

		lblPhoneTempStorage = new JLabel(
				"Temp storage (change if you face problems)");
		lblPhoneTempStorage.setBounds(10, 169, 230, 14);
		contentPane.add(lblPhoneTempStorage);

		cb_tempDir = new JComboBox();
		cb_tempDir.setModel(new DefaultComboBoxModel(new String[] { "/sdcard",
				"/cache", "/sdcard0", "/sdcard1", "/data" }));
		cb_tempDir.setEditable(true);
		cb_tempDir.setBounds(239, 166, 160, 20);
		contentPane.add(cb_tempDir);

		setVisible(true);
	}

	private void startRecording() {
		String command, filePath, tempDir;
		tempDir = (String) cb_tempDir.getSelectedItem();
		if (!tempDir.startsWith("/")) {
			tempDir = "/" + tempDir;
		}
		if (!tempDir.endsWith("/")) {
			tempDir += "/";
		}
		filePath = tempDir+"screen.mp4";
		command = "shell screenrecord";
		if (cb_size.isSelected()) {
			try {
				int heigh, width;
				heigh = Integer.parseInt(t_heigh.getText());
				width = Integer.parseInt(t_width.getText());
				command += "-- size " + width + "X" + heigh;
			} catch (NumberFormatException e) {
				updateLog("Wrong size input, skipping...");
			}
		}
		command += " --bit-rate " + bitrate;
		if (cb_bugreport.isSelected()) {
			command += " --bugreport";
		}
		command += " --time-limit " + timelimit;
		if (cb_rotate.isSelected()) {
			command += " --rotate";
		}
		command += " "+filePath;
		recordScreen.startRecording(command);
	}

	public void setPng(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(l_png.getWidth(), 444,
				Image.SCALE_SMOOTH);

		ImageIcon screenshot = new ImageIcon(dimg);

		l_png.setText("");
		l_png.setIcon(screenshot);
	}

	/**
	 * Adds update text to textarea
	 * 
	 * @param update
	 */
	public void updateLog(String update) {
		ta_log.append("\n" + update);
		ta_log.setCaretPosition(ta_log.getDocument().getLength());
	}
}
