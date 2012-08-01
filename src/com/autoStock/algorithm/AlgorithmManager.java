package com.autoStock.algorithm;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.external.AlgorithmCondition;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.position.PositionManager;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
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
				while (true){
					try{Thread.sleep(1000 * Resolution.min.seconds);}catch(InterruptedException e){return;}
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
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			if (listOfSymbols.contains(container.symbol.symbol)){
				//pass
			}else{
				Co.println("--> No longer want: " + container.symbol.symbol);
				container.algorithm.endOfFeed(container.symbol);
			}
		}
	}
	
	public void displayAlgorithmTable(){
		ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
		
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			ArrayList<String> columnValues = new ArrayList<String>();
			Position position = PositionManager.instance.getPosition(container.symbol.symbol);
			
			columnValues.add(DateTools.getPrettyDate(new Date())); 
			columnValues.add(container.symbol.symbol);
			columnValues.add(container.algorithm.signal.currentSignalPoint.name() + ", " + container.algorithm.signal.getCombinedSignal().strength);
			columnValues.add(position == null ? "-" : position.positionType.name());
			
			listOfDisplayRows.add(columnValues);
		}
		
		new TableController().displayTable(AsciiTables.algorithm_manager, listOfDisplayRows);
	}
	
	public void warnAll(ExchangeState exchangeState){
		//No-op
	}
	
	public void stopAll(){
		for (ActiveAlgorithmContainer container : listOfActiveAlgorithmContainer){
			container.algorithm.endOfFeed(container.symbol);
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
}
