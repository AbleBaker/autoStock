package com.autoStock.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.finance.Account;
import com.autoStock.position.PositionManager;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmManager {
	private ArrayList<ActiveAlgorithmContainer> listOfActiveAlgorithmContainer = new ArrayList<ActiveAlgorithmContainer>();
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
				ActiveAlgorithmContainer container = new ActiveAlgorithmContainer(false, exchange, new Symbol(symbol));
				container.activate();
				listOfActiveAlgorithmContainer.add(container);
			}
		}
	}
	
	public void pruneListOfSymbols(ArrayList<String> listOfSymbols, Exchange exchange){
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			if (listOfSymbols.contains(container.symbol.symbol) == false){
				Co.println("--> No longer want: " + container.symbol.symbol);
				container.deactivate();
				iterator.remove();
			}
		}
	}
	
	public void warnAll(ExchangeState exchangeState){
		//No-op
	}
	
	public void stopAll(){
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			container.deactivate();
		}
	}
	
	private ActiveAlgorithmContainer getAlgorithmContainerForSymbol(String symbol){
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			if (container.symbol.symbol.equals(symbol)){
				return container;
			}
		}
		
		return null;
	}
	
	public void displayAlgorithmTable(){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
		
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			
			ArrayList<String> columnValues = new ArrayList<String>();
			Position position = PositionManager.instance.getPosition(container.symbol.symbol);
			
			double percentGainFromAlgorithm = 0;
			double percentGainFromPosition = 0;
			
			if (container.algorithm.firstQuoteSlice != null && container.algorithm.currentQuoteSlice != null){
				if (container.algorithm.firstQuoteSlice.priceClose != 0 && container.algorithm.currentQuoteSlice.priceClose != 0){
					percentGainFromAlgorithm = (container.algorithm.currentQuoteSlice.priceClose / container.algorithm.firstQuoteSlice.priceClose) -1d;
				}
			}
			
			if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
				if (position.price != 0 && position.lastKnownPrice != 0){
					percentGainFromPosition = (position.lastKnownPrice / position.price);
				}
			}
			
			columnValues.add(container.algorithm.currentQuoteSlice != null && container.algorithm.currentQuoteSlice.dateTime != null ? DateTools.getPrettyDate(container.algorithm.currentQuoteSlice.dateTime) : "?"); 
			columnValues.add(container.symbol.symbol);
			columnValues.add(container.algorithm.signal.currentSignalPoint.name() + ", " + container.algorithm.signal.getCombinedSignal().strength);
			columnValues.add(position == null ? "-" : position.positionType.name());
			columnValues.add(String.valueOf(container.algorithm.firstQuoteSlice == null ? 0 : MathTools.round(container.algorithm.firstQuoteSlice.priceClose)));
			columnValues.add(String.valueOf(container.algorithm.firstQuoteSlice == null ? 0 : MathTools.round(container.algorithm.currentQuoteSlice.priceClose)));
			columnValues.add(String.valueOf(percentGainFromAlgorithm));
			columnValues.add(String.valueOf(percentGainFromPosition));
			
			listOfDisplayRows.add(columnValues);
		}
		
		if (listOfDisplayRows.size() > 0){
			new TableController().displayTable(AsciiTables.algorithm_manager, listOfDisplayRows);
		}
	}
	
	public void displayEndOfDayStats(){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
		
		Co.println("--> Account balance, transactions, fees paid" + Account.instance.getBankBalance() + ", " + Account.instance.getTransactions() + ", " + Account.instance.getTransactionFeesPaid());
	}
}
