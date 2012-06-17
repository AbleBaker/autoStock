/**
 * 
 */
package com.autoStock.signal;



/**
 * @author Kevin Kowalewski
 *
 */
public class SignalControl {
	
	public static int periodLength = 50;
	public static int periodWindow = 18;
	
	public static int periodAverageForPPC = 3;
	public static int periodAverageForDI = 4; 
	public static int periodAverageForCCI = 2; 
	public static int periodAverageForMACD = 2; 
	public static int periodAverageForTRIX = 2;
	public static final int periodAverageForRSI = 0;
	public static final int periodAverageForSTORSI = 5;
	
	public static double weightForPPC = 1.5;
	public static double weightForDI = 1.0;
	public static double weightForCCI = 1.0;
	public static double weightForMACD = 1.0;
	public static double weightForTRIX = 1.0;
	public static final double weightForSTORSI = 1.0;
	
	public static int pointToSignalLongEntry = 13;
	public static int pointToSignalLongExit = -3;
	public static int pointToSignalShortEntry = -12;
	public static int pointToSignalShortExit = -10;
	
	public static void setSignalStrengthForPPC(SignalMetric signalMetric, double percentChange){
		signalMetric.strength = (int) ((percentChange - 1) * 10000);
	}

	public static void setSignalStrengthForDI(SignalMetric signalMetric, double diValue) {
		signalMetric.strength = (int) diValue * 2;
	}
	
	public static void setSignalStrengthForCCI(SignalMetric signalMetric, double cciValue){
		signalMetric.strength = (int) cciValue / 4;
	}
	
	public static void setSignalStrengthForMACD(SignalMetric signalMetric, double macdValue){
		signalMetric.strength = (int) (macdValue * 5000);
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
