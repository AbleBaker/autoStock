package com.autoStock;

import java.sql.SQLException;

import com.autoStock.com.CommandHolder.Command;
import com.autoStock.comClient.ConnectionClient;
import com.autoStock.database.DatabaseTest;
import com.autoStock.exchange.request.RequestHistoricalDataListener;
import com.autoStock.exchange.request.RequestHolder;
import com.autoStock.exchange.request.RequestInformation;
import com.autoStock.exchange.request.RequestListener;
import com.autoStock.exchange.request.RequestManager;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.trading.platform.ib.IbEquityInformation;
import com.autoStock.trading.platform.ib.IbExchangeInstance;
import com.autoStock.trading.platform.ib.IbExchangeManager;
import com.autoStock.trading.platform.ib.core.AnyWrapper;
import com.autoStock.trading.platform.ib.core.EClientSocket;
import com.autoStock.trading.platform.ib.tws.TWSSupervisor;
import com.bethecoder.ascii_table.ASCIITable;
import com.qutoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {
	public static void main(String[] args) throws SQLException {
		Co.println("Welcome to autoStock\n");
		
		ApplicationStates.startup();
	
		//new MenuController().displayMenu(MenuStructures.menu_main);


		//ASCIITable.getInstance().printTable(header, data);
		
		new IbExchangeManager();
		IbExchangeManager.getIbExchangeInstance();
		
		new RequestInformation().requestHistoricalData(new RequestHolder(null), new RequestHistoricalDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			@Override
			public void completed(RequestHolder requestHolder) {
				
			}
		});
		
//		IbExchangeInstance ibExchangeInstance = new IbExchangeInstance();
//		ibExchangeInstance.init();
//		ibExchangeInstance.getQuote(null);
		

		//new TWSSupervisor().launchTws();
		//new DatabaseTest().test();
//				
//		ConnectionClient connectionClient = new ConnectionClient();
//		connectionClient.startClient();		
//		connectionClient.sendSerializedCommand(Command.testSleep);	
//		//connectionClient.stop();
		
		new RequestInformation().requestHistoricalData(new RequestHolder(null), new RequestHistoricalDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder) {
				
			}
		});
		
		Co.println("Waiting for callbacks...");
		try{Thread.sleep(3000);}catch(Exception e){}
		Co.println("\n Done \n");
		System.exit(0);
		
	}
}
