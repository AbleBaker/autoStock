package com.autoStock.algorithm.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.exchange.results.MultipleResultMarketScanner.MultipleResultRowMarketScanner;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionManager;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
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
	
	public boolean setListOfSymbols(ArrayList<MultipleResultRowMarketScanner> listOfMultipleResultRowMarketScanner, Exchange exchange){
		for (MultipleResultRowMarketScanner result : listOfMultipleResultRowMarketScanner){
			if (listOfActiveAlgorithmContainer.size() >= 100){
				Co.println("--> Reached market data concurrent request limit. Not adding symbol: " + result.marketScannerType.name() + ", " + result.symbol);
				return false;
			}else if (getAlgorithmContainerForSymbol(result.symbol, exchange.exchangeName) == null){
				Co.println("Will run algorithm for symbol: " + result.marketScannerType.name() + ", " + result.symbol);
				algorithmInfoManager.activatedSymbol(result.symbol);
				ActiveAlgorithmContainer container = new ActiveAlgorithmContainer(false, exchange, new Symbol(result.symbol));
				container.activate();
				listOfActiveAlgorithmContainer.add(container);
			}
		}
		
		Co.println("Active algorithm count: " + listOfActiveAlgorithmContainer.size());
		
		return true;
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
		
		if (threadForDisplay != null){
			threadForDisplay.interrupt();
		}
		
		deactivateAll();
		
		int count = 0;
		
		while (PositionManager.getInstance().getPositionListSize() > 0){
			try {Thread.sleep(1000);}catch(InterruptedException e){return;}
			Co.println("Position manager still has: " + PositionManager.getInstance().getPositionListSize() + " positions..." + count);
			if (count == 30){
				Co.println("Warning, position(s) still not exited!");
				deactivateAll();
			}else if (count > 60){
				Co.println("Failed to exit all positions.");
				return;
			}
		}
	}
	
	private void deactivateAll(){
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			container.deactivate();
			algorithmInfoManager.deactivatedSymbol(container.symbol.symbolName);
		}
	}
	
	private ActiveAlgorithmContainer getAlgorithmContainerForSymbol(String symbol, String exchange){
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			if (container.symbol.symbolName.equals(symbol) && container.exchange.exchangeName.equals(exchange)){
				return container;
			}
		}
		
		return null;
	}
	
	public void displayAlgorithmTable(){
		new TableController().displayTable(AsciiTables.algorithm_manager, getAlgorithmTable());
		Co.println("--> Current entered position P&L: " + PositionManager.getInstance().getCurrentProfitLossBeforeComission() + " / " + PositionManager.getInstance().getCurrentProfitLossAfterComission(false));
		Co.println("--> Current fees paid: " + Account.getInstance().getTransactionFeesPaid());
		Co.println("--> Current account balance: " + Account.getInstance().getAccountBalance());
		Co.println("--> All position value including fees: " + PositionManager.getInstance().getAllPositionValueIncludingFees()); 
		Co.println("--> Complete gain from starting account balance: $" + new DecimalFormat("#.###").format((Account.getInstance().getAccountBalance() + PositionManager.getInstance().getAllPositionValueIncludingFees()) - Account.getInstance().bankBalanceDefault));
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
		Co.println("--> Account balance, transactions, fees paid: " + Account.getInstance().getAccountBalance() + ", " + Account.getInstance().getTransactions() + ", " + Account.getInstance().getTransactionFeesPaid());
		new TableController().displayTable(AsciiTables.algorithm_manager, listOfAlgorithmDisplayRows);
		
		Co.println(new Gson().toJson(algorithmInfoManager.listOfAlgorithmInfo));
	}
}
