/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsSTORSI extends ResultsBase {
	
	public Date[] arrayOfDates;
	public double[] arrayOfPercentK;
	public double[] arrayOfPercentD;
	public float[] arrayOfPrice;
	
	public ResultsSTORSI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfPercentK = new double[length]; 
		this.arrayOfPercentD = new double[length];
		this.arrayOfPrice = new float[length];
	}
}
