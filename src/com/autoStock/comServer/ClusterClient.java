/**
 * 
 */
package com.autoStock.comServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;

import com.autoStock.Co;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.comServer.CommunicationDefinitions.CommunicationCommands;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class ClusterClient {
	private Socket clientSocket;
	public PrintWriter printWriter;
	private ListenerOfCommandHolderResult listener;
	
	public ClusterClient(ListenerOfCommandHolderResult listener){
		this.listener = listener;
	}
	
	public void startClient(){
	    try {this.clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 8888);}catch (Exception e){e.printStackTrace();}
	    try {this.printWriter = new PrintWriter(clientSocket.getOutputStream(), true);}catch (Exception e){e.printStackTrace();}
	    
	    listenForResponse();
	}
	
	public void stop(){
		try {this.printWriter.flush();}catch(Exception e){e.printStackTrace();}
		try {this.clientSocket.close();}catch(Exception e){e.printStackTrace();}
	}
	
	private void listenForResponse(){
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				BufferedReader in = null;
				String receivedString = new String();
				String receivedLine = new String();
				
				try {
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					while (true) {
						try {
							receivedLine = in.readLine();
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
//						Co.println("Got line: " + receivedLine);
						
						if (receivedLine.trim().equals(CommunicationCommands.com_end_communication.command)) {
							return;
						} else if (receivedLine.trim().equals(CommunicationCommands.com_end_command.command)) {
							CommandHolder commandHolderGeneric = new CommandReceiver().receiveGsonString(receivedString);
							
							if (commandHolderGeneric.command == Command.compute_unit_backtest){
								Type type = new TypeToken<CommandHolder<ComputeUnitForBacktest>>(){}.getType();
								CommandHolder commandHolderTyped = new Gson().fromJson(receivedString, type);
								listener.receivedCommand(commandHolderTyped);
							}else if (commandHolderGeneric.command == Command.no_units_left){
								clientSocket.close();
							}
//							printWriter.println(CommunicationCommands.com_ok_command.command);
							receivedString = new String();
						} else {
							receivedString = receivedString.concat(receivedLine);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
				
		});
		
		thread.start();
	}
}
