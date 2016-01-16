package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Aafc;
import io.kuenzler.aafc.control.Log;
import io.kuenzler.aafc.control.Utilities;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;

public class Preferences extends Model {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel p_general;
	private JPanel p_workspace;
	private JPanel p_execution;
	private JCheckBox cb_showdisc;
	private JCheckBox cb_useRealSdk;
	private JCheckBox cb_clearOnChange;
	private JLabel l_useRealSdk;
	private JTextField t_useRealSdk;
	private JButton btnSelect;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private final Aafc main;
	private JCheckBox cb_backgroundDeviceSearch;
	private JLabel lblcheckForConnected;
	private JPanel panel;
	private JCheckBox cb_sendHbOnStart;
	private JCheckBox cb_sendHbOnNewDevice;
	private JCheckBox cb_includeSerial;
	private JTextField t_xdaName;
	private JLabel lblNewLabel_3;
	private JTextPane txtpnIfYouEnable;
	private JScrollPane scrollPane;
	private JLabel lblAnonymously;
	private JLabel lblanonymouslyViaUid;
	private JLabel lblprivateDataBlank;
	private JCheckBox cb_autoUpate;
	private JLabel lblConnectsToUpdate;
	private JButton b_deleteProperties;
	private JTabbedPane tabbedPane_1;
	private JPanel p_log;
	private JLabel lblNewLabel_4;
	private JComboBox cbx_logDays;
	private JLabel lblDayskeepForever;
	private JButton b_deleteOldLogs;
	private JButton b_openLogFolder;
	private JLabel lblSetLogLevel;
	private JComboBox comboBox;

	/*
	 * running=false showDisc=true realSdk=false sdkPath=null devices=0
	 * devicesPath=null version=3
	 */

