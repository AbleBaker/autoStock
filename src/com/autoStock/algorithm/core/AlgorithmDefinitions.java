package com.autoStock.algorithm.core;

import com.autoStock.backtest.BacktestDefinitions.BacktestType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmDefinitions {
	public static enum AlgorithmMode {
		mode_backtest(true, true, false),
		mode_backtest_with_adjustment(false, false, false),
		mode_engagement(false, true, false),
		;
		
		public final boolean displayChart;
		public final boolean displayTable;
		public final boolean displayMessages;
		
		AlgorithmMode(boolean displayChart, boolean displayTable, boolean displayMessages){
			this.displayChart = displayChart;
			this.displayTable = displayTable;
			this.displayMessages = displayMessages;
		}

		public static AlgorithmMode getFromBacktestType(BacktestType backtestType) {
			if (backtestType == BacktestType.backtest_default){
				return AlgorithmMode.mode_backtest;
			}else if (backtestType == BacktestType.backtest_adjustment){
				return AlgorithmMode.mode_backtest_with_adjustment;	
			}else if (backtestType == BacktestType.backtest_clustered_client){
				return AlgorithmMode.mode_backtest_with_adjustment;
			}
			
			throw new UnsupportedOperationException();
		}
	}
}
