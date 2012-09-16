package com.autoStock.com;

import java.io.PrintWriter;

import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.comServer.CommunicationDefinitions.CommunicationCommands;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommandSerializer {
	public synchronized static void sendSerializedCommand(Command command, PrintWriter printWriter){
		String string = new Gson().toJson(new CommandHolder(command), new TypeToken<CommandHolder>(){}.getType());
		printWriter.println(string);
		printWriter.println(CommunicationCommands.com_end_command.command);
	}
	
	public synchronized static void sendSerializedCommand(CommandHolder commandHolder, PrintWriter printWriter){
		String string = new Gson().toJson(commandHolder, new TypeToken<CommandHolder>(){}.getType());
		printWriter.println(string);
		printWriter.println(CommunicationCommands.com_end_command.command);
	}
}
