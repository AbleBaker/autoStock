/**
 * 
 */
package com.autoStock.signal;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalStrenghts {
	public static void setSignalStrengthForPPC(SignalMetric signalMetric, double percentChange){
		if (percentChange > 1.01){
			signalMetric.strength = (int) ((percentChange - 1) * 1000 * 2);
		}else if (percentChange < 0.99){
			signalMetric.strength = (int) ((percentChange - 1) * 1000 * 1);
		}else {
			//pass
		}
	}

	public static void setSignalStrengthForADX(SignalMetric signalMetric, double adxValue) {
		
	}
}
