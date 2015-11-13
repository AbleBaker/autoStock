package com.autoStock.backtest.encog;

import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationSteepenedSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.pattern.FeedForwardPattern;

import com.autoStock.Co;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.signal.SignalCache;
import com.autoStock.signal.extras.EncogNetworkCache;
import com.autoStock.signal.extras.EncogNetworkGenerator;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.trading.types.HistoricalData;

/**
 * @author Kevin Kowalewski
 * 
 */
public class TrainEncogSignal {
	public static final int TRAINING_ITERATIONS = 30;
	private boolean saveNetwork;
	private HistoricalData historicalData;
	private EncogScoreProvider encogScoreProvider = new EncogScoreProvider();
	private TrainEncogBase encogTrainer;
	
	public static enum EncogNetworkType {
		neat,
		basic,
	}
	
	public TrainEncogSignal(AlgorithmModel algorithmModel, HistoricalData historicalData, boolean saveNetwork, String networkSufix){
		this.historicalData = historicalData;
		this.saveNetwork = saveNetwork;
		encogScoreProvider.setDetails(algorithmModel, historicalData);
		
		if (SignalOfEncog.encogNetworkType == EncogNetworkType.basic){
			encogTrainer = new TrainEncogNetworkOfBasic(encogScoreProvider, EncogNetworkGenerator.getBasicNetwork(SignalOfEncog.getInputWindowLength(), 6, new ActivationTANH()), historicalData.exchange.exchangeName + "-" + historicalData.symbol.symbolName + "-" + networkSufix, TRAINING_ITERATIONS);
		}else if (SignalOfEncog.encogNetworkType == EncogNetworkType.neat){
			encogTrainer = new TrainEncogNetworkOfNeat(encogScoreProvider, historicalData.exchange.exchangeName + "-" + historicalData.symbol.symbolName + "-" + networkSufix);
		}
	}
	
	public void execute(AlgorithmModel algorithmModel, double score) {
		encogScoreProvider.setDetails(algorithmModel, historicalData);
		
		Co.println("...");
		
//		SignalCache signalCache = new SignalCache();
//		SignalCache.erase();
//		encogScoreProvider.setSignalCache(signalCache);
		
		encogTrainer.train(TRAINING_ITERATIONS, score);
		if (saveNetwork){encogTrainer.saveNetwork();}
		EncogNetworkCache.getInstance().clear();
		
		Co.println(" . ");
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
