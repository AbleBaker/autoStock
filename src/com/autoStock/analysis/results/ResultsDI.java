/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsDI extends ResultsBase {
	
	public Date[] arrayOfDates;
	public double[] arrayOfDIPlus;
	public double[] arrayOfDIMinus;
	public double[] arrayOfPrice;
	
	public ResultsDI(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfDIPlus = new double[length];
		this.arrayOfDIMinus = new double[length];
		this.arrayOfPrice = new double[length];
	}
}
