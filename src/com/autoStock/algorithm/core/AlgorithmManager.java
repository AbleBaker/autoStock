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
				algorithmInfoManager.activatedSymbol(symbol);
			}
		}
	}
	
	public void pruneListOfSymbols(ArrayList<String> listOfSymbols, Exchange exchange){
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			if (listOfSymbols.contains(container.symbol.symbol) == false){
				Co.println("--> No longer want: " + container.symbol.symbol);
				container.deactivate();
				algorithmInfoManager.deactivatedSymbol(container.symbol.symbol);
				iterator.remove();
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
		}
		
		if (PositionManager.instance.getPositionListSize() > 0){
			throw new IllegalStateException("Position manager still has: " + PositionManager.instance.getPositionListSize() + " positions...");
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
		new TableController().displayTable(AsciiTables.algorithm_manager, getAlgorithmTable());
		Co.println("--> Current P&L: " + PositionManager.instance.getCurrentProfitLossIncludingFees());
	}
	
	public ArrayList<ArrayList<String>> getAlgorithmTable(){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
		
		for (Iterator<ActiveAlgorithmContainer> iterator = listOfActiveAlgorithmContainer.iterator(); iterator.hasNext();){
			ActiveAlgorithmContainer container = iterator.next();
			
			ArrayList<String> columnValues = new ArrayList<String>();
			Position position = PositionManager.instance.getPosition(container.symbol.symbol);
			
			double percentGainFromAlgorithm = 0;
			double percentGainFromPosition = 0;
			
			if (container.algorithm.firstQuoteSlice != null && container.algorithm.getCurrentQuoteSlice() != null){
				if (container.algorithm.firstQuoteSlice.priceClose != 0 && container.algorithm.getCurrentQuoteSlice().priceClose != 0){
					percentGainFromAlgorithm = (container.algorithm.getCurrentQuoteSlice().priceClose / container.algorithm.firstQuoteSlice.priceClose) -1d;
				}
			}
			
			if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
				if (position.price != 0 && position.lastKnownPrice != 0){
					percentGainFromPosition = (position.lastKnownPrice / position.price);
				}
			}
			
			columnValues.add(container.algorithm.getCurrentQuoteSlice() != null && container.algorithm.getCurrentQuoteSlice().dateTime != null ? DateTools.getPrettyDate(container.algorithm.getCurrentQuoteSlice().dateTime) : "?"); 
			columnValues.add(container.symbol.symbol);
			columnValues.add(container.algorithm.strategy.lastStrategyResponse == null ? "-" : (container.algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.name() + ", " + container.algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name()));
			columnValues.add(position == null ? "-" : position.positionType.name() + ", " + position.getPositionProfitLossAfterComission());
			columnValues.add(String.valueOf(container.algorithm.getFirstQuoteSlice() == null ? 0 : MathTools.round(container.algorithm.getFirstQuoteSlice().priceClose)));
			columnValues.add(String.valueOf(container.algorithm.getCurrentQuoteSlice() == null ? 0 : MathTools.round(container.algorithm.getCurrentQuoteSlice().priceClose)));
			columnValues.add(String.valueOf(percentGainFromAlgorithm));
			columnValues.add(String.valueOf(percentGainFromPosition == 1 ? "-" : percentGainFromPosition));
			columnValues.add(String.valueOf(position == null ? "-" : position.getPositionProfitLossAfterComission()));
			
			listOfDisplayRows.add(columnValues);
		}
		
		return listOfDisplayRows;
	}
	
	public void displayEndOfDayStats(ArrayList<ArrayList<String>> listOfAlgorithmDisplayRows){
		Co.println("--> Account balance, transactions, fees paid: " + Account.instance.getBankBalance() + ", " + Account.instance.getTransactions() + ", " + Account.instance.getTransactionFeesPaid());
		new TableController().displayTable(AsciiTables.algorithm_manager, listOfAlgorithmDisplayRows);
		
		Co.println(new Gson().toJson(algorithmInfoManager.listOfAlgorithmInfo));
	}
}
