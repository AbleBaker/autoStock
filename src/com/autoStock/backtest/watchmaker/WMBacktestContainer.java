package com.autoStock.backtest.watchmaker;

import java.awt.IllegalComponentStateException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.DummyAlgorithm;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.ListenerOfBacktestCompleted;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.google.gson.internal.Pair;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestContainer implements EvolutionObserver<AlgorithmModel>, IslandEvolutionObserver<AlgorithmModel> {
	public DummyAlgorithm algorithm;
	public Symbol symbol;
	public Exchange exchange;
	public Date dateStart;
	public Date dateEnd;
	private double bestResult = 0;
	private HistoricalData historicalData;

	private WMCandidateFactory wmCandidateFactory;
	private MersenneTwisterRNG randomNumberGenerator = new MersenneTwisterRNG();
	
	private GenerationalEvolutionEngine<AlgorithmModel> evolutionEngine;
	private IslandEvolution<AlgorithmModel> islandEvolutionEngine;

	public WMBacktestContainer(Symbol symbol, Exchange exchange, Date dateStart, Date dateEnd) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		this.historicalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min);
	
		algorithm = new DummyAlgorithm(exchange, symbol, AlgorithmMode.mode_backtest_with_adjustment, new BasicAccount(AccountProvider.defaultBalance));
		
		wmCandidateFactory = new WMCandidateFactory(this);
		List<EvolutionaryOperator<AlgorithmModel>> operators = new ArrayList<EvolutionaryOperator<AlgorithmModel>>();
		operators.add(new WMMutation(new Probability(0.25)));
		operators.add(new WMCrossover(1));
		
		EvolutionaryOperator<AlgorithmModel> evolutionaryPipeline = new EvolutionPipeline<AlgorithmModel>(operators);
		
		islandEvolutionEngine = new IslandEvolution<>(3, 
				new RingMigration(), 
				wmCandidateFactory, 
				evolutionaryPipeline, 
				new WMBacktestEvaluator(historicalData), 
				new RouletteWheelSelection(), 
				randomNumberGenerator);
		
		islandEvolutionEngine.addEvolutionObserver(this);
		
		evolutionEngine = new GenerationalEvolutionEngine<AlgorithmModel>(wmCandidateFactory,
			evolutionaryPipeline, 
			new WMBacktestEvaluator(historicalData), 
			new RouletteWheelSelection(), 
			randomNumberGenerator);
		
		evolutionEngine.addEvolutionObserver(this);
	}
	
	public void runBacktest(){
//		AlgorithmModel algorithmModel = islandEvolutionEngine.evolve(32, 5, 5, 5, new TargetFitness(999999, true), new GenerationCount(10));
		AlgorithmModel algorithmModel = evolutionEngine.evolve(256, 5, new TargetFitness(999999, true), new GenerationCount(50));
		WMBacktestEvaluator wmBacktestEvaluator = new WMBacktestEvaluator(new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min));
		BacktestEvaluation backtestEvaluation = wmBacktestEvaluator.getBacktestEvaluation(algorithmModel);
		
		double fitness = backtestEvaluation.getScore();
		
		Co.println("\n\n Best result: " + fitness);
		
		if (bestResult != fitness){
			throw new IllegalComponentStateException("Backtest result did not match best: " + bestResult + ", " + fitness); 
		}		
		
		for (AdjustmentBase adjustmentBase : algorithmModel.wmAdjustment.listOfAdjustmentBase){
			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
		}
		
		Co.print(wmBacktestEvaluator.getBacktestEvaluation(algorithmModel).toString());

		BacktestEvaluation backtestEvaluationOutOfSample = new WMBacktestEvaluator(new HistoricalData(exchange, symbol, DateTools.getDateFromString("03/12/2012"), DateTools.getDateFromString("03/16/2012"), Resolution.min)).getBacktestEvaluation(algorithmModel);
		
		Co.println("\n\n Out of sample");
		
		Co.print(backtestEvaluationOutOfSample.toString());
		
		new BacktestEvaluationWriter().writeToDatabase(backtestEvaluation, false);
	}

	@Override
	public void populationUpdate(PopulationData<? extends AlgorithmModel> data) {
//		HistoricalData historicalData = new HistoricalData(exchange, symbol, DateTools.getDateFromString("03/12/2012"), DateTools.getDateFromString("03/16/2012"), Resolution.min);
//		historicalData.setStartAndEndDatesToExchange();
//		
//		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
//		singleBacktest.remodel(data.getBestCandidate());
//		singleBacktest.selfPopulateBacktestData();
//		singleBacktest.runBacktest();
		
//		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
		
		Co.println("\n--> Generation " + data.getGenerationNumber() + ", " + data.getBestCandidateFitness()); // + " Out of sample: " + backtestEvaluation.getScore() + "\n");
		
		for (AdjustmentBase adjustmentBase : data.getBestCandidate().wmAdjustment.listOfAdjustmentBase){
			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
		}
		
		bestResult = data.getBestCandidateFitness();
	}

	@Override
	public void islandPopulationUpdate(int islandIndex, PopulationData<? extends AlgorithmModel> data) {
		Co.print("\n--> Generation [" + islandIndex + "] " + data.getGenerationNumber() + ", " + data.getBestCandidateFitness());
	}
}
