/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.chart.ChartDataFiller;
import com.autoStock.chart.ChartDataFiller.BasicTimeValuePair;

/**
 * @author Kevin Kowalewski
 *
 */
public class ResultsBollingerBands extends ResultsBase {
	public double[] arrayOfUpperBand;
	public double[] arrayOfMiddleBand;
	public double[] arrayOfLowerBand;
	public Date[] arrayOfDates;
	
	public ResultsBollingerBands(int length){
		this.arrayOfUpperBand = new double[length];
		this.arrayOfMiddleBand = new double[length];
		this.arrayOfLowerBand = new double[length];
		this.arrayOfDates = new Date[length];
	}
}
