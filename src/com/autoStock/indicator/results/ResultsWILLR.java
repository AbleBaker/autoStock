/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsWILLR extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfWILLR;
	public double[] arrayOfPrice;
	
	public ResultsWILLR(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfWILLR = new double[length];
	}
}
