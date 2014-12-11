package com.autoStock.backtest.encog;

import java.io.File;
import java.io.FileOutputStream;

import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSteepenedSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.ml.factory.method.SOMFactory;
import org.encog.ml.factory.train.PSOFactory;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.ml.train.strategy.Strategy;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
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
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.backtest.encog.EncogScoreProvider.EncogTest;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.signal.SignalCache;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.tools.Benchmark;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.google.gson.Gson;

/**
 * @author Kevin Kowalewski
 * 
 */
public class TrainEncogSignal {
	public static final int TRAINING_ITERATIONS = 30;
	private HistoricalData historicalData;
	private EncogScoreProvider encogScoreProvider = new EncogScoreProvider();
	private TrainEncogBase encogTrainer;
	
	public static enum EncogNetworkType {
		neat,
		basic,
	}
	
	public TrainEncogSignal(AlgorithmModel algorithmModel, HistoricalData historicalData){
		this.historicalData = historicalData;
		encogScoreProvider.setDetails(algorithmModel, historicalData);		
		
		if (SignalOfEncog.encogNetworkType == EncogNetworkType.basic){
			encogTrainer = new TrainEncogNetworkOfBasic(encogScoreProvider, historicalData.exchange.exchangeName + "-" + historicalData.symbol.symbolName, TRAINING_ITERATIONS);
		}else if (SignalOfEncog.encogNetworkType == EncogNetworkType.neat){
			encogTrainer = new TrainEncogNetworkOfNeat(encogScoreProvider, historicalData.exchange.exchangeName + "-" + historicalData.symbol.symbolName);
		}
	}
	
	public void execute(AlgorithmModel algorithmModel, double score) {
		encogScoreProvider.setDetails(algorithmModel, historicalData);
		
		Co.println("...");
		
//		SignalCache signalCache = new SignalCache();
//		SignalCache.erase();
//		encogScoreProvider.setSignalCache(signalCache);
		
		encogTrainer.train(TRAINING_ITERATIONS, score);
		encogTrainer.saveNetwork();
	}
	
	public void setDetails(AlgorithmModel algorithmModel) {
		encogScoreProvider.setDetails(algorithmModel, historicalData);
	}
	
	public TrainEncogBase getTrainer(){
		return encogTrainer;
	}

	public boolean networkExists() {
		return encogTrainer.networkExists();
	}

	public EncogScoreProvider getScoreProvider() {
		return encogScoreProvider;
	}
}
