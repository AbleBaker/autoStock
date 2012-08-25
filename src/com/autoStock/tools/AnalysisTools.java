package com.autoStock.tools;

import com.autoStock.analysis.IndicatorBase;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisTools {
	public static synchronized void setAnalysisPeriodLength(int periodLength, IndicatorBase... arrayOfAnalysisBase){
		for (IndicatorBase analysis : arrayOfAnalysisBase){
			analysis.periodLength = periodLength;
		}
	}
}
