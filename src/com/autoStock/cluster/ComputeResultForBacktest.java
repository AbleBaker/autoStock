package com.autoStock.cluster;

import java.util.ArrayList;

import com.autoStock.backtest.BacktestEvaluation;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktest {
	public long requestId;
	public ArrayList<BacktestEvaluation> listOfBacktestEvaluation = new ArrayList<BacktestEvaluation>();

	public ComputeResultForBacktest(long requestId, ArrayList<BacktestEvaluation> listOfBacktestEvaluation){
		this.listOfBacktestEvaluation = listOfBacktestEvaluation;
	}
}
