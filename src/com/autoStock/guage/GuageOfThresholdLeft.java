package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class GuageOfThresholdLeft extends GuageBase {
	private SignalGuage signalGuage;
	private int[] arrayOfValues;
	
	public GuageOfThresholdLeft(SignalGuage signalGuage, int[] arrayOfValues){
		this.arrayOfValues = arrayOfValues;
		this.signalGuage = signalGuage;
	}

	@Override
	public boolean isQualified(){
		boolean isQualifiedMet =  arrayOfValues.length > 1 && new GuageOfThresholdMet(signalGuage, ArrayTools.subArray(arrayOfValues, 0, arrayOfValues.length-1)).isQualified();
		
		if (signalGuage.signalBounds == SignalBounds.bounds_upper){
			if (arrayOfValues[arrayOfValues.length-1] < signalGuage.threshold && isQualifiedMet){
				return true;
			}
		} else {
			if (arrayOfValues[arrayOfValues.length-1] > signalGuage.threshold && isQualifiedMet){
				return true;
			}
		}
		
		return false;
	}
}
