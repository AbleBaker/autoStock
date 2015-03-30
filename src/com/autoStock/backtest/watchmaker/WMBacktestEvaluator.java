package com.autoStock.backtest.watchmaker;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.trading.types.HistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestEvaluator implements FitnessEvaluator<AlgorithmModel>{
	private HistoricalData historicalData;
	
	public WMBacktestEvaluator(HistoricalData historicalData){
		this.historicalData = historicalData;
		historicalData.setStartAndEndDatesToExchange();
	}
	
	@Override
	public double getFitness(AlgorithmModel algorithmModel, List<? extends AlgorithmModel> notUsed) {
		BacktestEvaluation backtestEvaluation = getBacktestEvaluation(algorithmModel, false);
		return backtestEvaluation.getScore();
	}
	
	public BacktestEvaluation getBacktestEvaluation(AlgorithmModel algorithmModel, boolean includeExtras){
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(algorithmModel);
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer, includeExtras, includeExtras, false);
		
		return backtestEvaluation;
	}

	@Override
	public boolean isNatural() {
		return true;
	}
}
