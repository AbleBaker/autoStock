package com.autoStock.backtest.watchmaker;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.extras.StrategyOptionsOverride;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.tools.Benchmark;
import com.autoStock.trading.types.HistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestEvaluator implements FitnessEvaluator<AlgorithmModel>{
	private HistoricalData historicalData;
	
	public WMBacktestEvaluator(HistoricalData historicalData, StrategyOptionsOverride soo){
		this.historicalData = historicalData;
		historicalData.setStartAndEndDatesToExchange();
	}
	
	@Override
	public double getFitness(AlgorithmModel algorithmModel, List<? extends AlgorithmModel> notUsed) {
		BacktestEvaluation backtestEvaluation = getBacktestEvaluation(algorithmModel, false);
		return backtestEvaluation.getScore();
	}
	
	public BacktestEvaluation getBacktestEvaluation(AlgorithmModel algorithmModel, boolean includeExtras){
		Benchmark bench = new Benchmark();
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(algorithmModel);
		singleBacktest.selfPopulateBacktestData();
		
		bench.tick();
		 
		singleBacktest.runBacktest();
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer, includeExtras, includeExtras, false);
		
		//bench.printTick("Done score");
		
		return backtestEvaluation;
	}

	@Override
	public boolean isNatural() {
		return true;
	}
}