	/**
	 * Create the frame.
	 * 
	 * @param main
	 */
	@SuppressWarnings("unchecked")
	public Preferences(final Aafc main) {
		super(450, 300);
		this.main = main;
		setTitle("Preferences");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[260px:423.00px,grow,fill]",
				"[100px:244.00px,grow,fill]"));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0,grow");

		p_general = new JPanel();
		tabbedPane.addTab("General", null, p_general, null);
		p_general
				.setLayout(new MigLayout(
						"",
						"[50.00px:60.00][70px:70px,grow][70px:70px,grow][70px:70:90px]",
						"[][][][][][][]"));

		cb_showdisc = new JCheckBox("Show disclaimer");
		cb_showdisc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setPreference("showDisc",
						Boolean.toString(cb_showdisc.isSelected()));
			}
		});
		p_general.add(cb_showdisc, "cell 0 0 2 1");

		lblNewLabel = new JLabel("(Shows Disclaimer at startup)");
		p_general.add(lblNewLabel, "cell 2 0 2 1,alignx right");

		cb_useRealSdk = new JCheckBox("Use real SDK");
		cb_useRealSdk.setEnabled(false);
		p_general.add(cb_useRealSdk, "cell 0 1 2 1");

		lblNewLabel_1 = new JLabel("(Use internal or real SDK)");
		lblNewLabel_1.setEnabled(false);
		p_general.add(lblNewLabel_1, "cell 2 1 2 1,alignx right");

		l_useRealSdk = new JLabel("SDK Path");
		l_useRealSdk.setEnabled(false);
		p_general.add(l_useRealSdk, "cell 0 2");

		t_useRealSdk = new JTextField();
		t_useRealSdk.setEnabled(false);
		p_general.add(t_useRealSdk, "cell 1 2 2 1,growx");
		t_useRealSdk.setColumns(10);

		btnSelect = new JButton("Select");
		btnSelect.setEnabled(false);
		p_general.add(btnSelect, "cell 3 2");

		cb_clearOnChange = new JCheckBox("Clear output on change");
		cb_clearOnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPreference("clearOutputOnChange",
						Boolean.toString(cb_clearOnChange.isSelected()));
			}
		});
		p_general.add(cb_clearOnChange, "cell 0 3 2 1");

		lblNewLabel_2 = new JLabel("(Clear output on mode change)");
		lblNewLabel_2.setEnabled(false);
		p_general.add(lblNewLabel_2, "cell 2 3 2 1,alignx right");

		cb_backgroundDeviceSearch = new JCheckBox("Background device search");
		cb_backgroundDeviceSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPreference("backgroundDeviceSearch", Boolean
						.toString(cb_backgroundDeviceSearch.isSelected()));
			}
		});
		p_general.add(cb_backgroundDeviceSearch, "cell 0 4 2 1");

		lblcheckForConnected = new JLabel(
				"(Check for connected devices in background)");
		lblcheckForConnected.setHorizontalAlignment(SwingConstants.RIGHT);
		lblcheckForConnected.setFont(new Font("Tahoma", Font.PLAIN, 11));
		p_general.add(lblcheckForConnected, "cell 2 4 2 1,alignx right");

		cb_autoUpate = new JCheckBox("Check for updates");
		cb_autoUpate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPreference("checkForUpdate",
						Boolean.toString(cb_autoUpate.isSelected()));
			}
		});
		p_general.add(cb_autoUpate, "cell 0 5 2 1");

		lblConnectsToUpdate = new JLabel(
				"(Connects to update server on every start)");
		lblConnectsToUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConnectsToUpdate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		p_general.add(lblConnectsToUpdate, "cell 2 5 2 1,alignx right");

		b_deleteProperties = new JButton("Delete properties file");
		b_deleteProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "Your properties will be deleted, you will get a new UID.\nDo you want to continue?";
				int dialogResult = JOptionPane.showConfirmDialog(null, message,
						"Warning", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					main.getPreferences().delete();
				}
			}
		});
		p_general.add(b_deleteProperties, "cell 2 6 2 1,growx");

		p_workspace = new JPanel();
		tabbedPane.addTab("Workspace", null, p_workspace, null);

		p_execution = new JPanel();
		tabbedPane.addTab("Job execution", null, p_execution, null);

		p_log = new JPanel();
		tabbedPane.addTab("Logs", null, p_log, null);
		p_log.setLayout(null);

		lblNewLabel_4 = new JLabel("Keep old logs for\r\n");
		lblNewLabel_4.setBounds(10, 11, 87, 14);
		p_log.add(lblNewLabel_4);

		cbx_logDays = new JComboBox();
		cbx_logDays.setEditable(true);
		cbx_logDays.setModel(new DefaultComboBoxModel(new String[] { "0", "1",
				"2", "3", "4", "5", "6", "7", "8", "9", "10", "20", "30" }));
		cbx_logDays.setBounds(99, 8, 42, 20);
		cbx_logDays.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				try {
					int days = Integer.decode((String) cbx_logDays
							.getSelectedItem());
					setPreference("keepOldLogs", days + "");
				} catch (Exception e) {
					setPreference("keepOldLogs", "7");
					Log.getLogger().log(Level.INFO, "set static keepOldLogs");
				}
			}
		});
		cbx_logDays.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO moved to itemstatechanged
			}
		});

		p_log.add(cbx_logDays);

		lblDayskeepForever = new JLabel("days (keep forever, if 0 selected)");
		lblDayskeepForever.setBounds(145, 11, 190, 14);
		p_log.add(lblDayskeepForever);

		b_deleteOldLogs = new JButton("Delete old log files");
		b_deleteOldLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.deleteOldLogs();
			}
		});
		b_deleteOldLogs.setBounds(10, 70, 196, 23);
		p_log.add(b_deleteOldLogs);

		b_openLogFolder = new JButton("Open folder");
		b_openLogFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File(Utilities.getAafcDir()));
				} catch (IOException e) {
					Log.exception(e);
				}
			}
		});
		b_openLogFolder.setBounds(216, 70, 194, 23);
		p_log.add(b_openLogFolder);

		lblSetLogLevel = new JLabel("Set log level:");
		lblSetLogLevel.setBounds(10, 36, 87, 14);
		p_log.add(lblSetLogLevel);

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
				"Log nothing", "SEVERE (highest)", "WARNING", "INFO", "CONFIG",
				"FINE", "FINER", "FINEST (lowest)" }));
		comboBox.setBounds(99, 36, 190, 20);
		p_log.add(comboBox);

		panel = new JPanel();
		panel.setLayout(new MigLayout("",
				"[50.00px:46.00][70px:127.00px][70px:70px,grow]",
				"[][][][][grow]"));
		tabbedPane.addTab("Error reports/Heartbeat", null, panel, null);

		cb_sendHbOnStart = new JCheckBox("Send heartbeat on start");
		cb_sendHbOnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPreference("heartbeatOnStart",
						Boolean.toString(cb_sendHbOnStart.isSelected()));
			}
		});
		cb_sendHbOnStart.setSelected(true);
		panel.add(cb_sendHbOnStart, "cell 0 0 2 1");

		lblAnonymously = new JLabel("(Anonymously, via uid)");
		panel.add(lblAnonymously, "cell 2 0,alignx right");

		cb_sendHbOnNewDevice = new JCheckBox("Send hardbeat on new connection");
		cb_sendHbOnNewDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPreference("heartbeatOnNewDevice",
						Boolean.toString(cb_sendHbOnNewDevice.isSelected()));
				cb_includeSerial.setEnabled(cb_sendHbOnNewDevice.isSelected());
				if (!cb_sendHbOnNewDevice.isSelected()) {
					cb_includeSerial.setSelected(false);
				} else {
					cb_includeSerial.setSelected(Boolean
							.parseBoolean(getPreference("includeSerialInHeartbeat")));
				}
			}
		});
		cb_sendHbOnNewDevice.setSelected(true);
		panel.add(cb_sendHbOnNewDevice, "cell 0 1 2 1");

		lblanonymouslyViaUid = new JLabel("(Hashed or half serial, via uid)");
		panel.add(lblanonymouslyViaUid, "cell 2 1,alignx right");

		cb_includeSerial = new JCheckBox("Include half device serial");
		cb_includeSerial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb_sendHbOnNewDevice.isSelected()) {
					setPreference("includeSerialInHeartbeat",
							Boolean.toString(cb_includeSerial.isSelected()));
				}
			}
		});
		panel.add(cb_includeSerial, "cell 1 2,alignx left,aligny bottom");

		lblprivateDataBlank = new JLabel("(eg HT0C2PXXXXXX and uid)");
		panel.add(lblprivateDataBlank, "cell 2 2,alignx right");

		lblNewLabel_3 = new JLabel("XDA Name, nickname or mail adress");
		panel.add(lblNewLabel_3, "cell 0 3,alignx center,aligny center");

		t_xdaName = new JTextField();
		t_xdaName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setPreference("user", t_xdaName.getText());
			}
		});
		panel.add(t_xdaName, "cell 2 3,growx");
		t_xdaName.setColumns(10);

		scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setValue(0);
		panel.add(scrollPane, "cell 0 4 3 1,growx,aligny bottom");

		txtpnIfYouEnable = new JTextPane();
		txtpnIfYouEnable.setEditable(false);
		scrollPane.setViewportView(txtpnIfYouEnable);

		DefaultCaret caret = (DefaultCaret) txtpnIfYouEnable.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		txtpnIfYouEnable
				.setText("If you enable on-start heartbeats, the tool will send your uid (generated on first start) and your OS to my server. I will never ever collect private data. I just want to know if and how often the tool is used on which machine. \r\nHeartbeat on new connection shows me how many devices you use and how often. Including (half of) the device serial helps me to put focus on most used devices, however, you may turn it off due privacy reasons. If include serial is turned off, a hash of the serial will be sent. If you dont want this, uncheck Send hardbeat on new connection.\r\nIf an error occurs, you will be promted for xda name or mail adress in case i have to contact you. You're able to write the information down here, but you can decide whether to send it to me or not at the moment an error report is showing up.");

		getPreferences();
		pack();
		setVisible(true);
	}

	private void getPreferences() {
		if (Boolean.parseBoolean(getPreference("showDisc"))) {
			cb_showdisc.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("clearOutputOnChange"))) {
			cb_clearOnChange.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("backgroundDeviceSearch"))) {
			cb_backgroundDeviceSearch.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("heartbeatOnNewDevice"))) {
			cb_sendHbOnNewDevice.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("realSdk"))) {
			cb_useRealSdk.setSelected(true);
		}
		String path = getPreference("sdkPath");
		if (!(path.equals("null"))) {
			t_useRealSdk.setText(path);
		}
		if (Boolean.parseBoolean(getPreference("checkForUpdate"))) {
			cb_autoUpate.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("heartbeatOnStart"))) {
			cb_sendHbOnStart.setSelected(true);
		}
		if (Boolean.parseBoolean(getPreference("includeSerialInHeartbeat"))) {
			cb_includeSerial.setSelected(true);
		}
		cbx_logDays.setSelectedItem(getPreference("keepOldLogs"));
		t_xdaName.setText(getPreference("user"));
	}

	private String getPreference(String key) {
		return main.getFromKey(key);
	}

	private void setPreference(String key, String value) {
		main.setFromKey(key, value);
	}
}
