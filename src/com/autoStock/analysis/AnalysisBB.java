/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DataExtractor;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisBB extends AnalysisBase {
	public ResultsBollingerBands results;
	
	public AnalysisBB(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsBollingerBands analyize(MAType manalysisType){	
		results = new ResultsBollingerBands(((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		
		float[] values = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		RetCode returnCode = getTaLibCore().bbands(0, values.length-1, values, 5, 2, 2, manalysisType, new MInteger(), new MInteger(), results.arrayOfUpperBand, results.arrayOfMiddleBand, results.arrayOfLowerBand);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
