/**
 * 
 */
package com.autoStock.signal.extras;

import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;

import com.autoStock.signal.signalMetrics.SignalOfEncog;

/**
 * @author Kevin
 *
 */
public class EncogNetworkGenerator {
	public static BasicNetwork getBasicNetwork(int inputs, int outputs){
//		ElmanPattern pattern = new ElmanPattern();
//		JordanPattern pattern = new JordanPattern();
		
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputs);
		pattern.addHiddenLayer(inputs/2);
		pattern.addHiddenLayer(inputs/3);
		//pattern.addHiddenLayer(inputWindow/4);
		pattern.setOutputNeurons(outputs);
		pattern.setActivationFunction(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}
}
