/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsROC extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfROC;
	
	public ResultsROC(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfROC = new double[length];
	}
}
