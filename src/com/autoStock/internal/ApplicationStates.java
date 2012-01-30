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
	
	public static void startup(){
		databaseCore = new DatabaseCore();
		databaseCore.init();
		
		ExchangeController.init();
		
		if (Global.mode == Mode.client){
			
		}
		
		if (Global.mode == Mode.server){
			
		}
	}
	
	
	public static void shutdown(){
		if (Global.mode == Mode.client){
			System.exit(0);
		}
		
		if (Global.mode == Mode.server){
			try {MainServer.runningThread.interrupt();}catch(Exception e){}
			Co.println("Good bye!");
			System.exit(0);
		}
	}
}
