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
	
	public ResultsBollingerBands(int length){
		this.arrayOfUpperBand = new double[length];
		this.arrayOfMiddleBand = new double[length];
		this.arrayOfLowerBand = new double[length];
	}
	
	public ArrayList<BasicTimeValuePair> getResultsAsListOfBasicTimeValuePair(Date[] arrayOfDates, double[] arrayOfValues){
		ArrayList<BasicTimeValuePair> listOfBasicTimeValuePair = new ArrayList<BasicTimeValuePair>();
		
		for (int i=0; i<Math.min(arrayOfDates.length, arrayOfValues.length); i++){
			listOfBasicTimeValuePair.add(new ChartDataFiller(). new BasicTimeValuePair(arrayOfDates[i], String.valueOf(arrayOfValues[i])));
		}
		
		return listOfBasicTimeValuePair;
	}
}
