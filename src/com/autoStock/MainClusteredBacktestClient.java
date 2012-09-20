package com.autoStock;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.Iteration;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterClient;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.finance.Account;
import com.autoStock.internal.Global;
import com.autoStock.tools.StringTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult, ListenerOfMainBacktestCompleted {
	private ClusterClient clusterClient;
	private AtomicInteger atomicIntBacktestIndex = new AtomicInteger(0);
	private ComputeUnitForBacktest computeUnitForBacktest;

	public MainClusteredBacktestClient() {
		Global.callbackLock.requestLock();
		
		clusterClient = new ClusterClient(this);
		clusterClient.startClient();
		
		requestNextUnit();
	}
	
	public void requestNextUnit(){
		CommandSerializer.sendSerializedCommand(Command.accept_unit, clusterClient.printWriter);
	}
	
	public void runNextBacktest(){
		int backtestIndex = atomicIntBacktestIndex.getAndIncrement();
		
		if (backtestIndex == computeUnitForBacktest.listOfIteration.size()){
			allBacktestsCompleted();
		}else{
			ArrayList<Iteration> listOfIteration = computeUnitForBacktest.listOfIteration.get(backtestIndex);
			applyIterations(listOfIteration);
			new MainBacktest(computeUnitForBacktest.exchange, computeUnitForBacktest.dateStart, computeUnitForBacktest.dateEnd, computeUnitForBacktest.listOfSymbols, BacktestType.backtest_clustered_client, this);
		}
	}
	
	public void applyIterations(ArrayList<Iteration> listOfIteration){
		AdjustmentCampaign.getInstance().setAdjustmentValuesFromIterationList(listOfIteration);
	}
	
	public void allBacktestsCompleted(){
		Account.instance.resetAccount();
		atomicIntBacktestIndex.set(0);
		requestNextUnit();
	}
	
	public void sendBacktestResult(){
		ArrayList<Iteration> listOfIteration = computeUnitForBacktest.listOfIteration.get(atomicIntBacktestIndex.get()-1);
		CommandHolder<ComputeResultForBacktest> commandHolder = new CommandHolder<ComputeResultForBacktest>(Command.backtest_results, new ComputeResultForBacktest(listOfIteration, Account.instance.getAccountBalance(), Account.instance.getTransactions()));
		CommandSerializer.sendSerializedCommand(commandHolder, clusterClient.printWriter);
	}

	@Override
	public void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.compute_unit_backtest){
			computeUnitForBacktest = (ComputeUnitForBacktest) commandHolder.commandParameters;
			
			Co.println("--> Compute unit: " + computeUnitForBacktest.dateStart + ", " + computeUnitForBacktest.dateEnd);
			Co.println("--> Compute unit symbols: " + computeUnitForBacktest.listOfSymbols.size());
			Co.println("--> Compute unit iteration: " + computeUnitForBacktest.listOfIteration.size());
			
			if (atomicIntBacktestIndex.get() != 0){
				throw new IllegalStateException();
			}
			
			runNextBacktest();
		}
	}
	
	@Override
	public void backtestCompleted(){
		Co.println("--> Backtesting completed: " + atomicIntBacktestIndex.get());
		sendBacktestResult();
		runNextBacktest();
	}
}
