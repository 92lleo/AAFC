package io.kuenzler.aafc.control;

import java.util.ArrayList;

/**
 * Command history for MainGui commandline.
 * 
 * No comments due simple tasks.
 * 
 * @author Leonhard Kuenzler
 * @date 26.05.14 23:30
 * @version 0.5
 */
public class CommandHistory {
	private ArrayList<String> commands;
	private int current;
	private boolean fst;

	public CommandHistory() {
		commands = new ArrayList<String>();
		current = -1;
		fst = true;
	}

	public void addCommand(String command) {
		commands.add(command);
		current++;
		fst = true;
		prin();
	}
	
	protected void setCurrent(){
		
	}

	public String getPrevCommand() {
		if (fst) {
			fst = false;
		} else {
			current--;
		}
		if (current < 0) {
			current++;
			return "exit";
		} else {
			return commands.get(current);
		}
	}

	public String getPostCommand() {
		if (fst) {
			fst = false;
		} else {
			current++;
		}
		if (current == commands.size()) {
			current--;
			return commands.get(current - 1);
		} else {
			return commands.get(current);
		}
		
	}

	protected void reset() {
		commands = new ArrayList<String>();
		current = -1;
	}
	
	public void prin(){
		for(String s :  commands){
			System.out.println(s);
		}
	}
	

}
