package com.autoStock.cluster;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.adjust.AdjustmentCampaign;
import com.autoStock.adjust.Iteration;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeUnitForBacktest {
	public Exchange exchange;
	public ArrayList<String> listOfSymbols = new ArrayList<String>();
	public BacktestType backtestType = BacktestType.backtest_clustered_client;
	public Date dateStart;
	public Date dateEnd;
	public ArrayList<ArrayList<Iteration>> listOfIteration = new ArrayList<ArrayList<Iteration>>();
	public int requestId;
	
	public ComputeUnitForBacktest(int requestId, ArrayList<ArrayList<Iteration>> listOfIteration, Exchange exchange, ArrayList<String> listOfSymbols, Date dateStart, Date dateEnd) {
		this.requestId = requestId;
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.listOfIteration = listOfIteration;
	}
}
