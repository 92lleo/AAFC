package io.kuenzler.aafc.control;

import io.kuenzler.aafc.data.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

/**
 * 
 * @author Leonhard Künzler
 * @version 1.1
 * @date 09.06.15 - 14:45
 */
public class DeviceChecker extends Thread {

	private boolean legalStart, tooManyDevicesWarning;
	private String adbPath, fbPath, sdkPath, currentSerial;
	private final Aafc main;
	private Device currentDevice;

	protected DeviceChecker(Aafc main) {
		this.main = main;
		legalStart = true;
		tooManyDevicesWarning = false;
		sdkPath = adbPath = fbPath = currentSerial = "";
		// start();
	}

	public void toggle() {
		if (isAlive()) {
			interrupt();
		} else {
			start();
		}
	}

	/**
	 * 
	 */
	public void run() {
		String fs = Utilities.getFileSep();
		sdkPath = main.getSdkPath() + fs + "platform-tools" + fs;

		adbPath = sdkPath + "adb";
		fbPath = sdkPath + "fastboot";
		if (main.getOs() == 1) {
			adbPath = adbPath + ".exe";
			fbPath = fbPath + ".exe";
		}
		try {
			while (!isInterrupted()) {
				String output;
				ArrayList<String> devices;
				int deviceCount, status;
				devices = new ArrayList<String>();
				status = 0;
				deviceCount = 0;
				// adb check
				output = new ProcessExecutor().command(adbPath, "devices")
						.readOutput(true).timeout(1, TimeUnit.SECONDS)
						.execute().outputUTF8();
				sleep(300);

				// -----------
				String[] adbDevices;
				adbDevices = output.split("\n");

				if (adbDevices.length < 2) {
					// no adb device
				} else {
					// putting adb devices into device list
					String device;
					for (int i = 1; i < adbDevices.length; i++) {
						device = adbDevices[i];
						device = device.replace('\t', ' ').trim();
						if (!device.isEmpty()) {
							devices.add(device);
						}
					}
				}

				// fastboot check
				output = new ProcessExecutor().command(fbPath, "devices")
						.readOutput(true).timeout(1, TimeUnit.SECONDS)
						.execute().outputUTF8();
				sleep(300);

				String[] fbDevices;
				fbDevices = output.split("\n");

				if (!(fbDevices == null) && fbDevices.length < 1) {
					// no fb device
				} else {
					// putting fb devices into device list
					for (String device : fbDevices) {
						device = device.replace('\t', ' ').trim();
						if (!device.isEmpty()) {
							devices.add(device);
						}
					}
				}

				if (devices.size() > 1) {
					if (!tooManyDevicesWarning) {
						tooManyDevicesWarning = true;
						main.updateCommandline("--You have connected "
								+ devices.size()
								+ " devices.\n--You only can use one device");
						main.setDeviceSerial("Too many devices ("
								+ devices.size() + ")");
						for (String d : devices) {
							main.updateCommandline("dev: " + d);
						}
						currentSerial = "";
						status = 0;
					}

				} else if (devices.size() < 1) {
					main.setDeviceSerial("No device");
					currentSerial = "";
					status = 0;

				} else {
					tooManyDevicesWarning = false;
					String device, serial;
					device = devices.get(0);
					serial = device.substring(0, device.indexOf(" ")).trim();

					if (device.endsWith("device")) {
						status = 2;
					} else if (device.endsWith("unknown")) {
						status = 4;
					} else if (device.endsWith("sideload")) {
						status = 3;
					} else if (device.endsWith("fastboot")) {
						status = 1;
					} else if (device.endsWith("recovery")) {
						status = 5;
					} else if (device.endsWith("unauthorized")) {
						status = 6;
					} else if (device.endsWith("offline")) {
						status = 7;
					}

					if (!serial.equals(currentSerial)) { // new mode entered
						currentSerial = serial;
						boolean sendSerial, devHb;
						sendSerial = Boolean.parseBoolean(main.getPreferences()
								.getFromKey("includeSerialInHeartbeat"));
						devHb = Boolean.parseBoolean(main.getPreferences()
								.getFromKey("heartbeatOnNewDevice"));
						// String uid, String device, boolean sendSerial,
						// boolean hb, boolean hbdev)
						Utilities.heartbeat(main.getFromKey("uid"),
								currentSerial, sendSerial, false, devHb);
						main.setDeviceSerial(currentSerial);
						//readDeviceInformation(serial);
					}
					readDeviceInformation(serial);
				}

				if (!(status == main.getDeviceStatus())) {
					main.setDeviceStatus(status);
					if (main.getAutoChange()) {
						if (status == 1) { // fb
							main.setMode(Executer.FASTBOOT);
						} else if (status == 2 || status == 3 || status == 4
								|| status == 5 || status == 6 || status == 7) { // adb
							main.setMode(Executer.ADB);
						} else if (status == 0) {
							main.setMode(Executer.CMD); // TODO gut?
						}
					}
				}

				sleep(500);
			}
		} catch (TimeoutException | InvalidExitValueException | IOException
				| InterruptedException e) {
			if (e.toString().toLowerCase().contains("timeout")) {
				return;
			}
			if (e.toString().toLowerCase().contains("sleep")) {
				return;
			}
			Utilities.error(e, true);
		}
	}

