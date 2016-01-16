/**
 * 
 */
package io.kuenzler.aafc.data;

import java.util.ArrayList;

/**
 * @author Leonhard Kuenzler
 * @version 0.1
 * @date 06.09.15 | 22:20
 *
 */
public class Device {
	private String serial, name, vendor, fullName, nickname;
	private boolean root, fastboot;
	private ArrayList<String> partitions;

	public Device() {

	}

	@Override
	public String toString() {
		return serial+" "+fullName;
	}

	/**
	 * 
	 * @param device
	 * @return
	 */
	public boolean equalsSerial(Device device) {
		if (this.serial.equals(device.serial)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param device
	 * @return
	 */
	public boolean equals(Device device) {
		if (!this.serial.equals(device.serial)) {
			return false;
		}
		if (!this.name.equals(device.name)) {
			return false;
		}
		if (!this.vendor.equals(device.vendor)) {
			return false;
		}
		if (!this.fullName.equals(device.fullName)) {
			return false;
		}
		if (!this.nickname.equals(device.nickname)) {
			return false;
		}
		if (!this.fastboot == device.fastboot) {
			return false;
		}
		if (!this.root == device.root) {
			return false;
		}
		return true;
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * @param serial
	 *            the serial to set
	 */
	public void setSerial(String serial) {
		this.serial = serial.trim();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name.trim();
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor.trim();
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName.trim();
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}

	/**
	 * @return the root
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * @return the fastboot
	 */
	public boolean isFastboot() {
		return fastboot;
	}

	/**
	 * @param fastboot
	 *            the fastboot to set
	 */
	public void setFastboot(boolean fastboot) {
		this.fastboot = fastboot;
	}

	/**
	 * @return the partitions
	 */
	public String[] getPartitions() {
		return (String[]) partitions.toArray();
	}

	/**
	 * @param partitions
	 *            the partitions to set
	 */
	public void setPartitions(ArrayList<String> partitions) {
		this.partitions = partitions;
	}

	/**
	 * 
	 * @param partition
	 */
	public void addPartition(String partition) {
		partitions.add(partition);
	}
}
