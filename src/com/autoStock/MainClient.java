package com.autoStock;

import java.sql.SQLException;

import com.autoStock.com.CommandHolder.Command;
import com.autoStock.comClient.ConnectionClient;
import com.autoStock.database.DatabaseTest;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {
	public static void main(String[] args) throws SQLException {
		Co.println("AutoStock configuration");
		Co.println("Testing...");
		
		//new DatabaseTest().test();
		
		ConnectionClient connectionClient = new ConnectionClient();
		connectionClient.startClient();
//		
//		//connectionClient.sendSerializedCommand(Command.shutdown);
		connectionClient.sendSerializedCommand(Command.testThreadCom, "Apples");
//		
		connectionClient.stop();
//		
		Co.println("Sent");
	}
}
