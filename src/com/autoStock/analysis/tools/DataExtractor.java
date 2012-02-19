/**
 * 
 */
package com.autoStock.analysis.tools;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DataExtractor {
	public static enum PriceExtractorMode{
		mode_average,
	}
	
	public double[] resultsOfDouble;
	public Date[] resultsOfDate;
	
	public void extractFromDbStockHistoricalPrice(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice, PriceExtractorMode extractorMode){
		this.resultsOfDouble = new double[listOfDbStockHistoricalPrice.size()];
		this.resultsOfDate = new Date[listOfDbStockHistoricalPrice.size()];
		
		int i = 0;
		for (DbStockHistoricalPrice dbStockHistoricalPrice : listOfDbStockHistoricalPrice){
			if (extractorMode == PriceExtractorMode.mode_average){
				this.resultsOfDouble[i] = (dbStockHistoricalPrice.priceOpen + dbStockHistoricalPrice.priceClose) / 2;
				this.resultsOfDate[i] = dbStockHistoricalPrice.dateTime;
				i++;
			}else{throw new UnsupportedOperationException();}
		}
	}
}
