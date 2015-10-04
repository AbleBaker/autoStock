package com.autoStock.backtest.encog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.DummyAlgorithm;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.extras.StrategyOptionsOverride;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.backtest.encog.TrainEncogSignal.EncogNetworkType;
import com.autoStock.backtest.watchmaker.WMEvolutionParams.WMEvolutionThorough;
import com.autoStock.backtest.watchmaker.WMEvolutionParams.WMEvolutionType;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogBacktestContainer {
	private static boolean USE_SO_OVERRIDE = true; 
	public DummyAlgorithm algorithm;
	public Symbol symbol;
	public Exchange exchange;
	public Date dateStart;
	public Date dateEnd;
	private double bestResult = 0;
	private HistoricalData historicalData;
	
//	private TrainEncogSignalNew trainEncogSignal;
	private TrainEncogSignal trainEncogSignal;

	public EncogBacktestContainer(Symbol symbol, Exchange exchange, Date dateStart, Date dateEnd) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		this.historicalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min);
		this.historicalData.setStartAndEndDatesToExchange();
	
		algorithm = new DummyAlgorithm(exchange, symbol, AlgorithmMode.mode_backtest_with_adjustment, new BasicAccount(AccountProvider.defaultBalance));
			
//		trainEncogSignal = new TrainEncogSignalNew(AlgorithmModel.getEmptyModel(), historicalData);
		trainEncogSignal = new TrainEncogSignal(AlgorithmModel.getEmptyModel(), historicalData, false);
	}
	
	public void runBacktest(){
		AlgorithmModel algorithmModel = null;
		
		Co.println("--> Blanking the network... ");
		if (SignalOfEncog.encogNetworkType == EncogNetworkType.basic){
			trainEncogSignal.getTrainer().saveNetwork();
		}else if (SignalOfEncog.encogNetworkType == EncogNetworkType.neat){
			trainEncogSignal.setDetails(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol));
			for (int i=0; i<TrainEncogSignal.TRAINING_ITERATIONS; i++){
				trainEncogSignal.getTrainer().train(1, 0);
				if (trainEncogSignal.getTrainer().bestScore != 0){trainEncogSignal.getTrainer().saveNetwork(); break;}
			}
		}
		
		Co.println("OK!");
		
		StrategyOptionsOverride strategyOptionsOverride = new StrategyOptionsOverride() {
			@Override
			public void override(StrategyOptions strategyOptions) {
				//For loose Encog training
				strategyOptions.disableAfterYield.value = 1000d;
				strategyOptions.maxStopLossPercent.value = -1000d;
				strategyOptions.maxProfitDrawdownPercent.value = -1000d;
				strategyOptions.maxPositionTimeAtLoss.value = -1000;
				strategyOptions.maxPositionTimeAtProfit.value = 1000;
			}
		};
		
		trainEncogSignal.execute(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, USE_SO_OVERRIDE ? strategyOptionsOverride : null), bestResult);
		trainEncogSignal.getTrainer().saveNetwork();
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, USE_SO_OVERRIDE ? strategyOptionsOverride : null));
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		Co.print(new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer).toString());
		
//		Date dateOutOfSample = DateTools.getFirstWeekdayAfter(dateEnd);
//		SingleBacktest singleBacktest = new SingleBacktest(new HistoricalData(exchange, symbol, dateOutOfSample, dateOutOfSample, Resolution.min), AlgorithmMode.mode_backtest_single);
//		singleBacktest.selfPopulateBacktestData();
//		singleBacktest.runBacktest();
//		
//		BacktestEvaluation backtestEvaluationOutOfSample = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
//		Co.println("\n\n Out of sample: " + dateOutOfSample + ", " + backtestEvaluationOutOfSample.percentYield);
	}
}
