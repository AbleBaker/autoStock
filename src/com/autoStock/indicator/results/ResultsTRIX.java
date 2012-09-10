/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsTRIX extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfTRIX;
	public double[] arrayOfPrice;
	
	public ResultsTRIX(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfTRIX = new double[length];
	}
}
