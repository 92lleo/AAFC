package io.kuenzler.aafc.control;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Handles executions (queue and list for waiting/running tasks)
 *
 * @author Leonhard Kuenzler
 * @date 21.05.15 16:50
 * @version 0.9.3
 */
public class ExecutionHandler {

	private LinkedList<Executer> waiting;
	private HashMap<String, Process> running;
	private final transient Aafc main;
	private transient Executer currentThread;
	private boolean working, shell;
	private Runner runner;
	private Executer exec;
	private Process currentProcess;

	/**
	 * Sets main object, creates List(waiting) and HashMap(running)
	 *
	 * @param main
	 *            the main object (Adbfbtool)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExecutionHandler(final Aafc main) {
		this.main = main;
		shell = false;
		waiting = new LinkedList();
		running = new HashMap();
		working = false;
		runner = new Runner(this);
	}

	/**
	 * Adds a new task to waiting processes list (queue) If handler is not
	 * running, it will be started
	 *
	 * @deprecated //TODO
	 * @param thread
	 *            Ready-to-execute Executer
	 */
	public void add(final Executer thread) {
		if (thread.getCommand() == null) {
			updateCommandline(thread.toString() + " cannot be added (emtpy)");
		} else {
			waiting.addLast(thread);
			if (working) {
				try {
					runner.join();
				} catch (InterruptedException ex) {
					updateCommandline("--Error (exechandler) while waiting for running object.\n--"
							+ ex);
				}
				runner = new Runner(this);
				runner.start();
				working = true;
			} else {
				try {
					runner.start();
				} catch (Exception e) {
					// Utilities.error(e, false);
					// TODO uncomment when fixed
				}
			}
		}
	}

	/**
	 * Calls main updateCommandline(String update) for gui update
	 *
	 * @param update
	 *            String to write to gui
	 * @see Adbfbtool.updateCommandline(String update)
	 */
	private void updateCommandline(String update) {
		main.updateCommandline(update);
	}

	/**
	 * Kills process selected by mode+command (eg. blocking fastboot)
	 *
	 * @param command
	 *            (<mode> <command>) key for getting process
	 * @return true: process found and killed, false: process not found
	 */
	@SuppressWarnings("unused")
	private boolean kill(String command) {
		Process process;
		process = (Process) running.get(command);
		if (process == null) {
			return false;
		} else {
			process.destroy();
			return true;
		}
	}

	/**
	 * Basic run methode. creates new executer and sets command, mode and flags
	 *
	 * @param command
	 *            command to execute
	 * @param mode
	 *            mode to use (1-3)
	 * @param wait
	 *            true: programm will wait for process, false: process ignored
	 * @param show
	 *            true: output will be printet in gui, false: output ignored
	 * @return
	 */
	public Process runCommand(String command, int mode, boolean wait,
			boolean show) {
		// TODO stuff here yolo
		if (mode == Executer.ADB && shell) {
			if (exec.isShell()) {
				exec.runNextShell();
			} else {
				exec = new Executer(main);
				exec.setShell(true);
			}
		} else {
			exec = new Executer(main);
			if(true){
			//TODOif (!isProcessRunning(currentProcess)) {
				currentProcess = exec.runCommand(command, mode, wait, show);
				return currentProcess;
			} else {
				if (wait) {
					// TODO add to queue
					main.updateCommandline("command "+command+" in queue");
					exec.setCommand(command, mode, wait, show);
					return exec.getProcess();
				} else {
					return exec.runCommand(command, mode, wait, show);
				}
			}
			// TODO debug msg System.out.println(command+" "+mode);
			// TODO add(exec);
		}
		return null;
	}

	private boolean isProcessRunning(Process state) {
		try {
			if (state.exitValue() > 0) {
				return false;
			}
		} catch (IllegalStateException | NullPointerException e) {
			return true;
		}
		return true;
	}

	protected void resetRunner() {
		// runner.join();
		runner = new Runner(this);
	}

	/**
	 * Kills all running processes. for programm exit and problem use
	 */
	public void killAllRunning() {
		Executer exec;
		for (Object value : running.values()) {
			exec = (Executer) value;
			if (exec != null) {
				exec.kill();
			}
		}
	}

	private class Runner extends Thread {

		// TODO final ExecutionHandler execHandler;

		Runner(ExecutionHandler execHandler) {
			// TODO this.execHandler = execHandler;
		}

		@Override
		public void run() {
			working = true;
			while (!waiting.isEmpty()) {
				currentThread = waiting.pollFirst();
				currentThread.start();
				running.put(currentThread.getCommand(),
						currentThread.getProcess());
				if (currentThread.getWait()) {
					main.setWorking(true);
					try {
						waitForProcess();
					} catch (InterruptedException ex) {
						updateCommandline("--Crashed while waiting for process:\n--"
								+ ex);
					} finally {
						main.setWorking(false);
					}
				}
			}
			working = false;
			resetRunner();
		}

		/**
		 * Blocks until the process has finished
		 *
		 * @throws InterruptedException
		 *             may be thrown by waitFor()
		 */
		private void waitForProcess() throws InterruptedException {
			Process currentProcess = currentThread.getProcess();
			currentProcess.waitFor();
		}
	}
}
