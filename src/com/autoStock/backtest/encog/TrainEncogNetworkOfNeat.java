/**
 * 
 */
package com.autoStock.backtest.encog;

import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.training.NEATTraining;
import org.encog.neural.networks.training.CalculateScore;

import com.autoStock.Co;
import com.autoStock.signal.signalMetrics.SignalOfEncog;

/**
 * @author Kevin
 *
 */
public class TrainEncogNetworkOfNeat extends TrainEncogBase {
	private static NEATTraining train;
	
	public TrainEncogNetworkOfNeat(CalculateScore calculateScore, String networkName){
		super(calculateScore, networkName);
		train = new NEATTraining(calculateScore, new NEATPopulation(SignalOfEncog.getInputWindowLength(), 4, 512));
		train.setPercentToMate(0.25);
		train.setMutationPercent(0.25f);
		train.setMatingPopulation(0.50f);
		train.setSnapshot(true);
	}
	
	@Override
	public void train(int count) {
		for (int i = 0; i < count; i++) {
			train.iteration();
			Co.println("--> Training... " + i + ", " + train.getError());
			bestScore = Math.max(train.getError(), bestScore);
		}
	}

	@Override
	public boolean saveNetwork() {
		return encogNetworkProvider.saveNeatNetwork((NEATNetwork) train.getMethod());
	}
}
