package com.autoStock.backtest.watchmaker;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestEvaluator implements FitnessEvaluator<AlgorithmModel>{
	private HistoricalData historicalData;
	private Exchange exchange;
	private Symbol symbol;
	
	public WMBacktestEvaluator(HistoricalData historicalData){
		exchange = new Exchange("NYSE");
		symbol = new Symbol("AIG", SecurityType.type_stock); 
		this.historicalData = historicalData;
		historicalData.setStartAndEndDatesToExchange();
	}
	
	@Override
	public synchronized double getFitness(AlgorithmModel algorithmModel, List<? extends AlgorithmModel> arg1) {
		PositionGovernor.getInstance().reset();
		PositionManager.getInstance().reset();
		
		BacktestEvaluation backtestEvaluation = getBacktestEvaluation(algorithmModel);
		
		BacktestEvaluation backtestEvaluationOutOfSample = new BacktestEvaluationBuilder().buildOutOfSampleEvaluation(backtestEvaluation); 


//		Co.print("Check A: " + singleBacktest.backtestContainer.algorithm.signalGroup.signalOfUO.signalParameters.periodLength.value);
//		Co.print("Check B: " + singleBacktest.backtestContainer.algorithm.getPeriodLength());		
//		Co.println("--> Yield: " + backtestEvaluation.percentYield);
		
		if (backtestEvaluationOutOfSample.getScore() == 0){
			return 0;
		}
		
		return backtestEvaluation.getScore() + backtestEvaluationOutOfSample.getScore();
	}
	
	public BacktestEvaluation getBacktestEvaluation(AlgorithmModel algorithmModel){
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(algorithmModel);
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		
		return new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
	}

	@Override
	public boolean isNatural() {
		return true;
	}
}
