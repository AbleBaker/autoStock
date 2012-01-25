package com.autoStock;

import java.sql.SQLException;

import com.autoStock.com.CommandHolder.Command;
import com.autoStock.comClient.ConnectionClient;
import com.autoStock.database.DatabaseTest;
import com.autoStock.internal.ApplicationStates;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {
	public static void main(String[] args) throws SQLException {
		Co.println("Welcome to autoStock");
		
		ApplicationStates.startup();
		
		new DatabaseTest().test();
				
//		ConnectionClient connectionClient = new ConnectionClient();
//		connectionClient.startClient();		
//		connectionClient.sendSerializedCommand(Command.testThreadCom, "Apples");	
//		connectionClient.stop();
		
		Co.println("OK");
	}
}
