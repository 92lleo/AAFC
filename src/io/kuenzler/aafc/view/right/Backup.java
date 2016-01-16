package io.kuenzler.aafc.view.right;

import io.kuenzler.aafc.control.Aafc;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JSeparator;
import javax.swing.JCheckBox;

public class Backup extends Model {
	
	private static final long serialVersionUID = -2327573491286609105L;
	
	private JLabel lblBackupRestore;
	private JRadioButton rb_backup;
	private JRadioButton rb_Restore;
	private JButton b_go;
	private JSeparator separator;
	private JLabel lblInclude;
	private JCheckBox cb_all;
	private JCheckBox cb_apk;
	private JCheckBox cb_obb;
	private JCheckBox cb_Sdcard;
	private JCheckBox cb_System;
	private JSeparator separator_1;
	private JLabel label;

	public Backup(Aafc main) {
		super(main);
		
		lblBackupRestore = new JLabel("Backup / Restore");
		lblBackupRestore.setBounds(10, 11, 87, 14);
		add(lblBackupRestore);
		
		rb_backup = new JRadioButton("Backup");
		rb_backup.setBounds(6, 32, 87, 23);
		add(rb_backup);
		
		rb_Restore = new JRadioButton("Restore");
		rb_Restore.setBounds(6, 58, 87, 23);
		add(rb_Restore);
		
		b_go = new JButton("Go!");
		b_go.setFont(new Font("Tahoma", Font.PLAIN, 14));
		b_go.setBounds(111, 32, 121, 49);
		add(b_go);
		
		separator = new JSeparator();
		separator.setBounds(10, 88, 222, 2);
		add(separator);
		
		lblInclude = new JLabel("Include:");
		lblInclude.setBounds(10, 98, 46, 14);
		add(lblInclude);
		
		cb_all = new JCheckBox("All");
		cb_all.setBounds(52, 94, 97, 23);
		add(cb_all);
		
		cb_apk = new JCheckBox(".apk's");
		cb_apk.setBounds(10, 119, 97, 23);
		add(cb_apk);
		
		cb_obb = new JCheckBox(".obb's");
		cb_obb.setBounds(10, 145, 97, 23);
		add(cb_obb);
		
		cb_Sdcard = new JCheckBox("SDcard");
		cb_Sdcard.setBounds(135, 119, 97, 23);
		add(cb_Sdcard);
		
		cb_System = new JCheckBox("System");
		cb_System.setBounds(135, 145, 97, 23);
		add(cb_System);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(10, 175, 222, 2);
		add(separator_1);
		
		label = new JLabel("Include:");
		label.setBounds(10, 184, 46, 14);
		add(label);
		
		setVisible(true);
	}

	@Override
	public void init() {
		// TODO
	}
}
