/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsUO extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfUO;
	
	public ResultsUO(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfUO = new double[length];
	}
}
