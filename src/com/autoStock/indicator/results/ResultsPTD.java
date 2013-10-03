package com.autoStock.indicator.results;

import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsPTD extends ResultsBase {
	public Date[] arrayOfDates;
	public double[] arrayOfPTD;
	
	public ResultsPTD(int length){
		this.arrayOfDates = new Date[length];
		this.arrayOfPTD = new double[length];
	}
}
