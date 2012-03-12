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
	public static final int periodAverageForADX = 4;
	public static final int periodAverageForCCI = 8;
	public static final int periodAverageForMACD = 2;
	
	public static final double weightForPPC = 1.0;
	public static final double weightForADX = 1.0;
	public static final double weightForCCI = 0.5;
	public static final double weightForMACD = 1.0;
	
	public static void setSignalStrengthForPPC(SignalMetric signalMetric, double percentChange){
		signalMetric.strength = (int) ((percentChange - 1) * 1000 * 5);
		
//		if (percentChange > 1.00){
//			signalMetric.strength = (int) ((percentChange - 1) * 1000 * 2);
//		}else if (percentChange < 0.99){
//			signalMetric.strength = (int) ((percentChange - 1) * 1000 * 1);
//		}else {
//			//pass
//		}
	}

	public static void setSignalStrengthForADX(SignalMetric signalMetric, double adxValue) {
		signalMetric.strength = (int) adxValue;
//		if (adxValue > 40){
//			signalMetric.strength = (int) (adxValue - 40);
//		}else if (adxValue < 10){
//			signalMetric.strength = (int) (adxValue - 40);
//		}else if (adxValue < 0){
//			signalMetric.strength = -100;
//		}else {
//			//pass
//		}
	}
	
	public static void setSignalStrengthForCCI(SignalMetric signalMetric, double cciValue){
		signalMetric.strength = (int) cciValue / 2;
		
		//cciValue /= 2;
		
//		if (cciValue > 50){
//			signalMetric.strength= (int) Math.min(cciValue, 100);
//		}else if (cciValue < 0){
//			signalMetric.strength = (int) Math.max(cciValue, -100);
//		}else {
//			//pass
//		}
	}
	
	public static void setSignalStrengthForMACD(SignalMetric signalMetric, double macdValue){
		
		signalMetric.strength = (int) (macdValue * 100);
		
//		if (macdValue > 0){
//			signalMetric.strength = (int) (macdValue*1000 * 10);
//		}else{
//			signalMetric.strength = (int) (macdValue*1000 * 5);
//		}
	}
}
