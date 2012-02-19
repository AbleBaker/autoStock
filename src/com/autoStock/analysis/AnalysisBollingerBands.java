/**
 * 
 */
package com.autoStock.analysis;

import com.autoStock.analysis.results.ResultsBollingerBands;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisBollingerBands extends AnalysisBase {
	
	public ResultsBollingerBands results;
	
	public ResultsBollingerBands analyize(MAType manalysisType){
		
		 results = new ResultsBollingerBands(super.values.length);
		
		RetCode returnCode = getTaLibCore().bbands(0, super.values.length-1, super.values, 2, 2, 2, manalysisType, new MInteger(), new MInteger(), results.arrayOfUpperBand, results.arrayOfMiddleBand, results.arrayOfLowerBand);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
