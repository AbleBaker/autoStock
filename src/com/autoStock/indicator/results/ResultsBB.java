/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsBB extends ResultsBase {
	public double[] arrayOfUpperBand;
	public double[] arrayOfMiddleBand;
	public double[] arrayOfLowerBand;
	public Date[] arrayOfDates;
	
	public ResultsBB(int length){
		this.arrayOfUpperBand = new double[length];
		this.arrayOfMiddleBand = new double[length];
		this.arrayOfLowerBand = new double[length];
		this.arrayOfDates = new Date[length];
	}
}
