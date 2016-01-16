package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Aafc;
import io.kuenzler.aafc.control.CommandHistory;
import io.kuenzler.aafc.control.Executer;
import io.kuenzler.aafc.control.Log;
import io.kuenzler.aafc.control.SdkManager;
import io.kuenzler.aafc.control.Text;
import io.kuenzler.aafc.control.Utilities;
import io.kuenzler.aafc.data.FileDrop;
import io.kuenzler.aafc.view.right.ApkInstall;
import io.kuenzler.aafc.view.right.Backup;
import io.kuenzler.aafc.view.right.Model;
import io.kuenzler.aafc.view.right.Sideload;
import io.kuenzler.aafc.view.right.Start;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

/**
 * Main GUI for aafc with command interface
 * 
 * @author Leonhard Kuenzler
 * @version 1.4.3
 * @date 27.07.15 16:30| refactoring, insert file path
 * @see gui.right.*
 */
public class MainView extends javax.swing.JFrame {

	private static final long serialVersionUID = -5856005806675184791L;

	private String path, orgpath, currentcommand, commandline;
	private HashMap<String, Integer> owncommands;
	private final Aafc main;
	private final CommandHistory comHistory;

	private JMenuBar MB_menu;
	private JMenu M_Adb;
	private JMenu M_Fastboot;
	private JMenu M_Insert;
	private JMenu M_mode;
	private JMenu M_Options;
	private JMenu M_Reboot;
	private JMenu M_start;
	private JMenu M_Sdk;
	private JMenu M_Batch;
	private JMenu M_Misc;
	private JMenu M_Install;
	private JMenu M_blank1;
	private JMenu M_blank2;
	private JMenu M_blank3;
	private JMenu M_blank4;
	private JMenu M_blank5;
	private JMenuItem MI_about;
	private JMenuItem MI_addFilePath;
	private JMenuItem MI_addFlashErase;
	private JMenuItem MI_addPushPull;
	private JMenuItem MI_bootAndroid;
	private JMenuItem MI_bootBootloader;
	private JMenuItem MI_bootRecovery;
	private JMenuItem MI_chooseADB;
	private JMenuItem MI_chooseCMD;
	private JMenuItem MI_chooseFastboot;
	private JMenuItem MI_close;
	private JMenuItem MI_editSdkPath;
	private JMenuItem MI_howTo;
	private JMenuItem MI_installDrivers;
	private JMenuItem MI_logcat;
	private JMenuItem MI_sideload;
	private JMenuItem MI_saveLog;
	private JMenuItem MI_sdkManager;
	private JMenuItem MI_avdManager;
	private JMenuItem MI_deviceMonitor;
	private JMenuItem MI_startAvd;
	private JMenuItem MI_showStartMsg;
	private JMenuItem MI_installApk;
	private JMenuItem MI_openSdkExplorer;
	private JMenuItem MI_backup;
	private JMenuItem MI_changeWorkspace;
	private JMenuItem MI_installSuperSU;
	private JMenuItem MI_installXposed;
	private JMenuItem MI_checkMD5;
	private JMenuItem MI_resetBatterystats;
	private JCheckBoxMenuItem CBMI_clear;
	private JCheckBoxMenuItem CBMI_autoLog;
	private JButton b_checkForDevice;
	private JButton b_clearOutput;
	private JButton b_executeCommand;
	private JButton b_exit;
	private JButton b_killAdb;
	private JButton b_killFastboot;
	private JButton b_reset;
	private JButton b_saveLog;
	private JScrollPane jScrollPane1;
	private JPopupMenu.Separator MI_blank1;
	private JPopupMenu.Separator MI_blank2;
	private JSeparator MI_blank4;
	private JSeparator MI_blank3;
	private JLabel l_device;
	private JLabel l_from;
	private JLabel l_working;
	private JLabel l_currentmode;
	private JLabel l_sdk;
	private JTextField t_command;
	private JTextArea ta_cmd;
	private JPanel p_right;
	private JCheckBoxMenuItem cbmi_autoModeChange;
	private JMenuItem MI_recordScreen;
	private JTextField t_serial;
	private JLabel l_serial;
	private JMenu MI_deviceDrivers;
	private JMenuItem MI_installUniversalDriver;
	private JMenuItem MI_preferences;
	private JSeparator separator;
	private JMenuItem MI_openOsShell;
	private JMenuItem MI_checkUpdate;
	private JMenu m_devices;
	private JMenu m_oem;
	private JCheckBoxMenuItem micb_autoDeviceDetect;
	private JLabel l_devName;
	private JLabel l_devBattery;
	private JLabel l_devVendor;
	private JMenuItem MI_exportMiniSdk;

