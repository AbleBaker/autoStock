package com.autoStock;

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
import com.autoStock.internal.Global;
import com.autoStock.tools.StringTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult, ListenerOfMainBacktestCompleted {
	private ClusterClient clusterClient;
	private AtomicInteger atomicIntForCount = new AtomicInteger();

	public MainClusteredBacktestClient() {
		Global.callbackLock.requestLock();
		
		clusterClient = new ClusterClient(this);
		clusterClient.startClient();
		
		requestNextUnit();
	}
	
	public void requestNextUnit(){
		CommandSerializer.sendSerializedCommand(Command.accept_unit, clusterClient.printWriter);
	}

	@Override
	public void receivedCommand(CommandHolder commandHolder) {
		ComputeUnitForBacktest computeUnitForBacktest = (ComputeUnitForBacktest) commandHolder.commandParameters;
		
		Co.println("--> Compute unit: " + computeUnitForBacktest.dateStart + ", " + computeUnitForBacktest.dateEnd);
		Co.println("--> Compute unit symbols: " + computeUnitForBacktest.listOfSymbols.size());
		Co.println("--> Compute unit iteration: ");
		for (Iteration iteration : computeUnitForBacktest.listOfIteration){
			Co.println("--> Iteration: " + iteration.start + ", " + iteration.end + ", " + iteration.adjustment.name() + ", " + iteration.signalTypeMetric.name() + ", " + iteration.getCurrentValue());
		}
		
		new MainBacktest(computeUnitForBacktest.exchange, computeUnitForBacktest.dateStart, computeUnitForBacktest.dateEnd, computeUnitForBacktest.listOfSymbols, BacktestType.backtest_clustered_client, this);
	}
	
	@Override
	public void backtestCompleted(){
		Co.println("--> X");
		CommandHolder<ComputeResultForBacktest> commandHolder = new CommandHolder<ComputeResultForBacktest>(Command.backtest_results, new ComputeResultForBacktest(atomicIntForCount.getAndIncrement()));
		CommandSerializer.sendSerializedCommand(commandHolder, clusterClient.printWriter);
		requestNextUnit();
	}
}
