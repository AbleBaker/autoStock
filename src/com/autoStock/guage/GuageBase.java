package com.autoStock.guage;


/**
 * @author Kevin Kowalewski
 *
 */
public abstract class GuageBase {
	protected SignalGuage signalGuage;
	protected double[] arrayOfValues;
	public abstract boolean isQualified();
	
	public GuageBase(SignalGuage signalGuage, double[] arrayOfValues) {
		this.signalGuage = signalGuage;
		this.arrayOfValues = arrayOfValues;
	}
}