	/**
	 * Creates new Mainframe
	 * 
	 * @param main
	 *            the main control object
	 * @param version
	 *            the current version as string
	 */
	public MainView(final Aafc main, String version) {
		this.main = main;
		setTitle("Android ADB Fastboot Commander" + " " + version); // TODO

		initComponents();

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainView.class.getResource("/io/kuenzler/aafc/res/icon.jpeg")));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth()) / 2,
				(dim.height - getHeight()) / 2);

		commandline = currentcommand = "";
		owncommands = Text.getOwnCommands();
		comHistory = new CommandHistory();

		// TODO path is no longer relevant here
		path = orgpath = new File(System.getProperty("java.io.tmpdir"))
				.getAbsolutePath().toString() + "\\";

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				main.exit();
			}
		});

		ta_cmd.setText("");
		ta_cmd.setLineWrap(true); // Zeilenumbruch ja

		l_device = new JLabel();
		l_device.setText("No device");

		l_currentmode = new JLabel("");

		l_sdk = new JLabel("SDK Mngr not running...");
		l_sdk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				main.openSdkLocation();
			}
		});

		p_right = new JPanel();
		p_right.setLayout(null);
		p_right.add(new Start(main));

		b_reset = new javax.swing.JButton();
		b_reset.setText("Reset all");
		b_reset.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_resetActionPerformed(evt);
			}
		});

		b_killAdb = new javax.swing.JButton();
		b_killAdb.setText("Kill ADB");
		b_killAdb.setToolTipText("Kills adb server");
		b_killAdb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_killadbActionPerformed(evt);
			}
		});

		b_exit = new javax.swing.JButton();
		b_exit.setText("Exit");
		b_exit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_exitActionPerformed(evt);
			}
		});

		l_from = new javax.swing.JLabel();
		l_from.setText("Leonhard Kuenzler, 2015");

		t_serial = new JTextField();
		t_serial.setEditable(false);
		t_serial.setText("No device");
		getContentPane()
				.setLayout(
						new MigLayout(
								"",
								"[110px:120px:120px][120.00px][:120.00px:120px][120px:120.00px,grow][164.00px,grow][120.00px][120.00px]",
								"[23px][][424.00px,grow][23.00px,top][23px,fill]"));

		getContentPane().add(t_serial, "cell 3 0,growx");
		t_serial.setColumns(10);

		getContentPane().add(l_sdk, "cell 5 0");

		l_devName = new JLabel("l_devName");
		l_devName.setToolTipText("Device Name");
		getContentPane().add(l_devName, "cell 0 1,alignx left");

		l_devBattery = new JLabel("l_devBattery");
		l_devBattery.setToolTipText("Battery Level");
		getContentPane().add(l_devBattery, "cell 1 1,grow");

		l_devVendor = new JLabel("l_devVendor");
		l_devVendor.setToolTipText("Device Vendor");
		getContentPane().add(l_devVendor, "cell 2 1,grow");
		getContentPane().add(p_right, "cell 5 2 2 1,grow");

		l_serial = new JLabel("Connected device:");
		getContentPane().add(l_serial, "flowx,cell 2 0");
		getContentPane().add(l_currentmode, "cell 2 0,alignx trailing");
		getContentPane().add(b_reset, "cell 5 3,growx,aligny top");
		getContentPane().add(b_killAdb, "cell 6 3,growx,aligny top");
		getContentPane().add(l_from, "cell 4 4,alignx center,aligny top");
		getContentPane().add(b_exit, "cell 5 4,growx,aligny top");
		getContentPane().add(l_working, "cell 0 4,growx,aligny center");
		getContentPane().add(b_executeCommand, "cell 1 4,growx,aligny top");
		getContentPane().add(b_clearOutput, "cell 2 4,growx,aligny top");
		getContentPane().add(b_saveLog, "cell 3 4,growx,aligny top");
		getContentPane().add(t_command, "cell 0 3 5 1,grow");
		getContentPane().add(b_checkForDevice, "cell 0 0,growx,aligny top");
		getContentPane().add(jScrollPane1, "cell 0 2 5 1,grow");
		getContentPane().add(b_killFastboot, "cell 6 4,growx,aligny top");
		getContentPane().add(l_device, "cell 1 0,growx,aligny center");
		pack();

		Log.getLogger().log(Level.INFO, "gui here:p"); // TODO delete
	}

	private void initComponents() {
		t_command = new javax.swing.JTextField();
		t_command.setForeground(Color.GRAY);
		t_command.setText("Type your commands here - hit enter to run");
		t_command.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				t_commandKeyPressed(evt);
			}
		});
		t_command.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (t_command.getText().equals("")) {
					t_command.setForeground(Color.GRAY);
					t_command
							.setText("Type your commands here - hit enter to run");
				}
			}

			public void focusGained(FocusEvent arg0) {
				if (t_command.getForeground() == Color.GRAY) {
					t_command.setText("");
					t_command.setForeground(Color.BLACK);
				}
			}
		});
		new FileDrop(t_command, new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				insertPath(files[0]);
			}
		});

		jScrollPane1 = new javax.swing.JScrollPane();
		ta_cmd = new javax.swing.JTextArea();
		ta_cmd.setFont(new Font("Courier New", Font.PLAIN, 13));
		b_executeCommand = new javax.swing.JButton();
		b_clearOutput = new javax.swing.JButton();
		b_saveLog = new javax.swing.JButton();
		l_working = new javax.swing.JLabel();
		b_checkForDevice = new javax.swing.JButton();
		b_killFastboot = new javax.swing.JButton();
		MB_menu = new javax.swing.JMenuBar();
		M_start = new javax.swing.JMenu();
		MI_howTo = new javax.swing.JMenuItem();
		MI_about = new javax.swing.JMenuItem();
		MI_close = new javax.swing.JMenuItem();
		M_mode = new javax.swing.JMenu();
		MI_chooseADB = new javax.swing.JMenuItem();
		MI_chooseADB.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				InputEvent.ALT_MASK));
		MI_chooseFastboot = new javax.swing.JMenuItem();
		MI_chooseFastboot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				InputEvent.ALT_MASK));
		MI_chooseCMD = new javax.swing.JMenuItem();
		MI_chooseCMD.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				InputEvent.ALT_MASK));
		MI_blank1 = new javax.swing.JPopupMenu.Separator();
		MI_blank2 = new javax.swing.JPopupMenu.Separator();
		CBMI_clear = new javax.swing.JCheckBoxMenuItem();
		M_Options = new javax.swing.JMenu();
		CBMI_autoLog = new javax.swing.JCheckBoxMenuItem();
		M_Insert = new javax.swing.JMenu();
		MI_addFilePath = new javax.swing.JMenuItem();
		M_blank1 = new javax.swing.JMenu();
		M_blank1.setEnabled(false);
		M_Reboot = new javax.swing.JMenu();
		MI_bootAndroid = new javax.swing.JMenuItem();
		MI_bootBootloader = new javax.swing.JMenuItem();
		MI_bootRecovery = new javax.swing.JMenuItem();
		M_blank2 = new javax.swing.JMenu();
		M_blank2.setEnabled(false);
		M_Adb = new javax.swing.JMenu();
		MI_logcat = new javax.swing.JMenuItem();
		MI_logcat.setEnabled(false);
		M_Fastboot = new javax.swing.JMenu();

		setLocationByPlatform(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setBackground(new java.awt.Color(51, 0, 255));
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setForeground(new java.awt.Color(0, 153, 255));
		// TODOsetLocationByPlatform(true);
		setResizable(true);

		ta_cmd.setEditable(false);
		ta_cmd.setColumns(20);
		ta_cmd.setRows(5);
		// ta_cmd.setToolTipText("Cmd output");
		jScrollPane1.setViewportView(ta_cmd);

		b_executeCommand.setText("Run!");
		b_executeCommand.setToolTipText("Run typed command");
		b_executeCommand.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_executecommandActionPerformed(evt);
			}
		});

		b_clearOutput.setText("Clear");
		b_clearOutput.setToolTipText("Clear cmd output");
		b_clearOutput.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_clearActionPerformed(evt);

				Log.getLogger().log(Level.INFO, "clear here:p"); // TODO delete
			}
		});

		b_saveLog.setText("Save log");
		b_saveLog.setToolTipText("Save cmd output to file");
		b_saveLog.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_savelogActionPerformed(evt);
			}
		});

		l_working.setText("Ready");
		l_working.setToolTipText("If ready, last command has finished");

		b_checkForDevice.setText("Stop auto-detect");
		b_checkForDevice.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_checkfordeviceActionPerformed(evt);
			}
		});

		b_killFastboot.setText("Kill Fastboot");
		b_killFastboot.setToolTipText("kills fastboot thread");
		b_killFastboot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_killfbActionPerformed(evt);
			}
		});

		MB_menu.setBackground(new java.awt.Color(255, 255, 255));
		MB_menu.setBorder(javax.swing.BorderFactory.createEtchedBorder(
				java.awt.Color.lightGray, null));
		MB_menu.setForeground(new java.awt.Color(153, 204, 255));

		M_start.setText("Start");

		MI_howTo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F1,
				java.awt.event.InputEvent.CTRL_MASK));
		MI_howTo.setText("How to");
		MI_howTo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_howToActionPerformed(evt);
			}
		});

		MI_saveLog = new JMenuItem("Save log");
		MI_saveLog.setEnabled(false);
		MI_saveLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		M_start.add(MI_saveLog);
		M_start.add(MI_howTo);

		MI_about.setText("About");
		MI_about.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_aboutActionPerformed(evt);
			}
		});

		MI_showStartMsg = new JMenuItem("Show welcome message");
		MI_showStartMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeRight(Model.START);
			}
		});
		M_start.add(MI_showStartMsg);
		M_start.add(MI_about);

		MI_close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F4,
				java.awt.event.InputEvent.ALT_MASK));
		MI_close.setText("Exit");
		MI_close.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_closeActionPerformed(evt);
			}
		});
		M_start.add(MI_close);

		MB_menu.add(M_start);

		M_mode.setText("Mode");
		M_mode.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				M_modeActionPerformed(evt);
			}
		});

		MI_chooseADB.setText("ADB");
		MI_chooseADB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_chooseADBActionPerformed(evt);
			}
		});
		M_mode.add(MI_chooseADB);

		MI_chooseFastboot.setText("Fastboot");
		MI_chooseFastboot
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						MI_chooseFastbootActionPerformed(evt);
					}
				});
		M_mode.add(MI_chooseFastboot);

		MI_chooseCMD.setText("CMD");
		MI_chooseCMD.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_chooseCMDActionPerformed(evt);
			}
		});
		M_mode.add(MI_chooseCMD);
		M_mode.add(MI_blank1);

		cbmi_autoModeChange = new JCheckBoxMenuItem("Autochange");
		cbmi_autoModeChange.setSelected(true);
		M_mode.add(cbmi_autoModeChange);
		M_mode.add(MI_blank2);
		CBMI_clear.setText("Clear window on change");
		M_mode.add(CBMI_clear);

		MB_menu.add(M_mode);

		M_Options.setText("Options");

		MI_deviceDrivers = new JMenu("Device Drivers");
		M_Options.add(MI_deviceDrivers);
		MI_installDrivers = new javax.swing.JMenuItem();
		MI_deviceDrivers.add(MI_installDrivers);

		MI_installDrivers.setText("Search for driver");

		MI_installUniversalDriver = new JMenuItem("Install universal driver");
		MI_installUniversalDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO change to downlaod & start
				try {
					Desktop.getDesktop()
							.browse(new URI(
									"http://download.clockworkmod.com/test/UniversalAdbDriverSetup.msi"));
				} catch (IOException | URISyntaxException e) {
					Log.exception(e);
				}
			}
		});
		MI_deviceDrivers.add(MI_installUniversalDriver);

		MI_installDrivers
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						MI_installDriversActionPerformed(evt);
					}
				});

		MI_changeWorkspace = new JMenuItem("Change workspace");
		MI_changeWorkspace.setEnabled(false);
		M_Options.add(MI_changeWorkspace);
		CBMI_autoLog.setText("Auto-log output");
		M_Options.add(CBMI_autoLog);

		separator = new JSeparator();
		M_Options.add(separator);

		MI_preferences = new JMenuItem("Preferences");
		MI_preferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new io.kuenzler.aafc.view.Preferences(main);
			}
		});

		MI_checkUpdate = new JMenuItem("Check for updates");
		M_Options.add(MI_checkUpdate);
		MI_checkUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.checkUpdate(false);
			}
		});
		M_Options.add(MI_preferences);

		MB_menu.add(M_Options);

		M_Insert.setText("Insert");

		MI_addFilePath.setText("Filepath");
		MI_addFilePath.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_addFilePathActionPerformed(evt);
			}
		});
		M_Insert.add(MI_addFilePath);

		MB_menu.add(M_Insert);

		M_blank1.setText("|");
		MB_menu.add(M_blank1);

		M_Reboot.setText("Reboot");

		MI_bootAndroid.setText("System");
		MI_bootAndroid.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_bootAndroidActionPerformed(evt);
			}
		});
		M_Reboot.add(MI_bootAndroid);

		MI_bootBootloader.setText("Bootloader");
		MI_bootBootloader
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						MI_bootBootloaderActionPerformed(evt);
					}
				});
		M_Reboot.add(MI_bootBootloader);

		MI_bootRecovery.setText("Recovery");
		MI_bootRecovery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_bootRecoveryActionPerformed(evt);
			}
		});
		M_Reboot.add(MI_bootRecovery);

		MB_menu.add(M_Reboot);

		M_blank2.setText("|");
		MB_menu.add(M_blank2);

		M_Adb.setText("ADB");

		MI_logcat.setText("Logcat");
		MI_logcat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_logcatActionPerformed(evt);
			}
		});

		M_Misc = new JMenu("Misc");
		MB_menu.add(M_Misc);

		MI_checkMD5 = new JMenuItem("Checksum Calc");
		MI_checkMD5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new io.kuenzler.aafc.view.MD5check();
			}
		});
		M_Misc.add(MI_checkMD5);

		MI_backup = new JMenuItem("Backup/Restore");
		M_Misc.add(MI_backup);

		MI_resetBatterystats = new JMenuItem("Reset Batterystats");
		MI_resetBatterystats.setEnabled(false);
		M_Misc.add(MI_resetBatterystats);

		MI_recordScreen = new JMenuItem("Record Device Screen");
		MI_recordScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new io.kuenzler.aafc.control.RecordScreen(main);
			}
		});
		M_Misc.add(MI_recordScreen);
		MI_backup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeRight(Model.BACKUP);
			}
		});

		M_Install = new JMenu("Install");
		MB_menu.add(M_Install);

		MI_installApk = new JMenuItem("APK");
		M_Install.add(MI_installApk);

		MI_installSuperSU = new JMenuItem("Chainfire SuperSU");
		MI_installSuperSU.setEnabled(false);
		M_Install.add(MI_installSuperSU);

		MI_installXposed = new JMenuItem("Xposed Framework");
		MI_installXposed.setEnabled(false);
		M_Install.add(MI_installXposed);

		MI_installApk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeRight(Model.APKINSTALL);
			}
		});

		M_blank3 = new JMenu("|");
		M_blank3.setEnabled(false);
		MB_menu.add(M_blank3);
		M_Adb.add(MI_logcat);

		MB_menu.add(M_Adb);

		MI_addPushPull = new javax.swing.JMenuItem();
		M_Adb.add(MI_addPushPull);
		MI_addPushPull.setText("Push/Pull file");

		MI_sideload = new JMenuItem("Sideload");
		MI_sideload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeRight(Model.SIDELOAD);
			}
		});
		M_Adb.add(MI_sideload);

		MI_addPushPull.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_addPushPullActionPerformed(evt);
			}
		});

		M_Fastboot.setText("Fastboot");
		MB_menu.add(M_Fastboot);
		MI_addFlashErase = new javax.swing.JMenuItem();
		M_Fastboot.add(MI_addFlashErase);

		MI_addFlashErase.setText("Flash/erase partition");
		MI_addFlashErase.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_addFlashEraseActionPerformed(evt);
			}
		});

		setJMenuBar(MB_menu);

		M_blank4 = new JMenu("|");
		M_blank4.setEnabled(false);
		MB_menu.add(M_blank4);

		m_oem = new JMenu("OEM");
		m_oem.setEnabled(false);
		MB_menu.add(m_oem);

		m_devices = new JMenu("Devices");
		MB_menu.add(m_devices);

		micb_autoDeviceDetect = new JCheckBoxMenuItem("Auto device detection");
		micb_autoDeviceDetect.setSelected(true);
		micb_autoDeviceDetect.setEnabled(false);
		m_devices.add(micb_autoDeviceDetect);

		M_blank5 = new JMenu();
		M_blank5.setText("|");
		M_blank5.setEnabled(false);
		MB_menu.add(M_blank5);

		M_Batch = new JMenu("Batch");
		M_Batch.setEnabled(false);
		MB_menu.add(M_Batch);

		M_Sdk = new JMenu("SDK");
		MB_menu.add(M_Sdk);
		MI_editSdkPath = new javax.swing.JMenuItem();
		M_Sdk.add(MI_editSdkPath);

		MI_editSdkPath.setText("Change Path");
		MI_editSdkPath.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MI_editPathActionPerformed(evt);
			}
		});

		MI_openSdkExplorer = new JMenuItem("Open in Explorer");
		MI_openSdkExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.openSdkLocation();
			}
		});
		M_Sdk.add(MI_openSdkExplorer);

		MI_openOsShell = new JMenuItem("Open OS Shell");
		MI_openOsShell.setEnabled(false);
		MI_openOsShell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Runtime.getRuntime().exec(
							"cmd.exe " + Utilities.getFileSep() + "K");
				} catch (IOException e) {
					Log.exception(e);
				}
			}
		});
		
		MI_exportMiniSdk = new JMenuItem("Export MiniSDK to Zip");
		MI_exportMiniSdk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.exportMiniSdk();
			}
		});
		M_Sdk.add(MI_exportMiniSdk);
		M_Sdk.add(MI_openOsShell);

		MI_blank3 = new JSeparator();
		M_Sdk.add(MI_blank3);

		MI_sdkManager = new JMenuItem("SDK Manager");
		MI_sdkManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.startSdk(SdkManager.SDKMANAGER);
			}
		});
		M_Sdk.add(MI_sdkManager);

		MI_avdManager = new JMenuItem("AVD Manager");
		MI_avdManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.startSdk(SdkManager.AVDMANAGER);
			}
		});
		M_Sdk.add(MI_avdManager);

		MI_deviceMonitor = new JMenuItem("DeviceMonitor");
		MI_deviceMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.startSdk(SdkManager.DEVICEMONITOR);
			}
		});
		M_Sdk.add(MI_deviceMonitor);

		MI_blank4 = new JSeparator();
		M_Sdk.add(MI_blank4);

		MI_startAvd = new JMenuItem("Start AVD");
		MI_startAvd.setEnabled(false);
		MI_startAvd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.startAvd();
			}
		});
		M_Sdk.add(MI_startAvd);
		pack();
		setSize(new Dimension(1028, 628));
	}

	private void MI_closeActionPerformed(ActionEvent evt) {
		main.exit();
	}

	private void MI_chooseADBActionPerformed(java.awt.event.ActionEvent evt) {
		main.setMode(Executer.ADB);
	}

	/**
	 * 
	 * @param serial
	 */
	public void setDeviceSerial(String serial) {
		t_serial.setText(serial);
	}
	

	private void b_executecommandActionPerformed(java.awt.event.ActionEvent evt) {
		main.setWorking(true);
		currentcommand = t_command.getText();
		currentcommand = currentcommand.trim();
		t_command.setText("");
		if (currentcommand.equals("")) {
			updateCommandline("--type command before entering");
			main.setWorking(false);
			return;
		}
		comHistory.addCommand(currentcommand);
		if (owncommands.containsKey(currentcommand)) {
			updateCommandline(">> " + currentcommand);
			switch ((int) owncommands.get(currentcommand)) {
			case 1: {
				commandline = "";
				ta_cmd.setText(commandline);
				break;
			}
			case 2: {
				main.exit();
				break;
			}
			case 3: {
				MI_chooseFastboot.doClick(); // TODO change to main.setMode()
				break;
			}
			case 4: {
				MI_chooseADB.doClick(); // TODO same
				break;
			}
			case 5: {
				MI_chooseCMD.doClick(); // TODO same
				break;
			}
			case 6: {
				updateCommandline("--help--"); // TODO help maybe
				break;
			}
			case 7: {
				updateCommandline("--commands:\n   clear\n   exit\n   "
						+ "mode [fastboot|adb|cmd]\n   helpme\n   reset path"
						+ "\n   about\n   commandlist");
				break;
			}
			case 8: {
				path = orgpath;
				updateCommandline("Path changed to " + path);
				break;
			}
			case 9: {
				MI_about.doClick(); // TODO change
				break;
			}
			case 10: {
				main.message("Happy Easter", "Hallo Anselm",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}
			case 11: {
				System.exit(0);
			}

			}
			main.setWorking(false);
		} else if (currentcommand.contains("message ")) {
			main.message("Info", currentcommand.substring(8),
					JOptionPane.INFORMATION_MESSAGE);
			main.setWorking(false);
		} else if (main.getMode() == 0) {
			updateCommandline(">-" + currentcommand);
			updateCommandline("--Select ADB, Fastboot or CMD before entering commands!");
			main.setWorking(false);
		} else {
			commandline += ">" + currentcommand;
			ta_cmd.setText(commandline);
			main.runCommand(currentcommand);
		}
	}// GEN-LAST:event_b_executecommandActionPerformed

	/**
	 * 
	 * @param evt
	 */
	private void MI_chooseFastbootActionPerformed(java.awt.event.ActionEvent evt) {
		main.setMode(Executer.FASTBOOT);
	}

	public void setDevName(String name) {
		if (name.length() > 20) {
			l_devName.setText("<not given>");
		} else {
			l_devName.setText(name);
		}
	}

	public void setDevVendor(String vendor) {
		if (vendor.length() > 20) {
			l_devVendor.setText("<not given>");
		} else {
			l_devVendor.setText(vendor);
		}
	}

	public void setDevBattery(int battery) {
		if (0 <= battery && battery <= 100) {
			l_devBattery.setText(battery + "%");
		} else {
			l_devBattery.setText("<not given>");
		}

	}

	/**
	 * 
	 * @param evt
	 */
	private void b_savelogActionPerformed(java.awt.event.ActionEvent evt) {
		Writer fw = null;
		File file = main
				.chooseFile("Save cmd output", false, "Save", "log.txt");
		if (file != null) {
			try {
				fw = new FileWriter(file);
				fw.write(ta_cmd.getText());
				fw.append(System.getProperty("line.separator")); // e.g. "\n"
				ta_cmd.setText("Log saved as " + file.toString()
						+ ", ready to clear");
			} catch (IOException e) {
				main.updateCommandline("--Cannot save log (" + e.toString()
						+ ")");
				Log.exception(e);
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						Log.exception(e);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param evt
	 */
	private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {
		// sicherheitsabfrage
		commandline = "";
		ta_cmd.setText(commandline);
		t_command.setText(commandline);
	}

	/**
	 * 
	 * @param evt
	 */
	private void M_modeActionPerformed(java.awt.event.ActionEvent evt) {
	}

	/**
	 * 
	 * @param evt
	 */
	private void MI_chooseCMDActionPerformed(java.awt.event.ActionEvent evt) {
		main.setMode(Executer.CMD);
	}

	public void setDeviceStatus(String status) {
		l_device.setText(status);
	}

	/**
	 * 
	 * @param evt
	 */
	private void t_commandKeyPressed(java.awt.event.KeyEvent evt) {
		int code = evt.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			b_executeCommand.doClick();
		} else {
			String command = "";
			if (code == KeyEvent.VK_UP) {
				command = comHistory.getPrevCommand();
				// older history
			} else if (code == KeyEvent.VK_DOWN) {
				command = comHistory.getPostCommand();
				// newer history
			} else {
				// no matching key
				return;
			}
			t_command.setText(command);
		}
	}

	private void b_checkfordeviceActionPerformed(java.awt.event.ActionEvent evt) {
		b_checkForDevice.setEnabled(false);
		boolean running = main.checkDevices();
		String buttonText;
		if (running) {
			buttonText = "Stop auto-detect";
		} else {
			buttonText = "Start auto-detect";
		}
		b_checkForDevice.setText(buttonText);
		b_checkForDevice.setEnabled(true);
	}

	private void MI_editPathActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO
		main.changeSdkPath();
	}

	private void MI_installDriversActionPerformed(java.awt.event.ActionEvent evt) {
		// http://clockworkmod.com/carbon/drivers contains driver collections
		// http://developer.android.com/tools/extras/oem-usb.html#Drivers
		// contains all links
		main.runCommand(
				"start http://developer.android.com/tools/extras/oem-usb.html#Drivers",
				3, false, false);
	}

	private void b_exitActionPerformed(java.awt.event.ActionEvent evt) {
		main.exit();
	}

	private void MI_aboutActionPerformed(java.awt.event.ActionEvent evt) {
		main.message("About", Text.getAbout(), JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 
	 * @param evt
	 */
	private void b_killadbActionPerformed(java.awt.event.ActionEvent evt) {
		updateCommandline("--restarting adb");
		main.kill(1);
		main.runCommand("start-server", 1, false, false);
	}

	/**
	 * 
	 * @param evt
	 */
	private void b_killfbActionPerformed(java.awt.event.ActionEvent evt) {
		main.kill(2);
	}

	/**
	 * 
	 * @param evt
	 */
	private void b_resetActionPerformed(java.awt.event.ActionEvent evt) {
		main.reset();
	}

	/**
	 * 
	 * @param evt
	 */
	private void MI_bootAndroidActionPerformed(java.awt.event.ActionEvent evt) {
		// Reboot into OS
		main.reboot(0);
		/*
		 * int currentMode = main.getMode(); //TODO delete old code if
		 * (currentMode == 1 || currentMode == 2) { main.runCommand("reboot",
		 * currentMode, true, true); if (currentMode == 2) { //change to adb
		 * mode main.setMode(Executer.ADB); } main.runCommand("wait-for-device",
		 * 1, true, true); } else {
		 * updateCommandline("--Reboot only works in ADB/Fastboot mode"); }
		 */
	}

	/**
	 * 
	 * @param evt
	 */
	private void MI_bootBootloaderActionPerformed(java.awt.event.ActionEvent evt) {
		// Reboot into bootloader
		main.reboot(1);
		/*
		 * int cmode = main.getMode(); //TODO delete old code if (cmode == 1 ||
		 * cmode == 2) { // Run the reboot-bootloader command (works on adb &
		 * fb) // fastboot/adb reboot-bootlader or adb reboot bootloader
		 * main.runCommand("reboot-bootloader", cmode, true, true); if (cmode ==
		 * 1) { //change to fastboot mode main.setMode(Executer.FASTBOOT); } }
		 * else { updateCommandline("--Reboot only works in ADB/Fastboot mode");
		 * }
		 */
	}

	/**
	 * 
	 * @param evt
	 */
	private void MI_bootRecoveryActionPerformed(java.awt.event.ActionEvent evt) {
		main.reboot(2);
		/*
		 * int cmode = main.getMode(); //TODO delete old code if (cmode == 1) {
		 * main.runCommand("reboot recovery", cmode, true, true); } else if
		 * (cmode == 2) {
		 * updateCommandline("--Booting to recovery only works in ADB mode\n" +
		 * "--You may boot into bootloader"); } else {
		 * updateCommandline("--Reboot only works in ADB/Fastboot mode"); }
		 */
	}

	private void MI_addFilePathActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser = new JFileChooser(main.getJarPath());
		chooser.setDialogTitle("Choose file to insert");
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setApproveButtonText("Insert file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			insertPath(chooser.getSelectedFile());
		} else {
			// TODO log here
		}
	}

	private void MI_addFlashEraseActionPerformed(java.awt.event.ActionEvent evt) {
		FlashEraseFormat commandbuilder;
		commandbuilder = new FlashEraseFormat(main);
		commandbuilder.setVisible(true);
	}

	private void MI_howToActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			Desktop.getDesktop()
					.browse(new URI(
							"http://forum.xda-developers.com/android/development/tool-aafc-adb-fastboot-commander-t3126676"));
		} catch (IOException | URISyntaxException e) {
			Log.exception(e);
		}
	}

	private void MI_addPushPullActionPerformed(java.awt.event.ActionEvent evt) {
		new io.kuenzler.aafc.view.ADBPushPull(main).setVisible(true);
		// TODO right
	}

	private void MI_logcatActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO new Logcat(main, main.getJarPath()).setVisible(true);
		main.message("Locat temporary disabled.\nBetter user DeviceMonitor(SDK) for proper output.");
	}

	/**
	 * Inserts file path at cursor position to t_command
	 * 
	 * @param file
	 *            the file to use
	 */
	private void insertPath(File file) {
		File f = file;
		if (f.exists()) {
			int position = t_command.getCaretPosition();
			// TODO get real cursor position
			String oldCommand, newCommand, oldPre, oldPost;
			if (t_command.getForeground().equals(Color.GRAY)) {
				newCommand = f.toString();
				t_command.setForeground(Color.BLACK);
			} else {
				oldCommand = t_command.getText();
				oldPre = oldCommand.substring(0, position);
				oldPost = oldCommand.substring(position, oldCommand.length());
				if (oldPre.endsWith(" ")) {
					newCommand = oldPre;
				} else {
					newCommand = oldPre + " ";
				}
				newCommand += f.toString();
				if (oldPost.startsWith(" ")) {
					newCommand += oldPost;
				} else {
					newCommand += " " + oldPost;
				}
				newCommand = newCommand.trim();
			}
			t_command.setText(newCommand);
		} else {
			main.error("File " + f.toString() + "does not exist");
		}
	}

	public void updateCommandline(String update, boolean newline) {
		if (commandline.equals("")) {
			commandline = update;
		} else {
			if (newline) {
				commandline += "\n" + update;
			} else {
				commandline += update;
			}
		}
		commandline = commandline.replace("\n\n\n", "\n");
		commandline = commandline.replace("\n\n", "\n");
		ta_cmd.setText(commandline);
		ta_cmd.setCaretPosition(ta_cmd.getDocument().getLength());
		System.out.println(update);
	}

	public void updateCommandline(String update) {
		updateCommandline(update, true);
	}

	public void windowClosing(WindowEvent e) {
		main.exit();
	}

	/**
	 * Sets the GUI to working or ready state
	 * 
	 * @param working
	 *            true:working, false:ready
	 */
	public void setWorking(boolean working) {
		if (working) {
			l_working.setText("-Busy-");
		} else {
			l_working.setText("Ready");
		}
	}

	/**
	 * Changes the right panel
	 * 
	 * @param model
	 *            the panel to view in right space
	 */
	private void changeRight(int model) {
		p_right.removeAll();
		switch (model) {
		case Model.SIDELOAD:
			p_right.add(new Sideload(main));
			break;
		case Model.APKINSTALL:
			p_right.add(new ApkInstall(main));
			break;
		case Model.BACKUP:
			p_right.add(new Backup(main));
			break;
		case Model.START:
		default:
			p_right.add(new Start(main));
		}
		getContentPane().add(p_right, "cell 5 1 2 1,grow");
		repaint();
	}

	/**
	 * 
	 * @param model
	 * @see view.right.Model
	 * 
	 *      private void changeRight(int model) {
	 *      getContentPane().remove(p_right); p_right.removeAll(); switch
	 *      (model) { case Model.SIDELOAD: p_right = new Sideload(main); break;
	 *      case Model.APKINSTALL: p_right = new ApkInstall(main); break; case
	 *      Model.BACKUP: p_right = new Backup(main); break; case Model.START:
	 *      default: p_right = new Start(main); } getContentPane().add(p_right,
	 *      "cell 5 1 2 1,grow"); repaint(); }
	 */

	/**
	 * If CBMI_clear is ticked, cmd_output window will be cleared
	 */
	public void clearOnChange() {
		if (CBMI_clear.getState()) {
			commandline = "";
			ta_cmd.setText("");
			// t_command.setText(""); executeline leeren
		}
	}

	/**
	 * Returns the current path for exefiles
	 * 
	 * @return path of exefiles(temp dir) as string
	 */
	public String getPath() {
		return path;
	}

	public void updateExecuteline(String update, boolean clear) {
		if (clear) {
			t_command.setText(update);
		} else {
			t_command.setText(t_command.getText() + " " + update);
		}
	}

	public void updateBatch(String update) {
		main.message("batch moved to batch.java"); // TODO to scripting
	}

	public void setMode(int mode) {
		clearOnChange();
		switch (mode) {
		case Executer.ADB: {
			updateCommandline("--ADB Mode");
			l_currentmode.setText("ADB");
			main.runCommand("start-server", 1, false, false);
			main.runCommand("version", 1, false, true);
			break;
		}
		case Executer.FASTBOOT: {
			updateCommandline("--Fastboot Mode\nif no device is connected"
					+ " in fastboot mode,\nfastboot will wait for device "
					+ "and block\n");
			l_currentmode.setText("Fastboot");
			break;
		}
		case Executer.CMD: {
			updateCommandline("--CMD Mode");
			l_currentmode.setText("CMD");
			break;
		}
		default: {
			main.updateCommandline("--Wrong input at setMode in class Frame"
					+ " (" + mode + ")");
		}
		}
	}

	public void setSdkWorking(boolean working, boolean realSdk) {
		l_sdk.setToolTipText("SDK path: " + main.getSdkPath());
		if (working && realSdk) {
			l_sdk.setBackground(Color.GREEN);
			l_sdk.setText("Using Android SDK");
		} else if (working && !realSdk) {
			l_sdk.setBackground(Color.GREEN);
			l_sdk.setText("Using internal ADB/Fastboot");
		} else {
			l_sdk.setBackground(Color.RED);
			l_sdk.setText("ERROR: SDK not working");
		}
	}

	public boolean getAutoChange() {
		return cbmi_autoModeChange.isSelected();
	}

	public Process editBeforeExecuting(String command, int mode, boolean wait,
			boolean show) {
		if (true) {
			return main.runCommand(command, mode, wait, show);
		} else {
			t_command.setText(command); // TODO solution...
		}
		return null;
	}
}
