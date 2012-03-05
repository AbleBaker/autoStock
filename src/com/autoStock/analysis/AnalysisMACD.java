/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsADX;
import com.autoStock.analysis.results.ResultsMACD;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DataConditioner;
import com.autoStock.tools.DataExtractor;
import com.autoStock.tools.DataConditioner.PrecededDataset;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisMACD extends Analysis {
	public ResultsMACD results;
	
	public AnalysisMACD(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(datasetLength+periodLength);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<TypeQuoteSlice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Float[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		//RetCode returnCode = getTaLibCore().macdExt(0, datasetLength+periodLength-1, valuesPriceClose, periodLength, MAType.Wma, periodLength, MAType.Wma, periodLength, MAType.Wma, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACD, results.arrayOfMACDHistogram);
		RetCode returnCode = getTaLibCore().macd(0, endIndex, arrayOfPriceClose, 32, 8, 32, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
