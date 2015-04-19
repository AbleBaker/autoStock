/**
 * 
 */
package com.autoStock.internal;

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
		ConsoleScanner.start();
		
		databaseCore = new DatabaseCore();
		databaseCore.init();
		
		if (mode != Mode.client_skip_tws){
			exchangeController = new ExchangeController();
			exchangeController.init();
		}
		
		if (mode == Mode.client){
			
		}
		
		if (mode == Mode.server){
			
		}
	}
	
	public static void shutdown(){
		System.exit(0);
	}
}
