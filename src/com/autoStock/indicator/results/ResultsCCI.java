/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsCCI extends ResultsBase {
	
	public Date[] arrayOfDates;
	public double[] arrayOfCCI;
	public double[] arrayOfPrice;
	
	public ResultsCCI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfCCI = new double[length];
		this.arrayOfPrice = new double[length];
	}
	
	public double getLastClippedResult(){
		return arrayOfCCI[0];
	}
}
