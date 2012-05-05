/**
 * 
 */
package com.autoStock.signal;



/**
 * @author Kevin Kowalewski
 *
 */
public class SignalControl {
	
	public static final int periodLength = 15;
	public static final int periodWindow = 45;
	
	public static final int periodAverageForPPC = periodWindow / 2;
	public static final int periodAverageForADX = 0;
	public static final int periodAverageForCCI = 25;
	public static final int periodAverageForMACD = periodWindow;
	public static final int periodAverageForRSI = 0;
	public static final int periodAverageForSTORSI = 5;
	public static final int periodAverageForTRIX = 0;
	
	public static final double weightForPPC = 1.0;
	public static final double weightForADX = 1.0;
	public static final double weightForCCI = 1.0;
	public static final double weightForMACD = 1.0;
	public static final double weightForSTORSI = 1.0;
	public static final double weightForTRIX = 1.0;
	
	public static void setSignalStrengthForPPC(SignalMetric signalMetric, double percentChange){
		signalMetric.strength = (int) ((percentChange - 1) * 10000);
	}

	public static void setSignalStrengthForADX(SignalMetric signalMetric, double adxValue) {
		signalMetric.strength = (int) adxValue;
	}
	
	public static void setSignalStrengthForCCI(SignalMetric signalMetric, double cciValue){
		signalMetric.strength = (int) cciValue /4;
	}
	
	public static void setSignalStrengthForMACD(SignalMetric signalMetric, double macdValue){
		signalMetric.strength = (int) (macdValue * 10000);
	}
	
	public static void setSignalStrengthForRSI(SignalMetric signalMetric, double rsiValue){
		signalMetric.strength = (int) (rsiValue - 50);
	}
	
	public static void setSignalStrengthForTRIX(SignalMetric signalMetric, double trixValue){
		signalMetric.strength = (int) (trixValue * 1000);
	}
	
	public static void setSignalStrengthForSTORSI(SignalMetric signalMetric, double percentKValue, double percentDValue){
		//Co.println("Have K, D " + percentKValue + ", " + percentDValue + ", " + (percentKValue - percentDValue));
		//signalMetric.strength = Math.max(-100, Math.min(100, (int) (percentKValue + percentDValue) / 2)) - 100;
		signalMetric.strength = 100;
	}
}
