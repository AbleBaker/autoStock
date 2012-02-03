/**
 * 
 */
package com.autoStock.com;

import com.autoStock.Co;
import com.autoStock.MainServer;
import com.autoStock.com.CommandDefinitions.Command;
import com.autoStock.internal.ApplicationStates;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CommandHolder implements Runnable {
	Command command;
	Object[] arrayOfObject;
	
	public CommandHolder(Command command){
		this.command = command;
	}
	
	public CommandHolder(Command command, Object... params){
		this.command = command;
		this.arrayOfObject = params;
	}

	@Override
	public void run() {
		Co.println("Server: Executing: " + this.command.name());
		if (command == Command.shutdown){
			ApplicationStates.shutdown();
		}
		
		else if (command == Command.testThreadCom){
			MainServer.appleState = (String)arrayOfObject[0];
		}
		
		else if (command == Command.client_ex_request_historical_data){
			MainServer.appleState = "Would have request historical data";
		}
		
		else {
			Co.println("Error: No matching command instance pairs");
		}
	}
}
