package com.autoStock.encog;

import java.io.File;
import java.io.FileOutputStream;

import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.factory.method.SOMFactory;
import org.encog.ml.factory.train.PSOFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.PersistNEATNetwork;
import org.encog.neural.neat.training.NEATTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.neural.pattern.SOMPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;
import org.encog.neural.som.SOM;
import org.encog.persist.EncogPersistor;
import org.encog.util.obj.SerializeObject;
import org.encog.util.simple.EncogUtility;

import com.autoStock.Co;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.encog.FeedScore.EncogTest;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.tools.Benchmark;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SolveFeedTest {
	private int epoch = 0;
	public static double bestScore = 0;
	private Benchmark bench = new Benchmark();
	private static final int TRAINING_ITERATIONS = 10;
	
	public void execute() {
//		ElmanPattern pattern = new ElmanPattern();
//		JordanPattern pattern = new JordanPattern();
		
//		BasicNetwork pattern = new BasicNetwork();
//		pattern.addLayer(new BasicLayer(null, false, 10));
//		pattern.addLayer(new BasicLayer(new ActivationTANH(), true, 8));
//		pattern.addLayer(new BasicLayer(new ActivationTANH(), true, 4));
//		pattern.addLayer(new BasicLayer(null, false, 3));
//		pattern.getStructure().finalizeStructure();
		
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(5);
		pattern.addHiddenLayer(3);
		pattern.addHiddenLayer(2);
		pattern.setOutputNeurons(2);
		pattern.setActivationFunction(new ActivationTANH());
		
		NEATTraining train = new NEATTraining(new FeedScore(), 3, 3, 256);
		train.setMutationPercent(0.20f);
		train.setPercentToMate(0.25f);
		train.setMatingPopulation(1.0f);
		Co.println("...");
		
		for (int i=0; i<30; i++){
			train.iteration();
			Co.println("--> Error: " + train.getError() + " : " + i);
		}
		
		Co.println("--> All done");
		
		NEATNetwork network = (NEATNetwork) train.getMethod();
		
		try {
			PersistNEATNetwork persistNEATNetwork = new PersistNEATNetwork();
			persistNEATNetwork.save(new FileOutputStream(new File("encog.file")), network);
		}catch(Exception e){e.printStackTrace();}
		
		System.exit(0);
		
//		BasicNetwork network = (BasicNetwork) pattern.generate();
//		network.reset();
		
//		new ConsistentRandomizer(-1,1).randomize(network);
//
//		MLTrain mlTrainMain = new NeuralPSO(network, new ConsistentRandomizer(-1, 1), new FeedScore(), 128);
//		MLTrain mlTrainAlt = new NeuralSimulatedAnnealing(network, new FeedScore(), 10, 2, 128);

//		final StopTrainingStrategy stop = new StopTrainingStrategy(0.001, 10);
//		mlTrainMain.addStrategy(new Greedy());
//		mlTrainMain.addStrategy(new HybridStrategy(mlTrainAlt));
//		train(mlTrainMain, 100);
	
//		train(mlTrainMain, TRAINING_ITERATIONS);
//		train(mlTrainAlt, TRAINING_ITERATIONS);
//		train(mlTrainMain, TRAINING_ITERATIONS);
//		train(mlTrainAlt, TRAINING_ITERATIONS);

		try {
			PersistBasicNetwork persistBasicNetwork = new PersistBasicNetwork();
			persistBasicNetwork.save(new FileOutputStream(new File("encog.file")), network);
		}catch(Exception e){e.printStackTrace();}
		
		System.exit(0);
		
		Co.println("--> All done. Runs: " + FeedScore.runCount);
		Co.println("--> Have results: " + FeedScore.listOfEncogTest.size());
		Co.println("--> Best score: " + bestScore);
		
		for (EncogTest result : FeedScore.listOfEncogTest){
			Co.println("--> Result: " + result.backtestEvaluation.getScore() + ", " + result.backtestEvaluation.percentYield + ", " + new BacktestEvaluationBuilder().buildOutOfSampleEvaluation(result.backtestEvaluation, result.network).percentYield);
			
			if (result.backtestEvaluation.getScore() == bestScore){
//				Co.print(result.table + "\n");
				Co.print(result.backtestEvaluation.toString());
				break;
			}
		}
		
		ApplicationStates.shutdown();
		
	}
	
	private void train(MLTrain mlTrain, int count){
		for (int i = 0; i < count; i++) {
			mlTrain.iteration();
			System.out.println("Epoch #" + epoch + " Score:" + mlTrain.getError());
			
			bench.printTick("Trained");
			
			bestScore = Math.max(mlTrain.getError(), bestScore);
			
			epoch++;
		}
	}
}
