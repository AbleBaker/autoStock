package com.autoStock.algorithm.core;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmDefinitions {
	public static enum AlgorithmMode {
		mode_backtest(false, true, false),
		mode_backtest_with_adjustment(false, false, false),
		mode_engagement(false, true, true),
		;
		
		public boolean displayChart;
		public boolean displayTable;
		public boolean displayMessages;
		
		AlgorithmMode(boolean displayChart, boolean displayTable, boolean displayMessages){
			this.displayChart = displayChart;
			this.displayTable = displayTable;
			this.displayMessages = displayMessages;
		}
	}
}
