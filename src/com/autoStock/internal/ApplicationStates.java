/**
 * 
 */
package com.autoStock.internal;

import com.autoStock.Co;
import com.autoStock.MainServer;
import com.autoStock.database.DatabaseCore;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.internal.Global.Mode;

/**
 * @author Kevin Kowalewski
 *
 */
public class ApplicationStates {
	private static DatabaseCore databaseCore;
	private static ExchangeController exchangeController;
	
	public static void startup(Mode mode){
		Global.setMode(mode);
		databaseCore = new DatabaseCore();
		databaseCore.init();
		
		exchangeController = new ExchangeController();
		//exchangeController.init();
		
		if (mode == Mode.client){
			
		}
		
		if (mode == Mode.server){
			
		}
	}
	
	public static void shutdown(){
		if (Global.getMode() == Mode.client){
			System.exit(0);
		}
		
		if (Global.getMode() == Mode.server){
			try {MainServer.runningThread.interrupt();}catch(Exception e){}
			Co.println("Good bye!");
			System.exit(0);
		}
	}
}
