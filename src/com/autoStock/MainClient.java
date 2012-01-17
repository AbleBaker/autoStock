/**
 * 
 */
package com.autoStock;

import com.autoStock.com.CommandHolder.Command;
import com.autoStock.comClient.ConnectionClient;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {
	public static void main(String[] args) {
		Co.println("AutoStock configuration");
		Co.println("Testing...");
		
		ConnectionClient connectionClient = new ConnectionClient();
		connectionClient.startClient();
		
		//connectionClient.sendSerializedCommand(Command.shutdown);
		connectionClient.sendSerializedCommand(Command.testThreadCom, args[0]);
		
		connectionClient.stop();
		
		Co.println("Sent");
	}
}
