package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;

/**
 * @author Kevin Kowalewski
 *
 */
public class GuageOfThresholdMet extends GuageBase {
	private SignalGuage signalGuage;
	private int[] arrayOfValues;
	
	public GuageOfThresholdMet(SignalGuage signalGuage, int[] arrayOfValues){
		this.arrayOfValues = arrayOfValues;
		this.signalGuage = signalGuage;
	}

	@Override
	public boolean isQualified(){
		if (signalGuage.signalBounds == SignalBounds.bounds_upper){
			if (arrayOfValues[arrayOfValues.length-1] >= signalGuage.threshold){
				return true;
			}
		} else if (signalGuage.signalBounds == SignalBounds.bounds_lower) {
			if (arrayOfValues[arrayOfValues.length-1] <= signalGuage.threshold){
				return true;
			}
		}else{
			throw new UnsupportedOperationException();
		}
		
		return false;
	}
}
