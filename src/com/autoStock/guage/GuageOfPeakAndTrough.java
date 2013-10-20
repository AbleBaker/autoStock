package com.autoStock.guage;

import java.util.Arrays;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.MathTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class GuageOfPeakAndTrough extends GuageBase {
	
	public GuageOfPeakAndTrough(SignalGuage signalGuage, double[] arrayOfValues) {
		super(signalGuage, arrayOfValues);
	}

	@Override
	public boolean isQualified() {
		if (arrayOfValues.length >= 10){
			if (signalGuage.signalBounds == SignalBounds.bounds_upper){
				return MathTools.isDecreasing(Arrays.copyOfRange(arrayOfValues, 6, 10), 1, false);
			}
			else if (signalGuage.signalBounds == SignalBounds.bounds_lower){
				return MathTools.isIncreasing(Arrays.copyOfRange(arrayOfValues, 6, 10), 1, false);
			}else{
				throw new IllegalArgumentException("No signal bounds matched: " + signalGuage.signalBounds.name());
			}
		}
		
		return false;
	}
}
