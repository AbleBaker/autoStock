package com.autoStock.cluster;

import com.autoStock.backtest.BacktestEvaluation;

/**
 * @author Kevin Kowalewski
 *
 */
public class ComputeResultForBacktestPartial {
	public int unitId;
	public BacktestEvaluation backtestEvaluation;
	
	public ComputeResultForBacktestPartial(int unitId, BacktestEvaluation backtestEvaluation){
		this.unitId = unitId;
		this.backtestEvaluation = backtestEvaluation;
	}
}
