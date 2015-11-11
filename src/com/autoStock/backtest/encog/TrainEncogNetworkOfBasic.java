/**
 * 
 */
package com.autoStock.backtest.encog;

import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.MethodFactory;
import org.encog.ml.genetic.MLMethodGeneticAlgorithm;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.FeedForwardPattern;

import com.autoStock.Co;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin
 *
 */
public class TrainEncogNetworkOfBasic extends TrainEncogWithScore {
	public BasicNetwork network;
	private MLTrain train;
	private int expectedIterations;
	
	public TrainEncogNetworkOfBasic(CalculateScore calculateScore, BasicNetwork network, String networkName, int expectedIterations){
		super(calculateScore, networkName);
		this.expectedIterations = expectedIterations;
		this.network = network;
		
		if (expectedIterations == 0){throw new IllegalArgumentException("Can't hangle 0 expected iterations for Hybrid learning strategy");}
		new NguyenWidrowRandomizer().randomize(network);
	}
	
	@Override
	public void train(int count, double score){
//		train = new MLMethodGeneticAlgorithm(new MethodFactory(){@Override public MLMethod factor() {final BasicNetwork result = (BasicNetwork) network.clone(); ((MLResettable)result).reset(); return result;}}, calculateScore, 1024);
		train = new NeuralPSO(network, new NguyenWidrowRandomizer(), calculateScore, 1024);
//		((NeuralPSO)train).setInertiaWeight(0.5);
		
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
