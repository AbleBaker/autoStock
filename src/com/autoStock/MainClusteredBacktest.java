package com.autoStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.AdjustmentOfPortable;
import com.autoStock.cluster.ClusterNode;
import com.autoStock.cluster.ComputeResultForBacktest;
import com.autoStock.cluster.ComputeResultForBacktestPartial;
import com.autoStock.cluster.ComputeUnitForBacktest;
import com.autoStock.com.CommandHolder;
import com.autoStock.com.ListenerOfCommandHolderResult;
import com.autoStock.comServer.ClusterServer;
import com.autoStock.comServer.CommunicationDefinitions.Command;
import com.autoStock.internal.Global;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionManager;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.ReflectiveComparator;
import com.autoStock.tools.ReflectiveComparator.ListComparator.SortDirection;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainClusteredBacktest implements ListenerOfCommandHolderResult {
	private static MainClusteredBacktest instance;
	private ArrayList<ClusterNode> listOfClusterNode = new ArrayList<ClusterNode>();
	private ArrayList<ComputeUnitForBacktest> listOfComputeUnitForBacktest = new ArrayList<ComputeUnitForBacktest>();
	private ArrayList<ComputeResultForBacktest> listOfComputeResultForBacktest = new ArrayList<ComputeResultForBacktest>();
	private ArrayList<String> listOfSymbols;
	private Exchange exchange;
	private ClusterServer clusterServer;
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AtomicInteger atomicIntForRequestId = new AtomicInteger();
	private Date dateStart;
	private Date dateEnd;
	private final int computeUnitIterationSize = 64;
	private Benchmark bench = new Benchmark();
	private Benchmark benchTotal = new Benchmark();
	
	public MainClusteredBacktest(Exchange exchange, Date dateStart, Date dateEnd, ArrayList<String> listOfSymbols) {
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		instance = this;
		Global.callbackLock.requestLock();
		PositionManager.getInstance().orderMode = OrderMode.mode_simulated;
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
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
		ArrayList<ArrayList<AdjustmentOfPortable>> listOfIteration = new ArrayList<ArrayList<AdjustmentOfPortable>>();
		
		Co.println("--> Generating unit...");
		
		if (benchTotal.hasTicked == false){
			benchTotal.tick();
		}
		
		for (int i=0; i<computeUnitIterationSize; i++){
			if (adjustmentCampaign.runAdjustment()){
				listOfIteration.add(adjustmentCampaign.getListOfPortableAdjustment());
			}else{
				break;
			}
		}
		
		Co.println("--> Percent complete: %" + MathTools.round(adjustmentCampaign.getPercentComplete()));
		bench.tick();
		
		if (listOfIteration.size() > 0){
			ComputeUnitForBacktest computeUnitForBacktest = new ComputeUnitForBacktest(atomicIntForRequestId.getAndIncrement(), listOfIteration, exchange, listOfSymbols, dateStart, dateEnd);
			listOfComputeUnitForBacktest.add(computeUnitForBacktest);
			return computeUnitForBacktest;
		}else{
			Co.println("--> No units left...");
			return null;
		}
	}
	
	public void displayResultTable(){
		ArrayList<ComputeResultForBacktestPartial> list = new ArrayList<ComputeResultForBacktestPartial>();
		
		for (ComputeResultForBacktest computeResultForBacktest : listOfComputeResultForBacktest){
			list.addAll(computeResultForBacktest.listOfComputeResultForBacktestPartial);
		}
		
		Collections.sort(list, new ReflectiveComparator.ListComparator("accountBalance", SortDirection.order_descending));
		
		Co.println("--> Size? " + list.size() + ", " + listOfComputeResultForBacktest.size());
		
		for (ComputeResultForBacktestPartial computeUnit : list.subList(0, Math.min(list.size()-1, 10))){
			Co.println(computeUnit.resultDetails);
		}
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.backtest_results){			
			ComputeResultForBacktest computeResult = (ComputeResultForBacktest) commandHolder.commandParameters;
			listOfComputeResultForBacktest.add(computeResult);
			
//			Co.println("--> Received result... " + computeResult.requestId + ", " + computeResult.unitId);
			
//			Co.println("--> Progress: " + listOfComputeResultForBacktest.size() + ", " + adjustmentCampaign.getPermutationCount());

			if (isComplete()){
				Co.println("--> All done!");
				displayResultTable();
				benchTotal.printTick("Finished clustered backtest");
				Global.callbackLock.releaseLock();
			}
		}
	}
	
	public boolean isComplete(){
		if (adjustmentCampaign.hasMore() == false && listOfComputeUnitForBacktest.size() == listOfComputeResultForBacktest.size()){
			return true;
		}else{
			return false;
		}
	}
}