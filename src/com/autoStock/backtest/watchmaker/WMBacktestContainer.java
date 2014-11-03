package com.autoStock.backtest.watchmaker;

import java.awt.IllegalComponentStateException;
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
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.algorithm.DummyAlgorithm;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.backtest.encog.TrainEncogSignal;
import com.autoStock.backtest.watchmaker.WMEvolutionParams.WMEvolutionThorough;
import com.autoStock.backtest.watchmaker.WMEvolutionParams.WMEvolutionType;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestContainer implements EvolutionObserver<AlgorithmModel>, IslandEvolutionObserver<AlgorithmModel> {
	private WMEvolutionType evolutionType = WMEvolutionType.type_island;
	private WMEvolutionThorough evolutionThorough = WMEvolutionThorough.thorough_quick;
	public DummyAlgorithm algorithm;
	public Symbol symbol;
	public Exchange exchange;
	public Date dateStart;
	public Date dateEnd;
	private double bestResult = 0;
	private HistoricalData historicalData;

	private WMCandidateFactory wmCandidateFactory;
	private MersenneTwisterRNG randomNumberGenerator = new MersenneTwisterRNG();
	
	private EvolutionaryOperator<AlgorithmModel> evolutionaryPipeline;
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
		
		evolutionaryPipeline = new EvolutionPipeline<AlgorithmModel>(operators);
	}
	
	public void runBacktest(){
		AlgorithmModel algorithmModel = null;
		
//		trainEncog(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol), historicalData, true);
		
		if (evolutionType == WMEvolutionType.type_island){
			islandEvolutionEngine = new IslandEvolution<>(evolutionThorough == WMEvolutionThorough.thorough_quick ? 4 : 16, 
					new RingMigration(), 
					wmCandidateFactory, 
					evolutionaryPipeline, 
					new WMBacktestEvaluator(historicalData), 
					new StochasticUniversalSampling(), 
					randomNumberGenerator);
			
			islandEvolutionEngine.addEvolutionObserver(this);
			
			if (evolutionThorough == WMEvolutionThorough.thorough_quick){
				algorithmModel = islandEvolutionEngine.evolve(256, 8, 4, 8, new TargetFitness(999999, true), new GenerationCount(3));
			}else{
				algorithmModel = islandEvolutionEngine.evolve(512, 16, 64, 16, new TargetFitness(999999, true), new GenerationCount(8));
			}
		}else if (evolutionType == WMEvolutionType.type_generational){
			evolutionEngine = new GenerationalEvolutionEngine<AlgorithmModel>(wmCandidateFactory,
				evolutionaryPipeline, 
				new WMBacktestEvaluator(historicalData), 
				new StochasticUniversalSampling(),
				randomNumberGenerator);
			
			evolutionEngine.addEvolutionObserver(this);
			
			if (evolutionThorough == WMEvolutionThorough.thorough_quick){
				algorithmModel = evolutionEngine.evolve(64, 16, new TargetFitness(999999, true), new GenerationCount(3));
			}else{
				algorithmModel = evolutionEngine.evolve(1024, 32, new TargetFitness(999999, true), new GenerationCount(16));
			}
		}else{
			throw new IllegalArgumentException();
		}
				
		WMBacktestEvaluator wmBacktestEvaluator = new WMBacktestEvaluator(new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min));
		BacktestEvaluation backtestEvaluation = wmBacktestEvaluator.getBacktestEvaluation(algorithmModel, true);
		
		double fitness = backtestEvaluation.getScore();
		
		Co.println("\n\nBest result: " + fitness + "\n");
		
		if (bestResult != fitness){
			Co.println("--> Warning: bestResult != current evaluation\n");
//			throw new IllegalComponentStateException("Backtest result did not match best: " + bestResult + ", " + fitness); 
		}		
		
//		for (AdjustmentBase adjustmentBase : algorithmModel.wmAdjustment.listOfAdjustmentBase){
//			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
//		}
		
		Co.print(wmBacktestEvaluator.getBacktestEvaluation(algorithmModel, true).toString());
		
//		Co.print(new TableController().displayTable(AsciiTables.algorithm_test,);)
		
		Date dateOutOfSample = DateTools.getFirstWeekdayAfter(dateEnd);

		BacktestEvaluation backtestEvaluationOutOfSample = new WMBacktestEvaluator(new HistoricalData(exchange, symbol, dateOutOfSample, dateOutOfSample, Resolution.min)).getBacktestEvaluation(algorithmModel, true);
		
		Co.println("\n\n Out of sample: " + dateOutOfSample + ", " + backtestEvaluationOutOfSample.percentYield);
		
//		Co.print(backtestEvaluationOutOfSample.toString());
		
		if (backtestEvaluation.getScore() > 0){
			new BacktestEvaluationWriter().writeToDatabase(backtestEvaluation, false);
		}
	}

	@Override
	public void populationUpdate(PopulationData<? extends AlgorithmModel> data) {		
//		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
//		singleBacktest.remodel(data.getBestCandidate());
//		singleBacktest.selfPopulateBacktestData();
//		singleBacktest.runBacktest();
//		
//		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
		
		Co.println("\n\n--> Generation " + data.getGenerationNumber() + ", " + data.getBestCandidateFitness() + "\n"); // + " Out of sample: " + backtestEvaluation.getScore() + "\n");
		
//		for (AdjustmentBase adjustmentBase : data.getBestCandidate().wmAdjustment.listOfAdjustmentBase){
//			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
//		}
		
		bestResult = data.getBestCandidateFitness();
		
		if (data.getGenerationNumber() != 2){
			trainEncog(data.getBestCandidate(), historicalData);
		}
	}

	@Override
	public void islandPopulationUpdate(int islandIndex, PopulationData<? extends AlgorithmModel> data) {
		Co.print("\n--> Generation [" + islandIndex + "] " + data.getGenerationNumber() + ", " + data.getBestCandidateFitness());
	}
	
	private void trainEncog(AlgorithmModel algorithmModel, HistoricalData historicalData){		
		new TrainEncogSignal(algorithmModel, historicalData).execute();
	}
}
