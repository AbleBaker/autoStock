/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsADX extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfADX;
	
	public ResultsADX(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfADX = new double[length];
	}
	
	public double getLastClippedResult(){
		return arrayOfADX[0];
	}
}
