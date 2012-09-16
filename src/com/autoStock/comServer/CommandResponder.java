/**
 * 
 */
package com.autoStock.comServer;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import com.autoStock.Co;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.cluster.ComputeUnitSupplier;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.comServer.CommunicationDefinitions.CommunicationCommands;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class CommandResponder {	
	public void receivedCommand(CommandHolder commandHolder, PrintWriter printWriter){
		Co.println("--> Responding to command: " + commandHolder.command.name());
		
		if (commandHolder.command == Command.accept_unit){
			CommandSerializer.sendSerializedCommand(new CommandHolder(Command.compute_unit_backtest, ComputeUnitSupplier.getInstance().getNextComputeUnit()), printWriter);
		}
		
		printWriter.flush();
	}
}
