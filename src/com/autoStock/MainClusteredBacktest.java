package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.cluster.ClusterNode;
import com.autoStock.cluster.ComputeResultForBacktest;
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
	private ArrayList<ClusterNode> listOfClusterNode = new ArrayList<ClusterNode>();
	private ArrayList<ComputeResultForBacktest> listOfComputeResultForBacktest = new ArrayList<ComputeResultForBacktest>();
	private AdjustmentCampaign adjustmentCampaign = new AdjustmentCampaign();
	private ArrayList<String> listOfSymbols;
	private Exchange exchange;
	private Date dateStart;
	private Date dateEnd;
	private Thread threadForRequestServer;
	private ClusterServer clusterServer;
	
	public MainClusteredBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.listOfSymbols = listOfSymbols;
		
		Global.callbackLock.requestLock();
		
		Co.println("--> Waiting for clients...");
		
		startRequestServer();
		//Generate compute units
		//Receive results here
		
	}
	
	private void startRequestServer(){
		threadForRequestServer = new Thread(new Runnable(){
			@Override
			public void run() {
				clusterServer = new ClusterServer(MainClusteredBacktest.this);
				clusterServer.startServer();
			}
		});
		
		threadForRequestServer.start();
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		Co.println("--> X");
		ComputeResultForBacktest computeResult = (ComputeResultForBacktest) commandHolder.commandParameters;
		Co.println("--> X: " + computeResult.yield);
	}
	
	//public void receiveComputeUnitResult
}
