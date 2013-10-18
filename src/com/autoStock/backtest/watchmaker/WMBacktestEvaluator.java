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
	
	public WMBacktestEvaluator(){
		exchange = new Exchange("NYSE");
		symbol = new Symbol("AIG", SecurityType.type_stock); 
		
		historicalData = new HistoricalData(exchange, symbol, DateTools.getDateFromString("01/09/2012"), DateTools.getDateFromString("01/09/2012"), Resolution.min);
		
		historicalData.startDate.setHours(historicalData.exchange.timeOpenForeign.hours);
		historicalData.startDate.setMinutes(historicalData.exchange.timeOpenForeign.minutes);
		historicalData.endDate.setHours(historicalData.exchange.timeCloseForeign.hours);
		historicalData.endDate.setMinutes(historicalData.exchange.timeCloseForeign.minutes);
	}
	
	@Override
	public synchronized double getFitness(AlgorithmModel algorithmModel, List<? extends AlgorithmModel> arg1) {
		PositionGovernor.getInstance().reset();
		PositionManager.getInstance().reset();
		
//		Co.println("\n--> Evaluating:");
//		Co.print(" " + algorithmModel.listOfSignalParameters.get(10).arrayOfSignalGuageForLongEntry[0].threshold);
//		Co.print(" " + algorithmModel.listOfSignalParameters.get(10).arrayOfSignalGuageForLongExit[0].threshold);
//		Co.print(" " + algorithmModel.listOfSignalParameters.get(10).arrayOfSignalGuageForShortEntry[0].threshold);
//		Co.print(" " + algorithmModel.listOfSignalParameters.get(10).arrayOfSignalGuageForShortExit[0].threshold);
//		Co.print(" " + algorithmModel.listOfSignalParameters.get(10).periodLength.value);
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(algorithmModel);
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		
		Co.println("Check A: " + singleBacktest.backtestContainer.algorithm.signalGroup.signalOfUO.signalParameters.arrayOfSignalGuageForLongEntry[0].mutableEnumForSignalGuageType.enumValue.name());
//		Co.print("Check A: " + singleBacktest.backtestContainer.algorithm.signalGroup.signalOfUO.signalParameters.periodLength.value);
//		Co.print("Check B: " + singleBacktest.backtestContainer.algorithm.getPeriodLength());
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
		
//		Co.println("--> Yield: " + backtestEvaluation.percentYield);
		
		return backtestEvaluation.percentYield > 0 ? backtestEvaluation.percentYield : 0;
	}


	@Override
	public boolean isNatural() {
		return true;
	}
}
