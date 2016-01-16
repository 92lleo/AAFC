/**
 * 
 */
package io.kuenzler.aafc.control;

import io.kuenzler.aafc.data.Device;
import io.kuenzler.aafc.view.DeviceView;

import java.util.ArrayList;

/**
 * @author Leonhard Kuenzler
 *
 */
public class DeviceManager {

	private ArrayList<Device> devices;
	private Aafc main;

	public DeviceManager(Aafc main) {
		devices = new ArrayList<Device>();
		this.main = main;
	}

	public void addDevice(Device device) {
		Log.getLogger().info("1 - entering\n" + toString());
		if (isDeviceKnown(device.getSerial())) {
			Log.getLogger().info("2 - device known");
			Device oldDevice = getDevice(device.getSerial());
			if (device.equals(oldDevice)) {
				Log.getLogger().info("3 -device equal");
				return;
			} else {
				Log.getLogger().info("4 - device not equal");
				devices.remove(oldDevice);
				Log.getLogger().info("5 - after removing\n" + toString());
			}
		}
		Log.getLogger().info("6 - adding new device");
		Log.getLogger().info(
				"Added device " + device.getSerial() + ","
						+ device.getFullName());
		devices.add(device);
		new DeviceView(device);
		Log.getLogger().info("7 - after adding\n" + toString());
	}

	public Device[] getDevices() {
		return (Device[]) devices.toArray();
	}

	public int getDeviceCount() {
		return devices.size();
	}

	public boolean removeDevice(String serial) {
		try {
			devices.remove(getDevice(serial));
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isDeviceKnown(String serial) {
		for (Device d : devices) {
			if (d.getSerial().equals(serial)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String r = "Device List:\nSize is " + devices.size() + "\n";
		for (Device d : devices) {
			r += d + "\n";
		}
		return r;
	}

	/**
	 * @param serial
	 * @return
	 */
	public boolean hasAllInformation(String serial) {
		// TODO check if name etc is there
		return false;
	}

	/**
	 * @param serial
	 * @return
	 */
	public Device getDevice(String serial) {
		for (Device d : devices) {
			if (d.getSerial().equals(serial)) {
				return d;
			}
		}
		return null;
	}

}
