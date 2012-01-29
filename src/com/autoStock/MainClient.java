package com.autoStock;

import java.sql.SQLException;

import com.autoStock.com.CommandHolder.Command;
import com.autoStock.comClient.ConnectionClient;
import com.autoStock.database.DatabaseTest;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.trading.platform.ib.tws.TWSSupervisor;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {
	public static void main(String[] args) throws SQLException {
		Co.println("Welcome to autoStock\n");
		
		ApplicationStates.startup();
		
		new MenuController().displayMenu(MenuStructures.menu_main);
		
		
		//new TWSSupervisor().launchTws();
		//new DatabaseTest().test();
//				
//		ConnectionClient connectionClient = new ConnectionClient();
//		connectionClient.startClient();		
//		connectionClient.sendSerializedCommand(Command.testSleep);	
//		//connectionClient.stop();
//		
//		try{Thread.sleep(30000);}catch(Exception e){}
		
		Co.println("\n Done \n");
	}
}
