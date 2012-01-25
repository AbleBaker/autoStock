/**
 * 
 */
package com.autoStock.comServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.autoStock.Co;
import com.autoStock.com.CommandHolder;
import com.autoStock.comClient.CommandSender;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Kevin Kowalewski
 *
 */
public class ConnectionServer {
	public static final String EndCommunication = "QUIT";
	public static final String EndCommand = "END";
	
	public void startServer (){
		while (true){
			ServerSocket server = null;
			Socket incoming = null;
			BufferedReader in = null;
			PrintWriter out = null;
			String receivedLine = new String();
			String receivedString = new String();
			
		    try {server = new ServerSocket(8888, 5, InetAddress.getByName("127.0.0.1"));}catch (Exception e){e.printStackTrace();}
		    try {incoming = server.accept();}catch(Exception e){e.printStackTrace();}
		    
		    try {in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));}catch(Exception e){e.printStackTrace();}
		    try {out = new PrintWriter(incoming.getOutputStream(), true);}catch(Exception e){e.printStackTrace();}
		   
			while (true) {
				try {receivedLine = in.readLine();}catch(IOException e){e.printStackTrace();}
				//System.out.println("Got line: " + receivedLine);
				if (receivedLine == null){
					break;
				}
				if (receivedLine.trim().equals(EndCommunication)){
					try {server.close();}catch(IOException e){e.printStackTrace();}
					break;
				}
				else if (receivedLine.trim().equals(EndCommand)){
					new CommandReceiver().receiveGsonString(receivedString);
					receivedString = new String();
				}
				else {
					receivedString = receivedString.concat(receivedLine);
				}
			}
			
			try {server.close();}catch(Exception e){e.printStackTrace();}
			//Co.println("Connection Closed. New server!");
		}
	}
}