	private void readDeviceInformation(String serial) {
		try {
			String devName, devVendor, devBattery;
			if (!main.getDeviceManager().isDeviceKnown(serial)
					&& !main.getDeviceManager().hasAllInformation(serial)) {
				devName = new ProcessExecutor()
						.command(adbPath, "shell", "getprop",
								"ro.product.model").readOutput(true)
						.timeout(1, TimeUnit.SECONDS).execute().outputUTF8();
				if (!checkInformation(devName)) {
					devName = new ProcessExecutor()
							.command(adbPath, "shell", "getprop",
									"ro.product.name").readOutput(true)
							.timeout(1, TimeUnit.SECONDS).execute()
							.outputUTF8();
				}
				if (!checkInformation(devName)) {
					devName = "<Can't get Name>";
				}
				devVendor = new ProcessExecutor()
						.command(adbPath, "shell", "getprop",
								"ro.product.brand").readOutput(true)
						.timeout(1, TimeUnit.SECONDS).execute().outputUTF8();
				if (!checkInformation(devVendor)) {
					devVendor = "<Can't get Vendor>";
				}
				sleep(400);
				Device d = new Device();
				d.setSerial(serial);
				d.setName(devName);
				d.setVendor(devVendor);
				String fullname = devVendor + " " + devName;
				d.setFullName(fullname);
				d.setNickname(fullname);
				main.getDeviceManager().addDevice(d);
				currentDevice = d;
			} else {
				Device d = main.getDeviceManager().getDevice(serial);
				devName = d.getName();
				devVendor = d.getVendor();
				currentDevice = d;
			}
			devBattery = "7";
			if (!checkInformation(devBattery)) {
				devName = "-1";
			}
			// TODO more stuff here
			main.getFrame().setDevName(devName);
			main.getFrame().setDevVendor(devVendor);
			main.getFrame().setDevBattery(Integer.parseInt(devBattery));
			Log.getLogger().log(
					Level.INFO,
					"Detected new device: Mode=" + devBattery + ", Name="
							+ devName + ".");
		} catch (TimeoutException | InvalidExitValueException | IOException
				| InterruptedException e) {
			if (e.toString().toLowerCase().contains("timeout")) {
				return;
			}
			if (e.toString().toLowerCase().contains("sleep")) {
				return;
			}
			Utilities.error(e, true);
		}
	}

	private boolean checkInformation(String information) {
		if (information.length() > 20 || information.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unused")
	private void changeMode(final int mode) {
		switch (mode) {
		case Executer.ADB:
			// TODO ADB stuff (unknown,device,sideload)
			break;
		case Executer.FASTBOOT:
			// TODO FB stuff
			break;
		default:
			// TODO error
		}
	}
}
