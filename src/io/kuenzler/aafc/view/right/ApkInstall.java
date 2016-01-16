package io.kuenzler.aafc.view.right;

import io.kuenzler.aafc.control.Aafc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("rawtypes")
public class ApkInstall extends Model {
	DefaultListModel<File> listModel;
	private static final long serialVersionUID = 1796520919288274726L;
	private JTextField t_installName;
	private JSeparator separator;
	private JTextField t_uninstallName;
	private JLabel lblApkPackageName;
	private JCheckBox cb_k;
	private JCheckBox cb_unlock;
	private JCheckBox cb_l;
	private JCheckBox cb_r;
	private JCheckBox cb_t;
	private JCheckBox cb_d;
	private JCheckBox cb_p;
	private JCheckBox cb_s;
	private JButton b_uninstall;

	public ApkInstall(final Aafc main) {
		super(main);

		JLabel l_title = new JLabel("Install/Remove APK");
		l_title.setBounds(10, 11, 115, 14);
		add(l_title);

		JButton b_choose = new JButton("Choose APK");
		b_choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String file, fileName, appName;
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".apk", "apk");
				chooser.setFileFilter(filter);
				chooser.setApproveButtonText("Select");
				chooser.showDialog(main.getFrame(), "Select APK to install");
				file = chooser.getSelectedFile().toString();
				if (file.length() > 35) {	
					t_installName.setToolTipText(file);
					file = chooser.getSelectedFile().getParent();
					fileName = chooser.getSelectedFile().getName();
					int offset = 35 - (3 + fileName.length());
					t_installName.setText(file.substring(0, offset) + " ... "
							+ fileName);
				} else {
					t_installName.setText(file);
				}
			}
		});
		b_choose.setBounds(10, 27, 222, 23);
		add(b_choose);

		JButton b_install = new JButton("Install");
		b_install.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File apk;
				boolean flags[] = new boolean[5];
				flags[0] = cb_l.isSelected();
				flags[1] = cb_r.isSelected();
				flags[2] = cb_t.isSelected();
				flags[3] = cb_s.isSelected();
				flags[4] = cb_d.isSelected();
				apk = new File(t_installName.getToolTipText());
				main.getActionCreator().installApk(apk, flags);
			}
		});
		b_install.setBounds(10, 275, 222, 23);
		add(b_install);

		b_uninstall = new JButton("Uninstall");
		b_uninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String apk;
				boolean keep;
				apk = t_uninstallName.getText();
				keep = cb_k.isSelected();
				main.getActionCreator().removeApk(apk, keep);
			}
		});
		b_uninstall.setBounds(10, 390, 222, 23);
		add(b_uninstall);

		cb_l = new JCheckBox("Forward lock application (-l)");
		cb_l.setEnabled(false);
		cb_l.setToolTipText("(-l: forward lock application)");
		cb_l.setBounds(10, 86, 222, 23);
		add(cb_l);

		cb_r = new JCheckBox("Replace existing application (-r)");
		cb_r.setToolTipText("(-r: replace existing application)");
		cb_r.setBounds(10, 112, 222, 23);
		add(cb_r);

		cb_t = new JCheckBox("Allow test packages (-t)");
		cb_t.setEnabled(false);
		cb_t.setToolTipText("(-t: allow test packages)");
		cb_t.setBounds(10, 138, 222, 23);
		add(cb_t);

		cb_s = new JCheckBox("Install application on sdcard (-s)");
		cb_s.setToolTipText("(-s: install application on sdcard)");
		cb_s.setBounds(10, 164, 222, 23);
		add(cb_s);

		cb_d = new JCheckBox("Allow version code downgrade (-d)");
		cb_d.setEnabled(false);
		cb_d.setToolTipText("(-d: allow version code downgrade)");
		cb_d.setBounds(10, 190, 222, 23);
		add(cb_d);

		cb_p = new JCheckBox("Partial application install (-p)");
		cb_p.setEnabled(false);
		cb_p.setToolTipText("(-p: partial application install)");
		cb_p.setBounds(10, 216, 222, 23);
		add(cb_p);

		t_installName = new JTextField();
		t_installName.setEditable(false);
		t_installName.setBounds(10, 56, 222, 23);
		add(t_installName);
		t_installName.setColumns(10);

		separator = new JSeparator();
		separator.setBounds(10, 306, 222, 14);
		add(separator);

		t_uninstallName = new JTextField();
		t_uninstallName.setBounds(10, 331, 222, 23);
		add(t_uninstallName);
		t_uninstallName.setColumns(10);

		lblApkPackageName = new JLabel("APK package name:");
		lblApkPackageName.setBounds(10, 316, 138, 14);
		add(lblApkPackageName);

		cb_k = new JCheckBox("Keep data and cache");
		cb_k.setBounds(10, 360, 222, 23);
		add(cb_k);

		cb_unlock = new JCheckBox("I know what I'm doing");
		cb_unlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				unlock();
			}
		});
		cb_unlock.setToolTipText("(-p: partial application install)");
		cb_unlock.setBounds(10, 246, 222, 23);
		add(cb_unlock);

		setVisible(true);
	}

	private void unlock() {
		boolean unlocked = cb_unlock.isSelected();
		cb_l.setEnabled(unlocked);
		cb_t.setEnabled(unlocked);
		cb_d.setEnabled(unlocked);
		cb_p.setEnabled(unlocked);
	}

	@Override
	public void init() {
		// TODO
	}
}
