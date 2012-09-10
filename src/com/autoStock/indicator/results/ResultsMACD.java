/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsMACD extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfMACD;
	public double[] arrayOfMACDSignal;
	public double[] arrayOfMACDHistogram;
	
	public ResultsMACD(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfMACD = new double[length];
		this.arrayOfMACDSignal = new double[length];
		this.arrayOfMACDHistogram = new double[length];
	}
}
