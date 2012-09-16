package com.autoStock;

import java.util.concurrent.atomic.AtomicBoolean;

import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.CommandSerializer;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterClient;
import com.autoStock.comServer.CommunicationDefinitions.Command;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktestClient implements ListenerOfCommandHolderResult {
	private ClusterClient clusterClient;
	private AtomicBoolean atomicBooleanCompletedBacktest = new AtomicBoolean(false);
	
	public MainClusteredBacktestClient() {
		clusterClient = new ClusterClient(this);
		clusterClient.startClient();
		
		requestNextUnit();
	}
	
	public void requestNextUnit(){
		CommandSerializer.sendSerializedCommand(Command.accept_unit, clusterClient.printWriter);
		clusterClient.listenForResponse();
	}

	@Override
	public void receivedCommand(CommandHolder commandHolder) {
		Co.println("--> X: " + commandHolder.command.name());
		ComputeUnitForBacktest computeUnit = (ComputeUnitForBacktest) commandHolder.commandParameters;
		
		backtestCompleted();
		requestNextUnit();
	}
	
	public void backtestCompleted(){
		CommandHolder commandHolder = new CommandHolder(Command.backtest_results, new ComputeResultForBacktest());
		CommandSerializer.sendSerializedCommand(commandHolder, clusterClient.printWriter);
	}
}
