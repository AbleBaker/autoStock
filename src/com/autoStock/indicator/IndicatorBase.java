/**
 * 
 */
package com.autoStock.indicator;

import java.util.ArrayList;

import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 * @param <T>
 *
 */
public abstract class IndicatorBase {
	public Core taLibCore;
	public CommonAnlaysisData commonAnlaysisData;
	
	public int periodLength;
	public int datasetLength;
	public double[] arrayOfPriceOpen;
	public double[] arrayOfPriceHigh;
	public double[] arrayOfPriceLow;
	public double[] arrayOfPriceClose;
	public int[] arrayOfSizeVolume;
	public int endIndex;
	
	public IndicatorBase(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore){
		this.periodLength = periodLength;
		this.commonAnlaysisData = commonAnlaysisData;
		this.taLibCore = taLibCore;
	}
	
	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice){
		if (listOfQuoteSlice.size() == 0 || listOfQuoteSlice.size() < periodLength){
			throw new IllegalArgumentException("List size was too small: " + listOfQuoteSlice.size());
		}
		
		this.datasetLength = listOfQuoteSlice.size();
		this.endIndex = datasetLength -1;
		
		arrayOfPriceOpen = commonAnlaysisData.arrayOfPriceOpen;
		arrayOfPriceHigh = commonAnlaysisData.arrayOfPriceHigh;
		arrayOfPriceLow = commonAnlaysisData.arrayOfPriceLow;
		arrayOfPriceClose = commonAnlaysisData.arrayOfPriceClose;
		arrayOfSizeVolume = commonAnlaysisData.arrayOfSizeVolume;
	}
	
	public void setDataSetFromDatabase(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalArgumentException();
		}
		
		this.datasetLength = listOfDbStockHistoricalPrice.size();
		this.endIndex = datasetLength -1;
	}
	
	public void handleAnalysisResult(RetCode returnCode){
		if (returnCode != RetCode.Success){
//			throw new IllegalStateException("Result code was not success...");
		}
	}
}
