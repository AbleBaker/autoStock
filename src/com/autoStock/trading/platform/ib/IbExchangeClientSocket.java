/**
 * 
 */
package com.autoStock.trading.platform.ib;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.autoStock.Co;
import com.autoStock.internal.Config;
import com.autoStock.trading.platform.ib.core.AnyWrapper;
import com.autoStock.trading.platform.ib.core.EClientSocket;

/**
 * @author Kevin Kowalewski
 *
 */
public class IbExchangeClientSocket {
	public EClientSocket eClientSocket;
	public Socket socket;
	
	public void init() throws UnknownHostException, IOException{
		socket = new Socket(Config.plIbTwsHost, Config.plIbTwsPort);
		eClientSocket = new EClientSocket(new IbExchangeWrapper());
	}
	
	public void connect() throws IOException{
		eClientSocket.eConnect(socket, new Random().nextInt());
	}
}
