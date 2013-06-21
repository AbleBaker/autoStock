package com.autoStock;

import java.util.ArrayList;

import com.autoStock.backtest.ListenerOfMainBacktestCompleted;
import com.autoStock.types.Exchange;


/**
 * @author Kevin Kowalewski
 *
 */
public class MainTest implements ListenerOfMainBacktestCompleted {
	private Exchange exchange;
	private ArrayList<String> listOfSymbols = new ArrayList<String>();
	public MainTest(){
//		Co.println("--> X");
//		
//		exchange = new Exchange("NYSE");
//		listOfSymbols.add("AIG");
//		
//		MainBacktest mainBacktest = new MainBacktest(exchange, DateTools.getDateFromString("01/19/2012"), DateTools.getDateFromString("01/19/2012"), listOfSymbols, BacktestType.backtest_result_only, this);
	}
	
	@Override
	public void backtestCompleted() {		
//		Co.println("--> " + Account.getInstance().getAccountBalance());
	}
}
