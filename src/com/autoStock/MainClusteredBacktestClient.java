package com.autoStock;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.account.AccountProvider;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterClient;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult, ListenerOfMainBacktestCompleted {
	private ClusterClient clusterClient;
	private ComputeUnitForBacktest computeUnitForBacktest;
	private MainBacktest mainBacktest;
	private ArrayList<BacktestEvaluation> listOfComputeResultForBacktest = new ArrayList<BacktestEvaluation>();

	public MainClusteredBacktestClient() {
		Global.callbackLock.requestLock();
		PositionManager.getInstance().orderMode = OrderMode.mode_simulated;
		
		clusterClient = new ClusterClient(this);
		clusterClient.startClient();
		
		requestNextUnit();
	}
	
	public void requestNextUnit(){
		new CommandSerializer().sendSerializedCommand(Command.accept_unit, clusterClient.printWriter);
	}

	public void runNextBacktest(){
		AccountProvider.getInstance().getGlobalAccount().reset();
		PositionGovernor.getInstance().reset();
		PositionManager.getInstance().reset();
		
//		for (AlgorithmModel algorithmModel : computeUnitForBacktest.listOfAlgorithmModel){
//			Co.println("--> Received parameters");
//
//			for (SignalParameters signalParameter : algorithmModel.listOfSignalParameters){
//				if (signalParameter.normalizeInterface != null){
//					Co.println("--> " + signalParameter.arrayOfSignalGuageForLongEntry[0].threshold);
//					Co.println("--> " + signalParameter.arrayOfSignalGuageForLongExit[0].threshold);
//				}
//			}
//		}
		
//		if (backtestIndex == computeUnitForBacktest.listOfAlgorithmModel.size()){
//			allBacktestsCompleted();
//		}else{
			mainBacktest = new MainBacktest(computeUnitForBacktest.exchange, computeUnitForBacktest.dateStart, computeUnitForBacktest.dateEnd, computeUnitForBacktest.listOfSymbols, BacktestType.backtest_clustered_client, this, computeUnitForBacktest.listOfAlgorithmModel);
//		}
	}
	
	public void allBacktestsCompleted(){
		Co.println("--> All backtests completed...");
		sendBacktestResult();
		requestNextUnit();
	}
	
	public void sendBacktestResult(){
		if (listOfComputeResultForBacktest.size() != computeUnitForBacktest.listOfAlgorithmModel.size()){
			throw new IllegalStateException("Sizes don't match: " + listOfComputeResultForBacktest.size() + ", " + computeUnitForBacktest.listOfAlgorithmModel.size());
		}
		
		CommandHolder<ComputeResultForBacktest> commandHolder = new CommandHolder<ComputeResultForBacktest>(Command.backtest_results, new ComputeResultForBacktest(computeUnitForBacktest.requestId, listOfComputeResultForBacktest));
		new CommandSerializer().sendSerializedCommand(commandHolder, clusterClient.printWriter);
		listOfComputeResultForBacktest.clear();
		mainBacktest.backtestEvaluator.reset();
	}
	
	public void storeBacktestResult(){
//		ArrayList<AdjustmentOfPortable> listOfIteration = (ArrayList<AdjustmentOfPortable>) computeUnitForBacktest.listOfAdjustment.get(atomicIntBacktestIndex.get()-1);
//		BacktestResultTransactionDetails backtestResultDetails = BacktestUtils.getProfitLossDetails(mainBacktest.getListOfBacktestContainer());
//		double percent = MathTools.round(((double)backtestResultDetails.countForTradesProfit / (double)(backtestResultDetails.countForTradesProfit + backtestResultDetails.countForTradesLoss)) * 100);
//		listOfComputeResultForBacktestPartial.add(new ComputeResultForBacktestPartial(atomicIntBacktestIndex.get()-1, listOfIteration, AccountProvider.getInstance().getGlobalAccount().getBalance(), percent, AccountProvider.getInstance().getGlobalAccount().getTransactions(), BacktestUtils.getCurrentBacktestCompleteValueGroup(mainBacktest.getStrategy().signal, mainBacktest.getStrategy().strategyOptions, backtestResultDetails, BacktestType.backtest_clustered_client, AccountProvider.getInstance().getGlobalAccount())));
	}

	@Override
	public void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.compute_unit_backtest){
			computeUnitForBacktest = (ComputeUnitForBacktest) commandHolder.commandParameters;
			
			Co.println("--> Compute unit: " + computeUnitForBacktest.requestId + ", " + computeUnitForBacktest.dateStart + ", " + computeUnitForBacktest.dateEnd);
			Co.println("--> Compute unit symbols: " + computeUnitForBacktest.listOfSymbols.size());
			Co.println("--> Compute unit iteration: " + computeUnitForBacktest.listOfAlgorithmModel.size());
			
			runNextBacktest();
		}else if(commandHolder.command == Command.no_units_left){
			Co.println("--> No units left... exiting...");
			try {Thread.sleep(1000);}catch(InterruptedException e){return;}
			ApplicationStates.shutdown();
		}
	}
	
	@Override
	public void backtestCompleted(){
		listOfComputeResultForBacktest = mainBacktest.backtestEvaluator.getResults(new Symbol(computeUnitForBacktest.listOfSymbols.get(0), SecurityType.type_stock));
		
		Co.println("--> Have results: "+ listOfComputeResultForBacktest.size());
		
		sendBacktestResult();
		requestNextUnit();
		
	}
}
