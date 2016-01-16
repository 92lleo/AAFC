package io.kuenzler.aafc.control;

/**
 * Executer and manager for automated tasks
 * 
 * @author Leonhard Künzler
 * @date 08.02.15 - 16:30
 * @version 0.1
 *
 */
public class BatchExecuter {
	
	
	public void loadScript(){
		String path;
		//TODO choose batch file > path
		path = "testpath";
		loadScript(path);
	}
	
	public void loadScript(String path){
		if(path==null){
			loadScript();
		}
	}
	
	

}
