package io.kuenzler.aafc.view;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import java.awt.Toolkit;

/**
 * Basic commandbuilding window -not for use-
 * 
 * @author Leonhard Kuenzler
 * @version 0.1
 * @date 19.09.14 15:00
 */
public class Batch extends javax.swing.JFrame {

	private static final long serialVersionUID = -2036394059706014615L;

	public Batch() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Batch.class.getResource("/res/icon.jpeg")));
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		JTextArea textArea = new JTextArea();
		textArea.setToolTipText("Load or create list of commands to run");
		textArea.setRows(5);
		textArea.setEditable(false);
		textArea.setColumns(20);

		JButton button = new JButton();
		button.setToolTipText("Load a textfile with your commands. First line must be \".adbfastboot\"");
		button.setText("Load batch file");

		JLabel label = new JLabel();
		label.setText("Load a batch file (txt). First line must be \".adbfastboot\"");

		JButton button_1 = new JButton();
		button_1.setToolTipText("Save your commands to a file");
		button_1.setText("Save batch file");

		JButton button_2 = new JButton();
		button_2.setToolTipText("Clear the commandbox");
		button_2.setText("Clear");

		JButton button_3 = new JButton();
		button_3.setText("Execute next command");

		JCheckBox checkBox = new JCheckBox();
		checkBox.setToolTipText("Enables/disables editing of commandbox");
		checkBox.setText("Edit commands");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(28)
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														label,
														GroupLayout.PREFERRED_SIZE,
														322,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		button,
																		GroupLayout.PREFERRED_SIZE,
																		103,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(22)
																.addComponent(
																		button_1,
																		GroupLayout.PREFERRED_SIZE,
																		103,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(22)
																.addComponent(
																		button_2,
																		GroupLayout.PREFERRED_SIZE,
																		72,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(1)
																.addComponent(
																		textArea,
																		GroupLayout.PREFERRED_SIZE,
																		164,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		button_3,
																		GroupLayout.PREFERRED_SIZE,
																		145,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(66)
																.addComponent(
																		checkBox)))
								.addContainerGap(50, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(label)
								.addGap(4)
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(button)
												.addComponent(button_1)
												.addComponent(button_2))
								.addGap(5)
								.addComponent(textArea,
										GroupLayout.PREFERRED_SIZE, 94,
										GroupLayout.PREFERRED_SIZE)
								.addGap(333)
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(button_3)
												.addComponent(checkBox))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		getContentPane().setLayout(layout);

		pack();
	}
	
	//TODO: Main entfernen

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		new Batch().setVisible(true);
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			//blabla
		}
	}
}
