package io.kuenzler.aafc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Prompt extends Model {

	private JPanel contentPane;
	private JTextField t_input;

	/**
	 * Create the frame.
	 */
	public Prompt() {
		super(450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		t_input = new JTextField();
		t_input.setBounds(10, 31, 414, 76);
		contentPane.add(t_input);
		t_input.setColumns(10);

		JButton b_submit = new JButton("Submit");
		b_submit.setBounds(10, 118, 414, 23);
		contentPane.add(b_submit);
	}
}
