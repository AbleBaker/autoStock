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
	
	public ResultsCommodityChannelIndex analyize(){
		
		results = new ResultsCommodityChannelIndex(((ArrayList<DbStockHistoricalPrice>)super.dataSource).size());
		results.arrayOfDates =  new DataExtractor().extractDate(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "dateTime").toArray(new Date[0]);
		results.arrayOfPrice =  new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
		
		float[] valuesPriceHigh = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceHigh").toArray(new Float[0]));
		float[] valuesPriceLow = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceLow").toArray(new Float[0]));
		float[] valuesPriceClose = new ArrayUtils().toPrimitive(new DataExtractor().extractFloat(((ArrayList<DbStockHistoricalPrice>)super.dataSource), "priceClose").toArray(new Float[0]));
	
		RetCode returnCode = getTaLibCore().cci(32, valuesPriceHigh.length-1, valuesPriceHigh, valuesPriceLow, valuesPriceClose, 32, new MInteger(), new MInteger(), results.arrayOfCCI);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
