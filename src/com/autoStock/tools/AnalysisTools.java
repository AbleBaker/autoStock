package com.autoStock.tools;

import com.autoStock.analysis.AnalysisBase;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisTools {
	public static synchronized void setAnalysisPeriodLength(int periodLength, AnalysisBase... arrayOfAnalysisBase){
		for (AnalysisBase analysis : arrayOfAnalysisBase){
			analysis.periodLength = periodLength;
		}
	}
}
