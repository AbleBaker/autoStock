package com.autoStock;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentOfPortable;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeResultForBacktestPartial;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterClient;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.finance.Account;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionManager;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult, ListenerOfMainBacktestCompleted {
	private ClusterClient clusterClient;
	private AtomicInteger atomicIntBacktestIndex = new AtomicInteger(0);
	private ComputeUnitForBacktest computeUnitForBacktest;
	private MainBacktest mainBacktest;
	private ArrayList<ComputeResultForBacktestPartial> listOfComputeResultForBacktestPartial = new ArrayList<ComputeResultForBacktestPartial>();

	public MainClusteredBacktestClient() {
		Global.callbackLock.requestLock();
		PositionManager.getInstance().orderMode = OrderMode.mode_simulated;
		
		clusterClient = new ClusterClient(this);
		clusterClient.startClient();
		
		requestNextUnit();
	}
	
	public void requestNextUnit(){
		CommandSerializer.sendSerializedCommand(Command.accept_unit, clusterClient.printWriter);
	}
	
	public void runNextBacktest(){
		int backtestIndex = atomicIntBacktestIndex.getAndIncrement();
		
		Account.getInstance().resetAccount();
		
		if (backtestIndex == computeUnitForBacktest.listOfAdjustment.size()){
			allBacktestsCompleted();
		}else{
			ArrayList<AdjustmentOfPortable> listOfAdjustmentOfPortable = (ArrayList<AdjustmentOfPortable>) computeUnitForBacktest.listOfAdjustment.get(backtestIndex);
			applyIterations(listOfAdjustmentOfPortable);
			mainBacktest = new MainBacktest(computeUnitForBacktest.exchange, computeUnitForBacktest.dateStart, computeUnitForBacktest.dateEnd, computeUnitForBacktest.listOfSymbols, BacktestType.backtest_clustered_client, this);
		}
	}
	
	public void applyIterations(ArrayList<AdjustmentOfPortable> listOfAdjustmentPortable){
		Co.println("--> Applying...");
		AdjustmentCampaign.getInstance().setAdjustmentValuesFromIterationList(listOfAdjustmentPortable);
	}
	
	public void allBacktestsCompleted(){
		Co.println("--> All backtests completed...");
		sendBacktestResult();
		atomicIntBacktestIndex.set(0);
		requestNextUnit();
	}
	
	public void sendBacktestResult(){
		CommandHolder<ComputeResultForBacktest> commandHolder = new CommandHolder<ComputeResultForBacktest>(Command.backtest_results, new ComputeResultForBacktest(listOfComputeResultForBacktestPartial));
		CommandSerializer.sendSerializedCommand(commandHolder, clusterClient.printWriter);
		listOfComputeResultForBacktestPartial.clear();
	}
	
	public void storeBacktestResult(){
		ArrayList<AdjustmentOfPortable> listOfIteration = (ArrayList<AdjustmentOfPortable>) computeUnitForBacktest.listOfAdjustment.get(atomicIntBacktestIndex.get()-1);
		listOfComputeResultForBacktestPartial.add(new ComputeResultForBacktestPartial(computeUnitForBacktest.requestId, atomicIntBacktestIndex.get()-1, listOfIteration, Account.getInstance().getAccountBalance(), Account.getInstance().getTransactions(), BacktestUtils.getCurrentBacktestCompleteValueGroup(mainBacktest.getStrategy().signal, mainBacktest.getStrategy().strategyOptions, 0, 0, 0)));
	}

	@Override
	public void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.compute_unit_backtest){
			computeUnitForBacktest = (ComputeUnitForBacktest) commandHolder.commandParameters;
			
			Co.println("--> Compute unit: " + computeUnitForBacktest.requestId + ", " + computeUnitForBacktest.dateStart + ", " + computeUnitForBacktest.dateEnd);
			Co.println("--> Compute unit symbols: " + computeUnitForBacktest.listOfSymbols.size());
			Co.println("--> Compute unit iteration: " + computeUnitForBacktest.listOfAdjustment.size());
			
			if (atomicIntBacktestIndex.get() != 0){
				throw new IllegalStateException();
			}
			
			runNextBacktest();
		}else if(commandHolder.command == Command.no_units_left){
			Co.println("--> No units left... exiting...");
			try {Thread.sleep(1000);}catch(InterruptedException e){return;}
			ApplicationStates.shutdown();
			requestNextUnit();
		}
	}
	
	@Override
	public void backtestCompleted(){
		Co.println("--> Backtesting completed: " + atomicIntBacktestIndex.get());
		storeBacktestResult();
		runNextBacktest();
	}
}
