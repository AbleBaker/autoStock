/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
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
		
		results = new ResultsBollingerBands(((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		
		float[] values = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		Co.println("Length: " + values.length + "," + ((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		
		RetCode returnCode = getTaLibCore().bbands(0, values.length-1, values, 5, 5, 5, manalysisType, new MInteger(), new MInteger(), results.arrayOfUpperBand, results.arrayOfMiddleBand, results.arrayOfLowerBand);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
