/**
 * 
 */
package com.autoStock.com;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Random;
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
		testSleep
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
		
		if (command == Command.testSleep){
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					String rand = String.valueOf(new Random().nextInt(100));
					while (true){
						MainServer.appleState = rand + "," + String.valueOf(new Random().nextInt(1000));
					}
				}
			});
			thread.start();
		}
	}
}
