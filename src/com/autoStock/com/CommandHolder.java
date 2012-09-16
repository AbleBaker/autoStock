/**
 * 
 */
package com.autoStock.com;

import java.lang.reflect.Type;

import com.autoStock.Co;
import com.autoStock.MainServer;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.internal.ApplicationStates;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CommandHolder<T> {
	public Command command;
	public T commandParameters;
	
	public CommandHolder(Command command){
		this.command = command;
	}
	
	public CommandHolder(Command command, T commandParameters){
		this.command = command;
		this.commandParameters = commandParameters;
	}
}
