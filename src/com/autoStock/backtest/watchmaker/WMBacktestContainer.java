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
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.ListenerOfBacktestCompleted;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestContainer implements EvolutionObserver<AlgorithmModel> {
	public DummyAlgorithm algorithm;
	public Symbol symbol;
	public Exchange exchange;
	public Date dateStart;
	public Date dateEnd;
	private double bestResult = 0;

	private WMCandidateFactory wmCandidateFactory;
	private MersenneTwisterRNG randomNumberGenerator = new MersenneTwisterRNG();
	private GenerationalEvolutionEngine<AlgorithmModel> evolutionEngine;
	

	public WMBacktestContainer(Symbol symbol, Exchange exchange, Date dateStart, Date dateEnd) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	
		algorithm = new DummyAlgorithm(exchange, symbol, AlgorithmMode.mode_backtest_with_adjustment, new BasicAccount(AccountProvider.defaultBalance));
		
		wmCandidateFactory = new WMCandidateFactory(this);
		List<EvolutionaryOperator<AlgorithmModel>> operators = new ArrayList<EvolutionaryOperator<AlgorithmModel>>();
		operators.add(new WMMutation(new Probability(0.25)));
		operators.add(new WMCrossover(1));
		
		EvolutionaryOperator<AlgorithmModel> evolutionaryPipeline = new EvolutionPipeline<AlgorithmModel>(operators);
		
//		new IslandEvolution<>(10, 
//				new RingMigration(), 
//				wmCandidateFactory, 
//				evolutionaryPipeline, 
//				new WMBacktestEvaluator(), 
//				new RouletteWheelSelection(), 
//				randomNumberGenerator);
		
		evolutionEngine = new GenerationalEvolutionEngine<AlgorithmModel>(wmCandidateFactory,
			evolutionaryPipeline, 
			new WMBacktestEvaluator(), 
			new RouletteWheelSelection(), 
			randomNumberGenerator);
		
		evolutionEngine.addEvolutionObserver(this);
	}
	
	public void runBacktest(){
		AlgorithmModel algorithmModel = evolutionEngine.evolve(256, 5, new TargetFitness(999999, true), new GenerationCount(100));
		double fitness = new WMBacktestEvaluator().getFitness(algorithmModel, null);
		
		Co.println("\n\n Best result: " + fitness);
		
		for (AdjustmentBase adjustmentBase : algorithmModel.wmAdjustment.listOfAdjustmentBase){
			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
		}
		
		if (bestResult != fitness){
			throw new IllegalComponentStateException("Backtest result did not match best: " + bestResult + ", " + fitness); 
		}
		
		Co.print(new WMBacktestEvaluator().getBacktestEvaluation(algorithmModel).toString());
	}

	@Override
	public void populationUpdate(PopulationData<? extends AlgorithmModel> data) {
		Co.print("\n--> Generation " + data.getGenerationNumber() + ", " + data.getBestCandidateFitness() + "\n");	
		
		for (AdjustmentBase adjustmentBase : data.getBestCandidate().wmAdjustment.listOfAdjustmentBase){
			Co.println(new BacktestEvaluationBuilder().getAdjustmentDescriptor(adjustmentBase).toString());
		}
		
		bestResult = data.getBestCandidateFitness();
	}
}
