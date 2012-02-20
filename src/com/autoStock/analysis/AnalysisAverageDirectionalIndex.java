/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsAverageDirectionalIndex;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisAverageDirectionalIndex extends AnalysisBase {
	public ResultsAverageDirectionalIndex results;
	
	public ResultsAverageDirectionalIndex analize(){
		super.initializeTypicalAnalys(64, ((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		
		results = new ResultsAverageDirectionalIndex(datasetLength+periodLength);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		valuesPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		valuesPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		valuesPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceLow").toArray(new Float[0]));
		valuesPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		preceedDataSetWithPeriod();
		
		RetCode returnCode = getTaLibCore().adx(0,  datasetLength+periodLength-1, valuesPriceHigh, valuesPriceLow, valuesPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfADX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
