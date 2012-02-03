package com.autoStock;

import java.sql.SQLException;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {	
	public static void main(String[] args) throws SQLException {
		Global.mode = Mode.client;
		Co.println("Welcome to autoStock\n");
		
		ApplicationStates.startup();
	
		MenuController menuController = new MenuController();
		menuController.displayMenu(MenuStructures.menu_main);
		MenuStructures menuStructure = menuController.getRelatedMenu(args);
		menuController.handleMenuStructure(menuStructure, args);				

		//new TWSSupervisor().launchTws();
		//new DatabaseTest().test();
		//connectionClient.stop();
		
		Co.println("\n\nWaiting for callbacks... OK");
		try{Thread.sleep(1*1000);}catch(Exception e){}
		Co.println("\n Done \n");
		System.exit(0);
		
	}
}
