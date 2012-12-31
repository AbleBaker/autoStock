package com.autoStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
	private ArrayList<ComputeResultForBacktestPartial> listOfComputeResultForBacktestPartialForProfit = new ArrayList<ComputeResultForBacktestPartial>();
	private ArrayList<ComputeResultForBacktestPartial> listOfComputeResultForBacktestPartialForTransactions = new ArrayList<ComputeResultForBacktestPartial>();
	private ArrayList<Long> listOfComputeUnitResultIds = new ArrayList<Long>();
	private ArrayList<String> listOfSymbols;
	private Exchange exchange;
	private ClusterServer clusterServer;
	private AdjustmentCampaign adjustmentCampaign = AdjustmentCampaign.getInstance();
	private AtomicLong atomicIntForRequestId = new AtomicLong();
	private Date dateStart;
	private Date dateEnd;
	private final int computeUnitIterationSize = 128;
	private final int computeUnitResultPruneSize = 16;
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
			return new ComputeUnitForBacktest(atomicIntForRequestId.getAndIncrement(), listOfIteration, exchange, listOfSymbols, dateStart, dateEnd);
		}else{
			Co.println("--> No units left...");
			return null;
		}
	}
	
	public void displayResultTable(){
		Co.println("--> ********************* Best profit yield *********************");
		Collections.sort(listOfComputeResultForBacktestPartialForProfit, new ReflectiveComparator.ListComparator("accountBalance", SortDirection.order_ascending));
		for (ComputeResultForBacktestPartial computeUnitResult : listOfComputeResultForBacktestPartialForProfit){
			Co.println(computeUnitResult.resultDetails);
		}
		
		Co.println("--> ********************* Best transaction yield *********************");
		Collections.sort(listOfComputeResultForBacktestPartialForTransactions, new ReflectiveComparator.ListComparator("percentOfTransProfitLoss", SortDirection.order_ascending));
		for (ComputeResultForBacktestPartial computeUnitResult : listOfComputeResultForBacktestPartialForTransactions){
			Co.println(computeUnitResult.resultDetails);
		}
	}
	
	public void pruneToTop(int count){
		Collections.sort(listOfComputeResultForBacktestPartialForProfit, new ReflectiveComparator.ListComparator("accountBalance", SortDirection.order_descending));		
		listOfComputeResultForBacktestPartialForProfit = new ArrayList<ComputeResultForBacktestPartial>(listOfComputeResultForBacktestPartialForProfit.subList(0, Math.min(listOfComputeResultForBacktestPartialForProfit.size()-1, count)));
		
		Collections.sort(listOfComputeResultForBacktestPartialForTransactions, new ReflectiveComparator.ListComparator("percentOfTransProfitLoss", SortDirection.order_descending));		
		listOfComputeResultForBacktestPartialForTransactions = new ArrayList<ComputeResultForBacktestPartial>(listOfComputeResultForBacktestPartialForTransactions.subList(0, Math.min(listOfComputeResultForBacktestPartialForTransactions.size()-1, count)));
	}

	@Override
	public synchronized void receivedCommand(CommandHolder commandHolder) {
		if (commandHolder.command == Command.backtest_results){			
			ComputeResultForBacktest computeResult = (ComputeResultForBacktest) commandHolder.commandParameters;
			
			listOfComputeUnitResultIds.add(computeResult.requestId);
			
			listOfComputeResultForBacktestPartialForProfit.addAll(computeResult.listOfComputeResultForBacktestPartial);
			listOfComputeResultForBacktestPartialForTransactions.addAll(computeResult.listOfComputeResultForBacktestPartial);
			pruneToTop(computeUnitResultPruneSize);
			
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
		if (adjustmentCampaign.hasMore() == false && atomicIntForRequestId.get() == listOfComputeUnitResultIds.size()){
			return true;
		}else{
			return false;
		}
	}
}