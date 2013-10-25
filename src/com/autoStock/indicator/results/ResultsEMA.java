package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsEMA extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfEMA;
	
	public ResultsEMA(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfEMA = new double[length];
	}
}
