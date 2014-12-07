/**
 * 
 */
package com.autoStock.backtest.encog;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSteepenedSigmoid;
import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.CalculateScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.plugin.system.SystemLoggingPlugin;
import org.encog.util.logging.EncogLogging;

import com.autoStock.Co;
import com.autoStock.signal.signalMetrics.SignalOfEncog;

/**
 * @author Kevin
 *
 */
public class TrainEncogNetworkOfBasic extends TrainEncogBase{
	private BasicNetwork network;
	private MLTrain mlTrainMain;
	
	public TrainEncogNetworkOfBasic(CalculateScore calculateScore, String networkName){
		super(calculateScore, networkName);
		network = getMLNetwork();
		
		//((SystemLoggingPlugin)Encog.getInstance().getLoggingPlugin()).setLogLevel(EncogLogging.LEVEL_DEBUG);
		//((SystemLoggingPlugin)Encog.getInstance().getLoggingPlugin()).startConsoleLogging();
		
		mlTrainMain = new NeuralPSO(network, new NguyenWidrowRandomizer(), calculateScore, 128);
		mlTrainMain.addStrategy(new HybridStrategy(new NeuralSimulatedAnnealing(network, calculateScore, 10, 2, 128), 0.10, 32, 15));
		mlTrainMain.addStrategy(new HybridStrategy(new NeuralGeneticAlgorithm(network, new NguyenWidrowRandomizer(), calculateScore, 128, 0.10f, 0.25f), 0.10, 32, 15));
		//mlTrainMain.addStrategy(new StopTrainingStrategy(0.01, 10));
		
		new NguyenWidrowRandomizer().randomize(network);
	}
	
	@Override
	public void train(int count){
		for (int i = 0; i < count; i++) {
			mlTrainMain.iteration();
			Co.println("--> Training... " + i + ", " + mlTrainMain.getError());
			bestScore = Math.max(mlTrainMain.getError(), bestScore);
		}
	}
	
	public BasicNetwork getMLNetwork(){
//		ElmanPattern pattern = new ElmanPattern();
//		JordanPattern pattern = new JordanPattern();
		
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(SignalOfEncog.getInputWindowLength());
		pattern.addHiddenLayer(100);
		pattern.addHiddenLayer(75);
		pattern.addHiddenLayer(50);
		pattern.setOutputNeurons(4);
		pattern.setActivationFunction(new ActivationSteepenedSigmoid());
		return (BasicNetwork) pattern.generate();
	}

	@Override
	public boolean saveNetwork() {
		return encogNetworkProvider.saveNetwork(network);
	}
}
