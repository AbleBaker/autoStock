package com.autoStock;

import java.lang.management.ManagementFactory;
import com.autoStock.comServer.ConnectionServer;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.trading.platform.ib.IbExchangeInstance;
/**
 * @author Kevin Kowalewski
 *
 */
public class MainServer {
	public static volatile String appleState = "Oranges";
	public static Thread runningThread;
	public static IbExchangeInstance ibExchangeInstance;
	
	public static void main(String[] args) {
		Global.mode = Mode.server;
		Co.println("Welcome to autoStock\n");
		
		ibExchangeInstance = new IbExchangeInstance();
		ibExchangeInstance.init();
		
		runningThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try{Thread.sleep(1000);}catch(InterruptedException e){return;}
					Co.println("Apples are: " + appleState);
				}
			}
		});
		
		runningThread.start();

		new ConnectionServer().startServer();
		
		Co.println("Done");
	}
}
