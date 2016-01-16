package io.kuenzler.aafc.view.right;

import io.kuenzler.aafc.control.Aafc;

import javax.swing.JPanel;

/**
 * Panel on the right side depending on operation
 * 
 * @author Leonhard Kuenzler
 * @version 1.0
 * @date 09.09.14 22:00 | final
 */
public abstract class Model extends JPanel {

	private static final long serialVersionUID = 2198092570137691235L;
	public static final int START = 0;
	public static final int SIDELOAD = 1;
	public static final int APKINSTALL = 2;
	public static final int PUSHPULL = 3;
	public static final int BACKUP = 4;

	@SuppressWarnings("unused")
	private final Aafc main;
	boolean working;

	// 242 * 424
	public Model(Aafc main) {
		this.main = main;
		setLayout(null);
		working = false;
		setBounds(0, 0, 242, 424); //244
	}

	public abstract void init();

	public boolean getWorking() {
		return working;
	}
}
