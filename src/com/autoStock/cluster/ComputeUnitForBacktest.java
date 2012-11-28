package com.autoStock.cluster;

import java.util.ArrayList;
import java.util.Date;

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
	public ArrayList<ArrayList<?>> listOfAdjustment = new ArrayList<ArrayList<?>>();
	public int requestId;
	
	public ComputeUnitForBacktest(int requestId, ArrayList<ArrayList<?>> listOfAdjustment, Exchange exchange, ArrayList<String> listOfSymbols, Date dateStart, Date dateEnd) {
		this.requestId = requestId;
		this.exchange = exchange;
		this.listOfSymbols = listOfSymbols;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.listOfAdjustment = listOfAdjustment;
	}
}
