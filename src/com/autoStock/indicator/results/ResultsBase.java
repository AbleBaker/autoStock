/**
 * 
 */
package com.autoStock.indicator.results;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.tools.ResultsTools;
import com.autoStock.types.basic.BasicTimeValuePair;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class ResultsBase {
	
	public ArrayList<BasicTimeValuePair> getResultsAsListOfBasicTimeValuePair(Date[] arrayOfDates, double[] arrayOfValues){
		return ResultsTools.getResultsAsListOfBasicTimeValuePair(arrayOfDates, arrayOfValues);
	}
}
