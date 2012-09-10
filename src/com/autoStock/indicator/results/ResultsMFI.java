/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsMFI extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfMFI;
	
	public ResultsMFI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfMFI = new double[length];
	}
}
