/**
 * 
 */
package com.autoStock.comClient;

import com.autoStock.com.CommandDefinitions.Command;
import com.autoStock.com.CommandHolder;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommandSender {
	public Runnable sendCommand(Command command){		
		return new CommandHolder(command); 
	}
	
	public Runnable sendCommand(Command command, Object... params){
		return new CommandHolder(command, params);
	}
}
