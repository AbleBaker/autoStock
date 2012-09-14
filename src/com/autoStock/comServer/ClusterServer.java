/**
 * 
 */
package com.autoStock.comServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ClusterServer {
	public static enum CommunicationCommands {
		com_end_communication("COM_END_COMMUNICATION"),
		com_end_command("COM_END_COMMAND"),
		;
		
		public String command;
		
		CommunicationCommands(String command){
			this.command = command;
		}
	}

	public void startServer() {
		ServerSocket server = null;
		Socket incoming = null;

		try {
			server = new ServerSocket(8888, 128, InetAddress.getByName("127.0.0.1"));
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
			cs.run();
		}
	}

	private class ClientThread extends Thread {
		private Socket socket;

		public ClientThread(Socket socket) {
			super();
			this.socket = socket;
			start();
		}

		@Override
		public void run() {
			BufferedReader in = null;
			PrintWriter out = null;
			String receivedLine = new String();
			String receivedString = new String();

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

			while (true) {
				try {
					receivedLine = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Got line: " + receivedLine);
				if (receivedLine == null) {
					break;
				}
				if (receivedLine.trim().equals(CommunicationCommands.com_end_communication.command)) {
					return;
				} else if (receivedLine.trim().equals(CommunicationCommands.com_end_communication.command)) {
					new CommandReceiver().receiveGsonString(receivedString);
					out.println(CommunicationCommands.com_end_command.command);
					receivedString = new String();
				} else {
					receivedString = receivedString.concat(receivedLine);
				}
			}
		}
	}
}
