/**
 * 
 */
package com.autoStock.analysis.results;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.chart.ChartDataFiller;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.tools.BasicTimeValuePair;
import com.autoStock.tools.ResultsTools;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class ResultsBase {
	
	public ArrayList<BasicTimeValuePair> getResultsAsListOfBasicTimeValuePair(Date[] arrayOfDates, double[] arrayOfValues){
		return ResultsTools.getResultsAsListOfBasicTimeValuePair(arrayOfDates, arrayOfValues);
	}
}
