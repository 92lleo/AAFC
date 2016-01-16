package io.kuenzler.aafc.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Model Class for all Frames containing the icon.
 * 
 * @author Leonhard Kuenzler
 * @version 1.0
 * @date 27.07.15 | 19:30 (set size only if greater than 0)
 *
 */
public abstract class Model extends JFrame {

	/**
	 * svUID
	 */
	private static final long serialVersionUID = -4363208127345233515L;

	/**
	 * Set size if greather than null, or just set frame icon
	 */
	public Model(int width, int heigh) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainView.class.getResource("/io/kuenzler/aafc/res/icon.jpeg")));
		if (width > 0 && heigh > 0) {
			setSize(width, heigh);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((dim.width - getWidth()) / 2,
					(dim.height - getHeight()) / 2);
		} else {
			pack(); // TODO need to pack?
		}

	}

}
