/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalMetric {
	public int strength;
	public SignalTypeMetric signalTypeMetric;
	
	public SignalMetric(int strength, SignalTypeMetric signalTypeMetric) {
		this.strength = strength;
		this.signalTypeMetric = signalTypeMetric;
	}
}
