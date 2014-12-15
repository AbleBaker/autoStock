package com.autoStock.backtest.encog;

import com.autoStock.Co;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.trading.types.HistoricalData;

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
