package com.autoStock.algorithm;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmDefinitions {
	public static enum AlgorithmMode {
		mode_backtest(true, true, false),
		mode_backtest_with_adjustment(false, false, false),
		mode_engagement(false, true, true),
		;
		
		boolean displayChart;
		boolean displayTable;
		boolean displayMessages;
		
		AlgorithmMode(boolean displayChart, boolean displayTable, boolean displayMessages){
			this.displayChart = displayChart;
			this.displayTable = displayTable;
			this.displayMessages = displayMessages;
		}
	}
}
