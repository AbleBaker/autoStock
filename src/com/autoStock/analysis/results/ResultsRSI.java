/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsRSI extends ResultsBase {
	
	public Date[] arrayOfDates;
	public double[] arrayOfRSI;
	public float[] arrayOfPrice;
	
	public ResultsRSI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfRSI = new double[length];
		this.arrayOfPrice = new float[length];
	}
}
