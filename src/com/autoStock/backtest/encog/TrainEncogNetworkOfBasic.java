/**
 * 
 */
package com.autoStock.backtest.encog;

import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.CalculateScore;
import org.encog.ml.factory.train.NEATGAFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.pso.NeuralPSO;

import com.autoStock.Co;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin
 *
 */
public class TrainEncogNetworkOfBasic extends TrainEncogBase {
	public BasicNetwork network;
	private MLTrain train;
	private int expectedIterations;
	private boolean isForBacktestScore = true;
	
	public TrainEncogNetworkOfBasic(CalculateScore calculateScore, BasicNetwork network, String networkName, int expectedIterations){
		super(calculateScore, networkName);
		this.expectedIterations = expectedIterations;
		this.network = network;
		
		if (expectedIterations == 0){throw new IllegalArgumentException("Can't hangle 0 expected iterations for Hybrid learning strategy");}
		new NguyenWidrowRandomizer().randomize(network);
	}
	
	@Override
	public void train(int count, double score){
		train = new NeuralPSO(network, new NguyenWidrowRandomizer(), calculateScore, 1024);
//		train.addStrategy(new HybridStrategy(new NeuralSimulatedAnnealing(network, calculateScore, 10, 2, 64), 0, (int)expectedIterations/2, (int)expectedIterations/2));
		
		//NeuralGeneticAlgorithm neuralGeneticAlgorithm = new NeuralGeneticAlgorithm(network, new NguyenWidrowRandomizer(), calculateScore, 128, 0.10f, 0.40f);
//		neuralGeneticAlgorithm.setThreadCount(Runtime.getRuntime().availableProcessors() * 2);
//		train.addStrategy(new HybridStrategy(neuralGeneticAlgorithm, 0.01, (int)expectedIterations/2, (int)expectedIterations/2));
		
		
		for (int i = 0; i < count; i++) {
			train.iteration();
			Co.println("--> Training... " + i + ", " + MathTools.round(train.getError()));
			bestScore = Math.max(train.getError(), bestScore);
		}
		
		train.finishTraining();
	}

	@Override
	public boolean saveNetwork() {
		return encogNetworkProvider.saveNetwork(network, networkName);
	}
}
