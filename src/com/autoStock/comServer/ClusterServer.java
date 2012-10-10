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
import java.net.ServerSocket;
import java.net.Socket;

import com.autoStock.Co;
import com.autoStock.cluster.ComputeResultForBacktest;
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
public class ClusterServer {
	private ListenerOfCommandHolderResult listener;
	private Thread threadForRequestServer;
	
	public ClusterServer(ListenerOfCommandHolderResult listener){
		this.listener = listener;
	}

	public void startServer() {
		threadForRequestServer = new Thread(new Runnable(){
			@Override
			public void run() {
				Co.println("--> Starting server...");
				ServerSocket server = null;
				Socket incoming = null;
		
				try {
					server = new ServerSocket(8888, 8, InetAddress.getByName("0.0.0.0"));
				} catch (Exception e) {
					e.printStackTrace();
				}
		
				while (true) {
					try {
						incoming = server.accept();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ClientThread cs = new ClientThread(incoming);
					cs.start();
					
					try {Thread.sleep(3000);}catch(Exception e){}
				}
			}
		});
		
		threadForRequestServer.start();
	}

	private class ClientThread extends Thread {
		private Socket socket;

		public ClientThread(Socket socket) {
			super();
			this.socket = socket;
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}

		@Override
		public void run() {
			BufferedReader in = null;
			PrintWriter out = null;
			String receivedLine = new String();
			String receivedString = new String();
			 
			Co.println("Instnace: " + socket.toString());

			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				while ((receivedLine = in.readLine()) != null) {
//					Co.println("Got line: " + receivedLine + ", " + receivedString);

					if (receivedLine.trim().equals(CommunicationCommands.com_end_communication.command)) {
						return;
					} else if (receivedLine.trim().equals(CommunicationCommands.com_end_command.command)) {
						CommandHolder commandHolderGeneric = new CommandReceiver().receiveGsonString(receivedString);
						
						if (commandHolderGeneric.command == Command.accept_unit){
							new CommandResponder().receivedCommand(commandHolderGeneric, out);
						}else if (commandHolderGeneric.command == Command.backtest_results){
							Type type = new TypeToken<CommandHolder<ComputeResultForBacktest>>(){}.getType();
							CommandHolder commandHolderTyped = new Gson().fromJson(receivedString, type);
							listener.receivedCommand(commandHolderTyped);
						}
						receivedString = new String();
					} else {
						receivedString = receivedString.concat(receivedLine);
					}
				}
			}catch(IOException e){
//				e.printStackTrace();
				Co.println("--> Client disconnected abruptly...");
				try {socket.close();}catch(Exception ex){}
			}
		}
	}
}
