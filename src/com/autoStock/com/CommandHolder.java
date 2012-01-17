/**
 * 
 */
package com.autoStock.com;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Vector;

import com.autoStock.Co;
import com.autoStock.MainServer;
import com.autoStock.internal.ApplicationStates;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CommandHolder implements Runnable {
	Command command;
	Object[] arrayOfObject;
	
	public static enum Command {
		shutdown,
		startup,
		testThreadCom,
		testThreadCom2,
	}
	
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
		
		if (command == Command.testThreadCom){
			MainServer.appleState = (String)arrayOfObject[0];
		}
	}
}
