package com.autoStock;

import java.sql.SQLException;

import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.menu.MenuDisplayLauncher;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {	
	public static void main(String[] args) throws SQLException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Co.println("******** Welcome to autoStock ********\n");
			
		ApplicationStates.startup(Mode.client);

		MenuController menuController = new MenuController();
		if (args.length == 0){menuController.displayMenu(MenuStructures.menu_main); ApplicationStates.shutdown();}
		MenuStructures menuStructure = menuController.getRelatedMenu(args);
		menuController.handleMenuStructure(menuStructure, args);
		
		new MenuDisplayLauncher().launchDisplay(menuStructure);
		
		while (Global.callbackLock.isWaitingForCallbacks()){
			Co.println("...");
			try{Thread.sleep(1*1000);}catch(InterruptedException e){return;}
		}
		
		Co.println("\n******** Finished ********");
		//System.exit(0);
	}
}
