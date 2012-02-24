/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.analysis.tools.DataConditioner;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisCCI extends AnalysisBase {
	public ResultsCCI results;
	
	public ResultsCCI analyize(){
		super.initializeTypicalAnalys(128, ((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		
		results = new ResultsCCI(datasetLength+periodLength);
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));

		arrayOfPriceOpen = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceOpen").toArray(new Float[0]));
		arrayOfPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		arrayOfPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceLow").toArray(new Float[0]));
		areayOfPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
	
		new DataConditioner().preceedDatasetWithPeriod(arrayOfPriceOpen, arrayOfPriceHigh, arrayOfPriceLow, areayOfPriceClose, periodLength, datasetLength);
		
		RetCode returnCode = getTaLibCore().cci(periodLength+1, periodLength+datasetLength-1, arrayOfPriceHigh, arrayOfPriceLow, areayOfPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
