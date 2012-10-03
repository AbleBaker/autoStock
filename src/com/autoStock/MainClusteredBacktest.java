package com.autoStock;

import java.util.ArrayList;
import java.util.Collections;
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
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.internal.Global;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.ReflectiveComparator;
import com.autoStock.tools.ReflectiveComparator.ListComparator;
import com.autoStock.tools.ReflectiveComparator.ListComparator.SortDirection;
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
	private final int computeUnitIterationSize = 32;
	private Benchmark bench = new Benchmark();
	
	public MainClusteredBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		instance = this;
		Global.callbackLock.requestLock();
		adjustmentCampaign.prepare();
		
		startRequestServer();
	}
	
	
	public static MainClusteredBacktest getInstance(){
		return instance;
	}
	
	private void startRequestServer(){
		clusterServer = new ClusterServer(MainClusteredBacktest.this);
		clusterServer.startServer();
	}
	
	public synchronized ComputeUnitForBacktest getNextComputeUnit(){
		ArrayList<ArrayList<Iteration>> listOfIteration = new ArrayList<ArrayList<Iteration>>();
		
		Co.println("--> Generating unit...");
		
		for (int i=0; i<computeUnitIterationSize; i++){
			if (adjustmentCampaign.runAdjustment()){
				listOfIteration.add(adjustmentCampaign.getListOfClonedIterations());
			}else{
				break;
			}
		}
		
		Co.println("--> Percent complete: %" + MathTools.round(adjustmentCampaign.getPercentComplete()));
		bench.tick();
		
		if (listOfIteration.size() > 0){
			return new ComputeUnitForBacktest(atomicIntForRequestId.getAndIncrement(), listOfIteration, exchange, listOfSymbols, dateStart, dateEnd);
		}else{
			Co.println("--> No units left...");
			return null;
		}
	}
	
	public void displayResultTable(){
		Collections.sort(listOfComputeResultForBacktest, new ReflectiveComparator.ListComparator("accountBalance", SortDirection.order_descending));
		
		for (ComputeResultForBacktest computeUnit : listOfComputeResultForBacktest.subList(0, Math.min(listOfComputeResultForBacktest.size()-1, 10))){
			Co.println(computeUnit.resultDetails);
		}
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.backtest_results){
			ComputeResultForBacktest computeResult = (ComputeResultForBacktest) commandHolder.commandParameters;
			listOfComputeResultForBacktest.add(computeResult);
//			Co.println("--> X: " + computeResult.requestId + ", " + computeResult.unitId +", " + computeResult.accountBalance + ", " + computeResult.transactions);
			if ((computeResult.requestId * computeUnitIterationSize) + computeResult.unitId+1 == adjustmentCampaign.getPermutationCount()){
				Co.println("--> All done!");
				displayResultTable();
			}
		}
	}
}