package com.autoStock.backtest;

import java.util.Date;

import com.autoStock.account.AccountProvider;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class SingleBacktest implements ListenerOfBacktestCompleted {
	private BacktestContainer backtestContainer;
	
	public SingleBacktest(HistoricalData historicalData){
		backtestContainer = new BacktestContainer(historicalData.symbol, historicalData.exchange, this, AlgorithmMode.mode_backtest_silent);
	}

	@Override
	public void backtestCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		
	}
}
