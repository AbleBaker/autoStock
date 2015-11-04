package com.autoStock.algorithm.core;

import com.autoStock.backtest.BacktestDefinitions.BacktestType;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmDefinitions {
	public static enum AlgorithmMode {
		mode_backtest(true, true, false, true),
		mode_backtest_with_adjustment(false, false, false, false),
		mode_backtest_single(false, false, false, true),
		mode_engagement(false, true, false, true),
		mode_backtest_silent(false, false, false, true);
		;
		
		public boolean populateTable;
		public final boolean displayChart;
		public final boolean displayTable;
		public final boolean displayMessages;
		
		AlgorithmMode(boolean displayChart, boolean displayTable, boolean displayMessages, boolean populateTable){
			this.displayChart = displayChart;
			this.displayTable = displayTable;
			this.displayMessages = displayMessages;
			this.populateTable = populateTable;
		}

		public static AlgorithmMode getFromBacktestType(BacktestType backtestType) {
			if (backtestType == BacktestType.backtest_default || backtestType == BacktestType.backtest_bgi){
				return AlgorithmMode.mode_backtest;
			}else if (backtestType == BacktestType.backtest_adjustment_boilerplate || backtestType == BacktestType.backtest_adjustment_individual){
				return AlgorithmMode.mode_backtest_with_adjustment;	
			}else if (backtestType == BacktestType.backtest_clustered_client){
				return AlgorithmMode.mode_backtest_with_adjustment;
			}else if (backtestType == BacktestType.backtest_result_only){
				return AlgorithmMode.mode_backtest_silent;
			}
			
			throw new UnsupportedOperationException();
		}
	}
}
