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
	
	public ResultsCCI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfCCI = new double[length];
	}
}
