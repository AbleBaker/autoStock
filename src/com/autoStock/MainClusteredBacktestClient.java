package com.autoStock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterClient;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.internal.Global;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult {
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
		ComputeUnitForBacktest computeUnit = (ComputeUnitForBacktest) commandHolder.commandParameters;
		
		backtestCompleted();
		requestNextUnit();
	}
	
	public void backtestCompleted(){
		CommandHolder<ComputeResultForBacktest> commandHolder = new CommandHolder<ComputeResultForBacktest>(Command.backtest_results, new ComputeResultForBacktest(atomicIntForCount.getAndIncrement()));
		CommandSerializer.sendSerializedCommand(commandHolder, clusterClient.printWriter);
		Co.println("--> X");
	}
}
