/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsBB;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisOfBB extends AnalysisBase {
	public ResultsBB results;
	public int optionDeviationUp = 8;
	public int optionDeviationDown = 8;
	
	public AnalysisOfBB(int periodLength, boolean preceedDataset) {
		super(periodLength, preceedDataset);
	}
	
	public ResultsBB analyize(MAType manalysisType){	
		results = new ResultsBB(endIndex+1);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		
		//arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceOpen").toArray(new Double[0]));
		//arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceHigh").toArray(new Double[0]));
		//arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<TypeQuoteSlice>)super.dataSource), "priceLow").toArray(new Double[0]));
		arrayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractDouble(((ArrayList<QuoteSlice>)super.dataSource), "priceClose").toArray(new Double[0]));
		
		if (preceedDataset){
			preceedDatasetWithPeriod();
		}
		
		RetCode returnCode = getTaLibCore().bbands(0, endIndex, arrayOfPriceClose,periodLength, optionDeviationUp, optionDeviationDown, manalysisType, new MInteger(), new MInteger(), results.arrayOfUpperBand, results.arrayOfMiddleBand, results.arrayOfLowerBand);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
