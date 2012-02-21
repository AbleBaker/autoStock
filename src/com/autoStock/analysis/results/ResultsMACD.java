/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsMACD extends ResultsBase {
	public Date[] arrayOfDates;
	public float[] arrayOfPrice;	
	public double[] arrayOfMACD;
	public double[] arrayOfMACDSignal;
	public double[] arrayOfMACDHistogram;
	
	public ResultsMACD(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfPrice = new float[length];
		this.arrayOfMACD = new double[length];
		this.arrayOfMACDSignal = new double[length];
		this.arrayOfMACDHistogram = new double[length];
	}
}
