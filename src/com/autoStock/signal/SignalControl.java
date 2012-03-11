/**
 * 
 */
package com.autoStock.signal;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalControl {
	
	public static final int periodAverageForPPC = 3;
	public static final int periodAverageForADX = 4;
	public static final int periodAverageForCCI = 8;
	public static final int periodAverageForMACD = 2;
	
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
		if (adxValue > 40){
			signalMetric.strength = (int) (adxValue - 40);
		}else if (adxValue < 10){
			signalMetric.strength = (int) (adxValue - 40);
		}else if (adxValue < 0){
			signalMetric.strength = -100;
		}else {
			//pass
		}
	}
	
	public static void setSignalStrengthForCCI(SignalMetric signalMetric, double cciValue){
		if (cciValue > 50){
			signalMetric.strength= (int) Math.min(cciValue, 100);
		}else if (cciValue < 0){
			signalMetric.strength = (int) Math.max(cciValue, -100);
		}else {
			//pass
		}
	}
	
	public static void setSignalStrengthForMACD(SignalMetric signMetric, double macdValue){
		signMetric.strength = (int) (macdValue*10000);
	}
}
