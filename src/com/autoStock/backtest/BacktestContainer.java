package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentRebaser;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbGson;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.GsonClassAdapter;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalRangeLimit;
import com.autoStock.signal.SignalDefinitions.IndicatorParameters;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 * 
 */
public class BacktestContainer implements ReceiverOfQuoteSlice {
	private final boolean USE_PRECOMPUTED_ALGORITHM_MODEL = true;
	public final Symbol symbol;
	public final Exchange exchange;
	public HistoricalData historicalData;
	public final AlgorithmTest algorithm;
	private ListenerOfBacktestCompleted listener;
	private Backtest backtest;
	private AlgorithmMode algorithmMode;
	private boolean isComplete;
	private BasicAccount basicAccount;
	private ArrayList<DbStockHistoricalPrice> listOfDbHistoricalPrices = new ArrayList<DbStockHistoricalPrice>();
	public ArrayList<StrategyResponse> listOfStrategyResponse = new ArrayList<StrategyResponse>();
	public HashMap<SignalBase, SignalRangeLimit> hashOfSignalRangeLimit = new HashMap<SignalBase, SignalRangeLimit>();
	public ArrayList<Pair<Date, Double>> listOfYield = new ArrayList<Pair<Date, Double>>();
	
	public Date dateContainerStart;
	public Date dateContainerEnd;

	@SuppressWarnings({ "unchecked", "unused" })
	public BacktestContainer(Symbol symbol, Exchange exchange, ListenerOfBacktestCompleted listener, AlgorithmMode algorithmMode) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.listener = listener;
		this.algorithmMode = algorithmMode;
		
		if (algorithmMode == AlgorithmMode.mode_backtest){
			basicAccount = AccountProvider.getInstance().getGlobalAccount();
		}else{
			basicAccount = AccountProvider.getInstance().newAccountForSymbol(symbol);			
		}
		
		algorithm = new AlgorithmTest(exchange, symbol, algorithmMode, basicAccount);

		if (algorithmMode == AlgorithmMode.mode_backtest && USE_PRECOMPUTED_ALGORITHM_MODEL){
			AlgorithmModel algorithmModel = BacktestEvaluationReader.getPrecomputedModel(exchange, symbol);
			if (algorithmModel != null){
				Co.println("--> Evaluation available");
				new AlgorithmRemodeler(algorithm, algorithmModel).remodel(true, true, true, false);
			}
		}
	}

	public void setBacktestData(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice, HistoricalData historicalData){
		this.listOfDbHistoricalPrices = listOfDbStockHistoricalPrice;
		this.historicalData = historicalData;
		
		setContainerDates();
				
		algorithm.init(historicalData.startDate);
		
		Iterator<DbStockHistoricalPrice> iterator = this.listOfDbHistoricalPrices.iterator();
		
		while (iterator.hasNext()){
			DbStockHistoricalPrice dbStockHistoricalPrice = iterator.next();
			
			if (dbStockHistoricalPrice.dateTime.getHours() >= exchange.timeCloseForeign.hours && dbStockHistoricalPrice.dateTime.getMinutes() > exchange.timeCloseForeign.minutes){
				iterator.remove();
			}
		}
	}

	public void runBacktest() {
		if (listOfDbHistoricalPrices.size() == 0) {
//			throw new IllegalStateException();
			endOfFeed(symbol);
			return;
		}
		
		backtest = new Backtest(historicalData, listOfDbHistoricalPrices, symbol);
		backtest.performBacktest(this);
	}
	
	private void setContainerDates(){
		if (dateContainerStart == null){
			dateContainerStart = historicalData.startDate;
		}else{
			dateContainerStart = DateTools.getEarliestDate(Arrays.asList(new Date[]{dateContainerStart, historicalData.startDate}));
		}
		
		if (dateContainerEnd == null){
			dateContainerEnd = historicalData.endDate;
		}else{
			dateContainerEnd = DateTools.getLatestDate(Arrays.asList(new Date[]{dateContainerEnd, historicalData.endDate}));
		}
	}

	public void setListener(ListenerOfBacktestCompleted listenerOfBacktestCompleted) {
		this.listener = listenerOfBacktestCompleted;
	}

	public void reset() {
		listOfStrategyResponse.clear();
		algorithm.basicAccount.reset();
		hashOfSignalRangeLimit.clear();
		listOfYield.clear();
		algorithm.positionGovernor.reset();
		Co.println("******* RESET");
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		algorithm.receiveQuoteSlice(quoteSlice);
	}

	@Override
	public void endOfFeed(Symbol symbol) {
		listOfStrategyResponse.addAll(algorithm.listOfStrategyResponse);
		algorithm.endOfFeed(symbol);
		
		for (SignalBase signalBase : algorithm.signalGroup.getListOfSignalBase()){
			if (signalBase.signalRangeLimit.isSet()){
				if (hashOfSignalRangeLimit.containsKey(signalBase)){
					hashOfSignalRangeLimit.put(signalBase, AdjustmentRebaser.getRangeLimit(Arrays.asList(new SignalRangeLimit[]{hashOfSignalRangeLimit.get(signalBase), signalBase.signalRangeLimit.copy()})));
				}else{
					hashOfSignalRangeLimit.put(signalBase, signalBase.signalRangeLimit.copy());
				}
			}else{
//				Co.println("--> Signal range limit not set: " + signalBase.getClass().getName());
			}
		}
		
		listOfYield.add(new Pair<Date, Double>(historicalData.startDate, algorithm.getYieldCurrent()));
		
		if (PositionManager.getGlobalInstance().getPosition(symbol) != null){
//			Co.println("--> Warning! Exchange data ends before exchange close and a position exists...");
			Position position = PositionManager.getGlobalInstance().getPosition(symbol);
		
			if (position.positionType == PositionType.position_long){
				PositionManager.getGlobalInstance().executePosition(algorithm.getCurrentQuoteSlice(), exchange, algorithm.strategyBase.signal, PositionType.position_long_exit, position, null, basicAccount);
			}else if (position.positionType == PositionType.position_short){
				PositionManager.getGlobalInstance().executePosition(algorithm.getCurrentQuoteSlice(), exchange, algorithm.strategyBase.signal, PositionType.position_short_exit, position, null, basicAccount);
			}else{
				throw new IllegalStateException();
			}
			
//			Co.print(new BacktestEvaluationBuilder().buildEvaluation(this).toString());
//			throw new IllegalStateException("Position manager still has position for: " + symbol.symbolName);
		}
		
		listener.backtestCompleted(symbol, algorithm);
	}

	public void markAsComplete() {
		isComplete = true;
	}

	public void markAsIncomplete() {
		isComplete = false;
	}

	public boolean isIncomplete() {
		return !isComplete;
	}
}
