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
	protected EncogNetworkProvider encogNetworkProvider;
		
	public TrainEncogBase(CalculateScore calculateScore, String networkName){
		this.networkName = networkName;
		encogNetworkProvider = new EncogNetworkProvider(networkName);
	}
	
	public abstract void train(int count);
	public abstract boolean saveNetwork();
	
//	public boolean saveNetwork(){
//		if (this instanceof TrainEncogNetworkOfBasic){
//			
//		}else if (this instanceof TrainEncogNetworkOfNeat){
//			encogNetworkProvider.saveNeatNetwork(neatNetwork);
//		}
//		
//		throw new IllegalComponentStateException("Don't understand network type!");
//	}
	
	public boolean networkExists() {//Get the network, don't just check for its existance 
		if (this instanceof TrainEncogNetworkOfBasic){
			return encogNetworkProvider.getNeatNetwork() != null; 
		}else if (this instanceof TrainEncogNetworkOfNeat){
			return encogNetworkProvider.getNeatNetwork() != null; 
		}
		
		throw new IllegalComponentStateException("Don't understand network type!");
	}
}
