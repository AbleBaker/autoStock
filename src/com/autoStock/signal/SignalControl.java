/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalControl {
	
	public static final int periodAverageForPPC = 0;
	public static final int periodAverageForADX = 0;
	public static final int periodAverageForCCI = 0;
	public static final int periodAverageForMACD = 0;
	
	public static final double weightForPPC = 1.0;
	public static final double weightForADX = 1.0;
	public static final double weightForCCI = 1.0;
	public static final double weightForMACD = 1.0;
	
	public static void setSignalStrengthForPPC(SignalMetric signalMetric, double percentChange){
		signalMetric.strength = (int) ((percentChange - 1) * 1000 * 10);
	}

	public static void setSignalStrengthForADX(SignalMetric signalMetric, double adxValue) {
		signalMetric.strength = (int) adxValue;
	}
	
	public static void setSignalStrengthForCCI(SignalMetric signalMetric, double cciValue){
		signalMetric.strength = (int) cciValue;
	}
	
	public static void setSignalStrengthForMACD(SignalMetric signalMetric, double macdValue){
		signalMetric.strength = (int) (macdValue * 1000);
	}
}
