/**
 * 
 */
package io.kuenzler.aafc.view;

import io.kuenzler.aafc.data.Device;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Leonhard Kuenzler
 * @version 0.1
 * @date 06.09.15 | 22:30
 *
 */
public class DeviceView extends Model {

	private final Device device;
	private JLabel l_header;
	private JTextField t_serial;
	private JTextField t_vendor;
	private JTextField t_name;
	private JLabel l_serial;
	private JLabel l_vendor;
	private JLabel l_name;
	private JTextField t_nickname;
	private JLabel lblNickname;

	/**
	 * 
	 */
	public DeviceView(Device device) {
		super(0, 0); // TODO
		this.device = device;
		getContentPane().setLayout(
				new MigLayout("", "[80:93.00][5:5:20px][258.00,grow][5px:20]",
						"[][13.00][][][][]"));

		l_header = new JLabel("Device overview for <dev> (<Serial>)");
		getContentPane().add(l_header, "cell 0 0 4 1");

		l_serial = new JLabel("Serial:");
		getContentPane().add(l_serial, "cell 0 2");

		t_serial = new JTextField();
		t_serial.setEditable(false);
		t_serial.setColumns(10);
		getContentPane().add(t_serial, "cell 2 2,growx");

		l_vendor = new JLabel("Vendor:");
		getContentPane().add(l_vendor, "cell 0 3");

		t_vendor = new JTextField();
		t_vendor.setColumns(10);
		getContentPane().add(t_vendor, "cell 2 3,growx");

		l_name = new JLabel("Name:");
		getContentPane().add(l_name, "cell 0 4");

		t_name = new JTextField();
		t_name.setColumns(10);
		getContentPane().add(t_name, "cell 2 4,growx");

		lblNickname = new JLabel("Nickname:");
		getContentPane().add(lblNickname, "cell 0 5");

		t_nickname = new JTextField();
		t_nickname.setColumns(10);
		getContentPane().add(t_nickname, "cell 2 5,growx");
		initContent();
		pack();
		setVisible(true);
	}

	/**
	 * 
	 */
	private void initContent() {
		l_header.setText("Device overview for \""
				+ checkInformation(device.getName()) + "\" ("
				+ device.getSerial() + "):");
		t_serial.setText(device.getSerial());
		t_vendor.setText(checkInformation(device.getVendor()));
		t_name.setText(checkInformation(device.getName()));
		t_nickname.setText(checkInformation(device.getNickname()));
	}

	private String checkInformation(String information) {
		if (information.length() > 20) {
			return "<Can't read information>";
		} else {
			return information;
		}
	}

}
