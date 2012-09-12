package com.autoStock.algorithm.core;

import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalTools;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmManager {
	private ArrayList<ActiveAlgorithmContainer> listOfActiveAlgorithmContainer = new ArrayList<ActiveAlgorithmContainer>();
	private AlgorithmInfoManager algorithmInfoManager = new AlgorithmInfoManager();
	private AlgorithmManagerTable algorithmManagerTable = new AlgorithmManagerTable();
	private Thread threadForDisplay;
	
	public void initalize() {
		threadForDisplay = new Thread(new Runnable(){
			@Override
			public void run() {
				try{Thread.sleep(1000 * 5);}catch(InterruptedException e){}
				while (true){
					try{Thread.sleep(1000 * Resolution.min.seconds);}catch(InterruptedException e){throw new IllegalStateException();}
					displayAlgorithmTable();
				}
			}
		});
		
		threadForDisplay.start();
	}
	
	public void setListOfSymbols(ArrayList<String> listOfSymbols, Exchange exchange){
		for (String symbol : listOfSymbols){
			if (getAlgorithmContainerForSymbol(symbol) == null){
				Co.println("Will run algorithm for symbol: " + symbol);
				algorithmInfoManager.activatedSymbol(symbol);
				ActiveAlgorithmContainer container = new ActiveAlgorithmContainer(false, exchange, new Symbol(symbol));
				container.activate();
				listOfActiveAlgorithmContainer.add(container);
			}
		}
	}
	
	public void pruneListOfSymbols(ArrayList<String> listOfSymbols, Exchange exchange){
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			if (listOfSymbols.contains(container.symbol.symbolName) == false){
				Co.println("--> No longer want: " + container.symbol.symbolName);
				algorithmInfoManager.deactivatedSymbol(container.symbol.symbolName);
				container.deactivate();
				iterator.remove();
			}
		}
	}
	
	public void algorithmSweep(){
		for (ActiveAlgorithmContainer activeAlgorithmContainer : listOfActiveAlgorithmContainer){
			if (activeAlgorithmContainer.algorithm.algorithmState.isDisabled){
				activeAlgorithmContainer.requestMarketData.cancel();
			}
		}
	}
	
	public void warnAll(ExchangeState exchangeState){
		//No-op
	}
	
	public void stopAll(){
		Co.println("--> STOP ALL!!!");
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			container.deactivate();
			algorithmInfoManager.deactivatedSymbol(container.symbol.symbolName);
		}
		
		if (PositionManager.instance.getPositionListSize() > 0){
			throw new IllegalStateException("Position manager still has: " + PositionManager.instance.getPositionListSize() + " positions...");
		}
	}
	
	private ActiveAlgorithmContainer getAlgorithmContainerForSymbol(String symbol){
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			if (container.symbol.symbolName.equals(symbol)){
				return container;
			}
		}
		
		return null;
	}
	
	public void displayAlgorithmTable(){
		new TableController().displayTable(AsciiTables.algorithm_manager, getAlgorithmTable());
		Co.println("--> Current P&L: " + PositionManager.instance.getCurrentProfitLossIncludingFees());
		Co.println("--> Current account balance: " + Account.instance.getAccountBalance());
	}
	
	public ArrayList<ArrayList<String>> getAlgorithmTable(){
		algorithmManagerTable.clear();
		
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			
			algorithmManagerTable.addRow(container.algorithm, container.algorithm.listOfQuoteSlice);
		}
		
		return algorithmManagerTable.getListOfDisplayRows();
	}
	
	public void displayEndOfDayStats(ArrayList<ArrayList<String>> listOfAlgorithmDisplayRows){
		Co.println("--> Account balance, transactions, fees paid: " + Account.instance.getAccountBalance() + ", " + Account.instance.getTransactions() + ", " + Account.instance.getTransactionFeesPaid());
		new TableController().displayTable(AsciiTables.algorithm_manager, listOfAlgorithmDisplayRows);
		
		Co.println(new Gson().toJson(algorithmInfoManager.listOfAlgorithmInfo));
	}
}
