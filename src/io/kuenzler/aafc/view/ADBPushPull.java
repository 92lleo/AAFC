package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Aafc;
import io.kuenzler.aafc.control.Executer;

import java.awt.Toolkit;


/**
 * GUI to create flash/erase/format fastboot commands Possible to run/push to
 * execution line
 *
 * @author Leonhard Kuenzler
 * @date 22.06.13
 */
public class ADBPushPull extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5945646980599277383L;
	
	Aafc main;
	boolean push;

	/**
	 * Creates new form FastbootFlashEraseFormat
	 */
	public ADBPushPull(Aafc main) {
		super(560, 160);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ADBPushPull.class.getResource("/res/icon.jpeg")));
		initComponents();
		this.main = main;
		push = true;
	}

	private void initComponents() {

		CB_PushPull = new javax.swing.JComboBox();
		t_path2 = new javax.swing.JTextField();
		l_action = new javax.swing.JLabel();
		l_path1 = new javax.swing.JLabel();
		l_path2 = new javax.swing.JLabel();
		b_run = new javax.swing.JButton();
		b_execute = new javax.swing.JButton();
		b_batch = new javax.swing.JButton();
		b_close = new javax.swing.JButton();
		CB_changeMode = new javax.swing.JCheckBox();
		t_path1 = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Commandbuilder");
		setBounds(new java.awt.Rectangle(300, 300, 0, 0));
		setName("Commandbuilder"); // NOI18N

		CB_PushPull.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"push", "pull" }));
		CB_PushPull.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CB_PushPullActionPerformed(evt);
			}
		});

		t_path2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				t_path2MouseClicked(evt);
			}
		});
		t_path2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				t_path2ActionPerformed(evt);
			}
		});

		l_action.setText("Action");

		l_path1.setText("<local>");

		l_path2.setText("<remote>");

		b_run.setText("Run!");
		b_run.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_runActionPerformed(evt);
			}
		});

		b_execute.setText("To executeline");
		b_execute.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_executeActionPerformed(evt);
			}
		});

		b_batch.setText("To batchwindow");
		b_batch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_batchActionPerformed(evt);
			}
		});

		b_close.setText("Close");
		b_close.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				b_closeActionPerformed(evt);
			}
		});

		CB_changeMode.setText("Change mode to ADB before executing command");

		t_path1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				t_path1MouseClicked(evt);
			}
		});
		t_path1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				t_path1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(22, 22, 22)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		CB_changeMode,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		354,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						b_run,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						l_action,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						CB_PushPull,
																						0,
																						77,
																						Short.MAX_VALUE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										b_execute)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										b_batch)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										145,
																										Short.MAX_VALUE)
																								.addComponent(
																										b_close))
																				.addGroup(
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														t_path1)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(
																																		l_path1)
																																.addGap(0,
																																		0,
																																		Short.MAX_VALUE)))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														l_path2)
																												.addComponent(
																														t_path2,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														224,
																														javax.swing.GroupLayout.PREFERRED_SIZE))))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(l_action)
												.addComponent(l_path1)
												.addComponent(l_path2))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														CB_PushPull,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														t_path2,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														t_path1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(5, 5, 5)
								.addComponent(CB_changeMode)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(b_run)
												.addComponent(b_execute)
												.addComponent(b_batch)
												.addComponent(b_close))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void CB_PushPullActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_CB_PushPullActionPerformed
		if (CB_PushPull.getSelectedIndex() == 0) {
			l_path1.setText("<local>");
			l_path2.setText("<remote>");
			push = true;
		} else {
			l_path1.setText("<remote>");
			l_path2.setText("<local>");
			push = false;
		}
	}// GEN-LAST:event_CB_PushPullActionPerformed

	private void t_path2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_t_path2ActionPerformed
		// if (!push) {
		// t_path2.setText(main.chooseFileString("Select file", false, "Select",
		// ""));
		// }
		// main.updateCommandline(main.chooseFileString("Select image", false,
		// "Insert", ""));
	}// GEN-LAST:event_t_path2ActionPerformed

	private void b_runActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_b_runActionPerformed
		changeMode();
		main.runCommand(createCommand(), Executer.ADB, true, true);
		main.updateCommandline(createCommand());
		dispose();
	}// GEN-LAST:event_b_runActionPerformed

	private void b_executeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_b_executeActionPerformed
		if (CB_changeMode.isSelected()) {
			main.setMode(Executer.ADB);
		}
		main.updateExecuteline(createCommand(), true);
		dispose();
	}// GEN-LAST:event_b_executeActionPerformed

	private void b_batchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_b_batchActionPerformed
		if (CB_changeMode.isSelected()) {
			main.updateBatch("mode adb");
		}
		main.updateBatch(createCommand());
		dispose();
	}// GEN-LAST:event_b_batchActionPerformed

	private void t_path2MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_t_path2MouseClicked
		if (!push) {
			t_path2.setText(main.chooseFileString("Select file", false,
					"Select", ""));
		}
	}// GEN-LAST:event_t_path2MouseClicked

	private void b_closeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_b_closeActionPerformed
		dispose();
	}// GEN-LAST:event_b_closeActionPerformed

	private void t_path1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_t_path1MouseClicked
		if (push) {
			t_path1.setText(main.chooseFileString("Select image", false,
					"Insert", ""));
		}
		// main.updateCommandline(main.chooseFileString("Select image", false,
		// "Insert", ""));
	}// GEN-LAST:event_t_path1MouseClicked

	private void t_path1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_t_path1ActionPerformed
		// path 1
	}// GEN-LAST:event_t_path1ActionPerformed
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JComboBox CB_PushPull;
	private javax.swing.JCheckBox CB_changeMode;
	private javax.swing.JButton b_batch;
	private javax.swing.JButton b_close;
	private javax.swing.JButton b_execute;
	private javax.swing.JButton b_run;
	private javax.swing.JLabel l_action;
	private javax.swing.JLabel l_path1;
	private javax.swing.JLabel l_path2;
	private javax.swing.JTextField t_path1;
	private javax.swing.JTextField t_path2;

	// End of variables declaration//GEN-END:variables

	private String createCommand() {
		String command = "";
		command += CB_PushPull.getSelectedItem();
		command += " " + t_path1.getText();
		command += " " + t_path2.getText();
		return command;
	}

	private void changeMode() {
		if (CB_changeMode.isSelected()) {
			main.setMode(Executer.ADB);
		}
	}
}
