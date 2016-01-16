package io.kuenzler.aafc.view;

import io.kuenzler.aafc.control.Utilities;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MD5check extends Model {

	private static final long serialVersionUID = 2374779627331702038L;
	private JPanel contentPane;
	private JTextField t_file;
	private JTextField t_md5;
	private JLabel l_success;
	private JLabel l_md5;
	private JLabel l_FileToFlash;
	private JLabel l_paste;
	private JLabel l_message;
	private JButton b_continue;
	private JButton b_skip;
	private JButton b_verify;
	private JButton b_paste;
	private JButton b_file;
	private JComboBox<String> cb_algorithm;
	private File file;

	/**
	 * Create the frame.
	 */
	public MD5check() {
		super(450, 250);
		setTitle("Checksum Calculator");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		initComponents();
		this.setVisible(true);
	}

	private void initComponents() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String l_message2 = "Please verify the md5 checksum of your file "
				+ "to be sure you wont harm your phone";
		l_message = new JLabel(l_message2);
		l_message.setBounds(10, 11, 414, 14);
		contentPane.add(l_message);

		t_file = new JTextField();
		t_file.setBounds(10, 53, 414, 20);
		contentPane.add(t_file);
		t_file.setColumns(10);

		t_md5 = new JTextField();
		t_md5.setBounds(10, 99, 414, 20);
		contentPane.add(t_md5);
		t_md5.setColumns(10);

		l_paste = new JLabel("Paste your checksum here");
		l_paste.setBounds(10, 84, 135, 14);
		contentPane.add(l_paste);

		l_FileToFlash = new JLabel("File to flash");
		l_FileToFlash.setBounds(10, 40, 94, 14);
		contentPane.add(l_FileToFlash);

		b_file = new JButton("Select other file");
		b_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectFile();
			}
		});
		b_file.setBounds(261, 33, 163, 21);
		contentPane.add(b_file);

		b_paste = new JButton("Paste from clipboard");
		b_paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String md5 = Utilities.getClipboardContents();
				if (!(md5.equals(""))) {
					t_md5.setText(md5);
				}
			}
		});
		b_paste.setBounds(261, 77, 163, 21);
		contentPane.add(b_paste);

		b_verify = new JButton("Verify file");
		b_verify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verify();
			}
		});
		b_verify.setBounds(137, 130, 245, 23);
		contentPane.add(b_verify);

		b_skip = new JButton("Skip");
		b_skip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				skip();
			}
		});
		b_skip.setBounds(10, 183, 135, 20);
		contentPane.add(b_skip);

		b_continue = new JButton("Continue");
		b_continue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont();
			}
		});
		b_continue.setEnabled(false);
		b_continue.setBounds(289, 183, 135, 20);
		contentPane.add(b_continue);

		l_success = new JLabel("");
		l_success.setHorizontalAlignment(SwingConstants.CENTER);
		l_success.setFont(new Font("Tahoma", Font.PLAIN, 20));
		l_success.setBounds(153, 172, 127, 31);
		contentPane.add(l_success);

		l_md5 = new JLabel("");
		l_md5.setHorizontalAlignment(SwingConstants.CENTER);
		l_md5.setBounds(10, 119, 414, 14);
		contentPane.add(l_md5);

		cb_algorithm = new JComboBox();
		cb_algorithm.setModel(new DefaultComboBoxModel<String>(new String[] {
				"MD5", "SHA-1", "SHA-256" }));
		cb_algorithm.setBounds(44, 131, 83, 20);
		contentPane.add(cb_algorithm);
	}

	// End of frame description

	private void verify() {
		try {
			String algoritm = (String) cb_algorithm.getSelectedItem();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					b_verify.setEnabled(false);
					b_verify.setText("Verifying...");
					cb_algorithm.setEnabled(false);
				}
			});
			String file, md5sum, orgmd5sum;
			file = t_file.getText();
			file = file.trim();
			// check
			// md5sum = io.kuenzler.aafc.control.MD5check.getMD5Checksum(file);
			md5sum = io.kuenzler.aafc.control.MD5check.hashFile(this.file,
					algoritm);
			l_md5.setText(md5sum);
			b_continue.setEnabled(true);
			System.out.println(md5sum); // TODO entfernen
			orgmd5sum = t_md5.getText();
			orgmd5sum = orgmd5sum.trim();
			// compare
			if (orgmd5sum.equals("")) {
				l_success.setText("Calcualted");
			} else if (orgmd5sum.equals(md5sum)) {
				l_success.setText("Match!");
			} else {
				l_success.setText("WRONG!");
				b_continue.setEnabled(false);
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					b_verify.setEnabled(true);
					b_verify.setText("Verify");
					cb_algorithm.setEnabled(true);
				}
			});
		} catch (Exception ex) {
			System.out.println("mimimi " + ex);
		} finally {
			
		}
	}

	private void selectFile() {
		JFileChooser chooser = new JFileChooser(new File(""));
		chooser.setDialogTitle("Choose file to check");
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setApproveButtonText("Choose file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		} else {
			// TODO log here
		}
		t_file.setText(file.getName());
	}

	private void cont() {
		// TODO
		skip();
	}

	private void skip() {
		// TODO aendern
		dispose();
	}

	/**
	 * TODO Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MD5check frame = new MD5check();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
