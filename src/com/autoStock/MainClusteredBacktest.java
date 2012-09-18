package com.autoStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.Iteration;
import com.autoStock.cluster.ClusterNode;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterServer;
import com.autoStock.internal.Global;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktest implements ListenerOfCommandHolderResult {
	private static MainClusteredBacktest instance;
	private ArrayList<ClusterNode> listOfClusterNode = new ArrayList<ClusterNode>();
	private ArrayList<ComputeResultForBacktest> listOfComputeResultForBacktest = new ArrayList<ComputeResultForBacktest>();
	private ArrayList<String> listOfSymbols;
	private Exchange exchange;
	private ClusterServer clusterServer;
	private AdjustmentCampaign adjustmentCampaign = new AdjustmentCampaign();
	private AtomicInteger atomicIntForRequestId = new AtomicInteger();
	private Date dateStart;
	private Date dateEnd;
	
	public MainClusteredBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		instance = this;
		Global.callbackLock.requestLock();
		adjustmentCampaign.prepare();
		
		startRequestServer();
	}
	
	public ComputeUnitForBacktest getNextComputeUnit(){
		if (adjustmentCampaign.runAdjustment()){
			ArrayList<Iteration> listOfIteration = adjustmentCampaign.getListOfIterations();
			return new ComputeUnitForBacktest(listOfIteration, exchange, listOfSymbols, dateStart, dateEnd);
		}else{
			return null;
		}
	}
	
	public static MainClusteredBacktest getInstance(){
		return instance;
	}
	
	private void startRequestServer(){
		clusterServer = new ClusterServer(MainClusteredBacktest.this);
		clusterServer.startServer();
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		ComputeResultForBacktest computeResult = (ComputeResultForBacktest) commandHolder.commandParameters;
		Co.println("--> X: " + computeResult.yield);
	}
}