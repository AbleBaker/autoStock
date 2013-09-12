package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentCampaignProvider;
import com.autoStock.adjust.AdjustmentIdentifier;
import com.autoStock.adjust.AdjustmentSeriesForAlgorithm;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.DummyAlgorithm;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestContainer;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.BacktestEvaluator;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterServer;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.guage.SignalGuage;
import com.autoStock.internal.Global;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.tools.Benchmark;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktest implements ListenerOfCommandHolderResult {
	private static MainClusteredBacktest instance;
	private ArrayList<Long> listOfComputeUnitResultIds = new ArrayList<Long>();
	private ArrayList<String> listOfSymbols;
	private Exchange exchange;
	private ClusterServer clusterServer;
	private AdjustmentCampaignProvider adjustmentCampaignProvider = AdjustmentCampaignProvider.getInstance();
	private AtomicLong atomicIntForRequestId = new AtomicLong();
	private Date dateStart;
	private Date dateEnd;
	private final int computeUnitIterationSize = 64;
	private Benchmark bench = new Benchmark();
	private Benchmark benchTotal = new Benchmark();
	private Thread threadForWatcher;
	private AlgorithmBase algorithm = new DummyAlgorithm(null, null, AlgorithmMode.mode_backtest_with_adjustment, new BasicAccount(0));
	public final BacktestEvaluator backtestEvaluator = new BacktestEvaluator();
	
	public MainClusteredBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		instance = this;
		
		Global.callbackLock.requestLock();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		for (String string : listOfSymbols){
			AdjustmentCampaign adjustmentCampaign = new AdjustmentSeriesForAlgorithm(algorithm);
			adjustmentCampaign.initialize();
			adjustmentCampaignProvider.addAdjustmentCampaignForAlgorithm(adjustmentCampaign, new Symbol(string, SecurityType.type_stock));
		}
		
		algorithm.initialize();
		
		startRequestServer();
	}
	
	
	public static MainClusteredBacktest getInstance(){
		return instance;
	}
	
	private void startRequestServer(){
		clusterServer = new ClusterServer(this);
		clusterServer.startServer();
	}
	
	public synchronized ComputeUnitForBacktest getNextComputeUnit(){
		ArrayList<AlgorithmModel> listOfAlgorithmModel = new ArrayList<AlgorithmModel>();
		
		Co.println("--> Generating unit...");
		
		if (benchTotal.hasTicked == false){
			benchTotal.tick();
		}
		
		int addedUnits = 0;
		
		for (Pair<AdjustmentIdentifier, AdjustmentCampaign> pair : adjustmentCampaignProvider.getListOfAdjustmentCampaign()){
			
			Co.println("--> Has more? " + pair.second.hasMore());
			
			listOfAlgorithmModel.clear();
			
			while (pair.second.hasMore() && addedUnits < computeUnitIterationSize){
				if (atomicIntForRequestId.get() == 0 && addedUnits == 0){
					pair.second.applyValues();
					listOfAlgorithmModel.add(getCurrentAlgorithmModel());
				} else {
					pair.second.runAdjustment();	
					listOfAlgorithmModel.add(getCurrentAlgorithmModel());
				}
				
				addedUnits++;
			}
		}
		
		Co.println("--> Issued unit: " + atomicIntForRequestId.get());
		
		bench.tick();
		
		if (listOfAlgorithmModel.size() > 0){
			return new ComputeUnitForBacktest(atomicIntForRequestId.getAndIncrement(), listOfAlgorithmModel, exchange, listOfSymbols, dateStart, dateEnd);
		}else{
			Co.println("--> No units left...");
			if (threadForWatcher == null) {startWatcher();}
			return null;
		}
	}
	
	private AlgorithmModel getCurrentAlgorithmModel(){
		ArrayList<SignalParameters> listOfSignalParameters = new ArrayList<SignalParameters>();
		
		for (SignalBase signalBase : algorithm.signalGroup.getListOfSignalBase()){
			listOfSignalParameters.add(signalBase.signalParameters.copy());
		}
		
//		Co.println("--> Send parameters");
//		
//		for (SignalParameters signalParameter : listOfSignalParameters){
//			if (signalParameter.normalizeInterface != null){
//				Co.println("--> " + signalParameter.arrayOfSignalGuageForLongEntry[0].threshold);
//				Co.println("--> " + signalParameter.arrayOfSignalGuageForLongExit[0].threshold);
//			}
//		}
		
		return new AlgorithmModel(algorithm.strategyBase.strategyOptions, listOfSignalParameters);
	}
	
	private void startWatcher() {
		threadForWatcher = new Thread(new Runnable() {
			@Override
			public void run() {
				int waitCount = 5;

				while (isComplete() == false) {
					try {Thread.sleep(1000);}catch(InterruptedException e){return;}
					if (waitCount == 0) {
						displayResultTable();
						Co.println("--> Warning: compute unit(s) went missing... Sent / Received: " + atomicIntForRequestId.get() + ", " + listOfComputeUnitResultIds.size());
						Global.callbackLock.releaseLock();
					}else{
						Co.println("--> Waiting..." + waitCount);
					}

					waitCount--;
				}
			}
		});
		threadForWatcher.start();
	}


	public void displayResultTable(){		
		Co.println("--> ********************* ********************* Clustered Backtest Results ********************* *********************");
		
		backtestEvaluator.pruneForFinish();
		backtestEvaluator.reverse();
		
		for (String string : listOfSymbols){
			Symbol symbol = new Symbol(string, SecurityType.type_stock);
			Co.println("--> SYMBOL BACKTEST: " + symbol.symbolName);
			for (BacktestEvaluation backtestEvaluation : backtestEvaluator.getResults(symbol)){
				Co.println("\n\n--> String representation: " + backtestEvaluation.toString());
			}
			
			BacktestEvaluation bestEvaluation = backtestEvaluator.getResults(symbol).get(backtestEvaluator.getResults(symbol).size() -1);
			
			new BacktestEvaluationWriter().writeToDatabase(bestEvaluation, true);
		}
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		Co.println("--> Received command: " + commandHolder.command);
		
		ComputeResultForBacktest computeResultForBacktest = (ComputeResultForBacktest) commandHolder.commandParameters;
		
		listOfComputeUnitResultIds.add(computeResultForBacktest.requestId);
		
		for (BacktestEvaluation backtestEvaluation : computeResultForBacktest.listOfBacktestEvaluation){
			backtestEvaluator.addResult(backtestEvaluation.symbol, backtestEvaluation, true);
		}
	}
	
	public boolean isComplete(){
		return false;
	}
}