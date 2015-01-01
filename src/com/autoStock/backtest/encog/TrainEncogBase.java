/**
 * 
 */
package com.autoStock.backtest.encog;

import java.awt.IllegalComponentStateException;

import org.encog.neural.networks.training.CalculateScore;

import com.autoStock.signal.extras.EncogNetworkProvider;

/**
 * @author Kevin
 *
 */
public abstract class TrainEncogBase {
	public double bestScore;
	public String networkName;
	protected CalculateScore calculateScore;
	protected EncogNetworkProvider encogNetworkProvider = new EncogNetworkProvider();
		
	public TrainEncogBase(CalculateScore calculateScore, String networkName){
		this.calculateScore = calculateScore;
		this.networkName = networkName; 
	}
	
	public abstract void train(int count, double score);
	public abstract boolean saveNetwork();

	public boolean networkExists() { 
		if (this instanceof TrainEncogNetworkOfBasic){
			return encogNetworkProvider.getNeatNetwork(networkName) != null; 
		}else if (this instanceof TrainEncogNetworkOfNeat){
			return encogNetworkProvider.getNeatNetwork(networkName) != null; 
		}
		
		throw new IllegalComponentStateException("Don't understand network type!");
	}
}
