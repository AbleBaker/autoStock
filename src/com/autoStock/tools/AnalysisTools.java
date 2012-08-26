package com.autoStock.tools;

import java.util.ArrayList;

import com.autoStock.indicator.IndicatorBase;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisTools {
	public static synchronized void setIndicatorPeriodLength(int periodLength, IndicatorBase... arrayOfAnalysisBase){
		for (IndicatorBase analysis : arrayOfAnalysisBase){
			analysis.periodLength = periodLength;
		}
	}
	
	public static synchronized void setIndicatorPeriodLength(int periodLength, ArrayList<IndicatorBase> listOfIndicatorBase){
		for (IndicatorBase analysis : listOfIndicatorBase){
			analysis.periodLength = periodLength;
		}
	}
	
}
