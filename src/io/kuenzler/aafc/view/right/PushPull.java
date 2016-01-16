package io.kuenzler.aafc.view.right;

import io.kuenzler.aafc.control.Aafc;
import io.kuenzler.aafc.data.FileDrop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;

@SuppressWarnings("rawtypes")
public class PushPull extends Model {

	JList list_toFlash;
	DefaultListModel listModel;
	private static final long serialVersionUID = 1796520919288274726L;

	@SuppressWarnings("rawtypes")
	public PushPull(Aafc main) {
		super(main);
		setLayout(null);

		JLabel l_title = new JLabel("Push | Pull files");
		l_title.setBounds(10, 11, 105, 14);
		add(l_title);

		listModel = new DefaultListModel();
		JList list = new JList(listModel);
		// listModel.addElement("new item");

		list_toFlash = new JList(listModel);
		list_toFlash.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteFromList();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveObject(-1);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					moveObject(1);
				}
			}
		});
		list_toFlash.setBounds(10, 115, 222, 157);
		add(list_toFlash);

		JButton b_choose = new JButton("Choose files");
		b_choose.setBounds(10, 87, 222, 23);
		add(b_choose);

		JButton b_remove = new JButton("Remove");
		b_remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteFromList();
			}
		});
		b_remove.setBounds(135, 283, 97, 23);
		add(b_remove);

		JButton b_up = new JButton("up");
		b_up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveObject(1);
			}
		});
		b_up.setBounds(10, 283, 46, 23);
		add(b_up);

		JButton b_down = new JButton("down");
		b_down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveObject(-1);
			}
		});
		b_down.setBounds(66, 283, 59, 23);
		add(b_down);

		JButton b_start = new JButton("Sideload!");
		b_start.setBounds(10, 390, 222, 23);
		add(b_start);

		JTextArea ta_sideload = new JTextArea();
		ta_sideload.setWrapStyleWord(true);
		ta_sideload.setLineWrap(true);
		ta_sideload
				.setText("By clicking 'Sideload!' the first item from the list will be pushed to the phone and flashed. be sure to know how 'adb sideload' works.");
		ta_sideload.setEditable(false);
		ta_sideload.setBounds(10, 317, 222, 65);
		add(ta_sideload);

		// JPanel myPanel = new JPanel();
		new FileDrop(list_toFlash, new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				// list_toFlash..append( files[i].getCanonicalPath() + "\n" );
				addFiles(files);
				System.out.println("Yolo asdf swag");
			} // end filesDropped
		}); // end FileDrop.Listener

		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void addFiles(File[] files) {
		for (File x : files) {
			listModel.addElement(x.getName());
		}
	}

	private void deleteFromList() {
		int[] i = list_toFlash.getSelectedIndices();
		int j = 0;
		Arrays.sort(i);
		for (int x : i) {
			listModel.removeElementAt(x - j);
			j++;
		}
	}

	private void moveObject(int direction) {

	}

	@Override
	public void init() {
		// TODO
	}
}
