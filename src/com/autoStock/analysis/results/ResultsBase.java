/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.analysis.tools.BasicTimeValuePair;
import com.autoStock.chart.ChartDataFiller;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class ResultsBase {
	
	public ArrayList<BasicTimeValuePair> getResultsAsListOfBasicTimeValuePair(Date[] arrayOfDates, double[] arrayOfValues){
		ArrayList<BasicTimeValuePair> listOfBasicTimeValuePair = new ArrayList<BasicTimeValuePair>();
		
		for (int i=0; i<Math.min(arrayOfDates.length, arrayOfValues.length); i++){
			listOfBasicTimeValuePair.add(new BasicTimeValuePair(arrayOfDates[i], String.valueOf(arrayOfValues[i])));
		}
		
		return listOfBasicTimeValuePair;
	}
	
	public ArrayList<BasicTimeValuePair> getResultsAsListOfBasicTimeValuePair(Date[] arrayOfDates, float[] arrayOfValues){
		ArrayList<BasicTimeValuePair> listOfBasicTimeValuePair = new ArrayList<BasicTimeValuePair>();
		
		for (int i=0; i<Math.min(arrayOfDates.length, arrayOfValues.length); i++){
			listOfBasicTimeValuePair.add(new BasicTimeValuePair(arrayOfDates[i], String.valueOf(arrayOfValues[i])));
		}
		
		return listOfBasicTimeValuePair;
	}
}
