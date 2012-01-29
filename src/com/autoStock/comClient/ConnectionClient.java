/**
 * 
 */
package com.autoStock.comClient;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.autoStock.com.CommandDefinitions.Command;
import com.autoStock.com.CommandHolder;
import com.autoStock.comServer.ConnectionServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class ConnectionClient {
	Socket clientSocket;
	PrintWriter printWriter;
	
	public void startClient(){
	    try {this.clientSocket = new Socket(InetAddress.getByName("127.0.0.1"), 8888);}catch (Exception e){e.printStackTrace();}
	    try {this.printWriter = new PrintWriter(clientSocket.getOutputStream(), false);}catch (Exception e){e.printStackTrace();}
	}
	
	public void stop(){
		try {
			this.printWriter.flush();
		}catch(Exception e){e.printStackTrace();}
		try {this.clientSocket.close();}catch(Exception e){e.printStackTrace();}
	}
	
	public void sendSerializedCommand(Command command){
		String string = new Gson().toJson(new CommandSender().sendCommand(command), new TypeToken<CommandHolder>(){}.getType());
		this.printWriter.println(string);
		this.printWriter.println(ConnectionServer.EndCommand);
		this.printWriter.flush();
	}
	
	public void sendSerializedCommand(Command command, Object... params){
		String string = new Gson().toJson(new CommandSender().sendCommand(command, params), new TypeToken<CommandHolder>(){}.getType());
		this.printWriter.println(string);
		this.printWriter.println(ConnectionServer.EndCommand);
		this.printWriter.flush();
	}
}
