package com.autoStock;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.cluster.ClusterNode;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.comServer.ClusterServer;
import com.autoStock.internal.Global;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktest {
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
		
		startRequestServer();
	}
	
	private void startRequestServer(){
		threadForRequestServer = new Thread(new Runnable(){
			@Override
			public void run() {
				clusterServer = new ClusterServer();
				clusterServer.startServer();
			}
		});
	}
}
