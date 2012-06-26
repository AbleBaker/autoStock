/**
 * 
 */
package com.autoStock.signal;

import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.signal.SignalDefinitions.SignalTypeMetric;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SignalControl {

	public static int periodLength = 60;

	public static int periodAverageForPPC = 4;
	public static int periodAverageForDI = 0;
	public static int periodAverageForCCI = 0;
	public static int periodAverageForMACD = 0;
	public static int periodAverageForTRIX = 0;
	public static final int periodAverageForRSI = 0;
	public static final int periodAverageForSTORSI = 0;

	public static int pointToSignalLongEntry = 16;
	public static int pointToSignalLongExit = -3;
	public static int pointToSignalShortEntry = -10;
	public static int pointToSignalShortExit = -5;

	public static void setSignalStrengthForSTORSI(SignalMetric signalMetric, double percentKValue, double percentDValue) {
		// Co.println("Have K, D " + percentKValue + ", " + percentDValue + ", "
		// + (percentKValue - percentDValue));
		// signalMetric.strength = Math.max(-100, Math.min(100, (int)
		// (percentKValue + percentDValue) / 2)) - 100;
		signalMetric.strength = 100;
	}
}
