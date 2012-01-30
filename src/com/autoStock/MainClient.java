package com.autoStock;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.autoStock.com.CommandDefinitions.Command;
import com.autoStock.comClient.ConnectionClient;
import com.autoStock.database.DatabaseTest;
import com.autoStock.exchange.request.RequestHistoricalData;
import com.autoStock.exchange.request.RequestHistoricalDataListener;
import com.autoStock.exchange.request.RequestHolder;
import com.autoStock.exchange.request.RequestListener;
import com.autoStock.exchange.request.RequestManager;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.internal.Global.Mode;
import com.autoStock.menu.MenuController;
import com.autoStock.menu.MenuDefinitions.MenuStructures;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.Tables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.platform.ib.IbEquityInformation;
import com.autoStock.trading.platform.ib.IbExchangeInstance;
import com.autoStock.trading.platform.ib.IbExchangeManager;
import com.autoStock.trading.platform.ib.core.AnyWrapper;
import com.autoStock.trading.platform.ib.core.EClientSocket;
import com.autoStock.trading.platform.ib.tws.TWSSupervisor;
import com.autoStock.trading.results.ExResultHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultRowHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.types.TypeHistoricalData;
import com.bethecoder.ascii_table.ASCIITable;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClient {	
	public static void main(String[] args) throws SQLException {
		Global.mode = Mode.client;
		Co.println("Welcome to autoStock\n");
		
		ApplicationStates.startup();
	
		//new MenuController().displayMenu(MenuStructures.menu_main);


		//ASCIITable.getInstance().printTable(header, data);
//		
//		new IbExchangeManager();
//		IbExchangeManager.getIbExchangeInstance();
		
//		new RequestInformation().requestHistoricalData(new RequestHolder(null), new RequestHistoricalDataListener() {
//			@Override
//			public void failed(RequestHolder requestHolder) {
//				
//			}
//			@Override
//			public void completed(RequestHolder requestHolder) {
//				
//			}
//		});
		
//		ibExchangeInstance = new IbExchangeInstance();
//		ibExchangeInstance.init();
	//	ibExchangeInstance.getQuote(null);
				
		new RequestHistoricalData(new RequestHolder(null), new RequestHistoricalDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetHistoricalData exResultSetHistoricalData) {
				
				ArrayList<ArrayList<String>> listOfRows = new ArrayList<ArrayList<String>>();
				
				ExResultRowHistoricalData lastRow = exResultSetHistoricalData.listOfExResultRowHistoricalData.get(0);
				
				for (ExResultRowHistoricalData exResultRowHistoricalData : exResultSetHistoricalData.listOfExResultRowHistoricalData){
					ArrayList<String> listOfColumnValues = new ArrayList<String>();
					listOfColumnValues.add(exResultSetHistoricalData.typeHistoricalData.symbol);
					listOfColumnValues.add(DateTools.getPrettyDate(exResultRowHistoricalData.date));
					listOfColumnValues.add(String.valueOf(exResultRowHistoricalData.price));
					listOfColumnValues.add(String.valueOf(MathTools.roundToTwoDecimalPlaces(exResultRowHistoricalData.price - lastRow.price)));
					listOfRows.add(listOfColumnValues);
					
					lastRow = exResultRowHistoricalData;
				}
				
				new TableController().displayTable(Tables.equity_historical_price_live, listOfRows);
				Co.println("Completed!!!");
			}
		}, new TypeHistoricalData("AAPL", null, null));
		

		//new TWSSupervisor().launchTws();
		//new DatabaseTest().test();
		
//		Date startDate = null;
//		try {
//			startDate = DateFormat.getInstance().parse("20120109 10:30:00 AM EST"); 
//		}catch(Exception e){}
//		
//		Date endDate = null;
//		try {
//			startDate = DateFormat.getInstance().parse("20120109 10:35:00 AM EST"); 
//		}catch(Exception e){}
//
//		ConnectionClient connectionClient = new ConnectionClient();
//		connectionClient.startClient();		
//		connectionClient.sendSerializedCommand(Command.client_ex_request_historical_data, new RequestHistoricalData(new RequestHolder(null), new RequestHistoricalDataListener() {
//			@Override
//			public void failed(RequestHolder requestHolder) {
//				
//			}
//			
//			@Override
//			public void completed(RequestHolder requestHolder, ExResultSetHistoricalData exResultSetHistoricalData) {
//				Co.println("Completed!!!");
//			}
//		}, new TypeHistoricalData("AAPL", null, null)));
		
		
		//connectionClient.stop();
		
		Co.println("Waiting for callbacks...");
		try{Thread.sleep(60*1000);}catch(Exception e){}
		Co.println("\n Done \n");
		System.exit(0);
		
	}
}
