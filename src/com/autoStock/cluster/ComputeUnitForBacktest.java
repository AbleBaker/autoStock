package com.autoStock.cluster;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.adjust.Iteration;
import com.autoStock.backtest.BacktestDefinitions.BacktestType;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeUnitForBacktest {
	private Exchange exchange;
	private ArrayList<String> listOfSymbols = new ArrayList<String>();
	private BacktestType backtestType;
	private Date dateStart;
	private Date dateEnd;
	
	private ArrayList<Iteration> listOfIteration = new ArrayList<Iteration>();
}
