/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.analysis.results.ResultsBollingerBands;
import com.autoStock.analysis.results.ResultsCommodityChannelIndex;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public class AnalysisCommodityChannelIndex extends AnalysisBase {
	
	public ResultsCommodityChannelIndex results;
	private int periodLength = 32; 
	
	public ResultsCommodityChannelIndex analyize(){
		
		results = new ResultsCommodityChannelIndex(((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		float[] valuesPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		float[] valuesPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceLow").toArray(new Float[0]));
		float[] valuesPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
	
		RetCode returnCode = getTaLibCore().cci(periodLength, valuesPriceHigh.length-1, valuesPriceHigh, valuesPriceLow, valuesPriceClose, periodLength, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		float[] arrayOfTemp = new float[results.arrayOfPrice.length-periodLength];
		
		for (int i=periodLength; i<results.arrayOfPrice.length; i++){
			arrayOfTemp[i-periodLength] = results.arrayOfPrice[i];
		}
		
		results.arrayOfPrice = arrayOfTemp;
		
		return results;
	}
}
