/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsSAR extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfSAR;
	
	public ResultsSAR(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfSAR = new double[length];
	}
}
