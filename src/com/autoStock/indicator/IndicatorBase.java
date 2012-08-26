/**
 * 
 */
package com.autoStock.indicator;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.DataExtractor;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class IndicatorBase {
	private Core taLibCore = new Core();
	public Object dataSource;
	public Type dataSourceType;
	public DataExtractor dataExtractor;
	public CommonAnlaysisData commonAnlaysisData;
	
	public int periodLength;
	public int datasetLength;
	public double[] arrayOfPriceOpen;
	public double[] arrayOfPriceHigh;
	public double[] arrayOfPriceLow;
	public double[] arrayOfPriceClose;
	public int[] arrayOfSizeVolume;
	public int endIndex;
	
	public IndicatorBase(int periodLength, CommonAnlaysisData commonAnlaysisData){
		this.periodLength = periodLength;
		this.commonAnlaysisData = commonAnlaysisData;
	}
	
	public Core getTaLibCore(){
		return this.taLibCore;
	}
	
	public void setDataSet(){
		
	}
	
	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice){
		if (listOfQuoteSlice.size() == 0 || listOfQuoteSlice.size() < periodLength){
			throw new IllegalArgumentException("List size was too small: " + listOfQuoteSlice.size());
		}
		
		this.dataSource = listOfQuoteSlice;
		this.datasetLength = listOfQuoteSlice.size();
		this.dataSourceType = listOfQuoteSlice.getClass();
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
		
		this.dataSource = listOfDbStockHistoricalPrice;
		this.datasetLength = listOfDbStockHistoricalPrice.size();
		this.dataSourceType = listOfDbStockHistoricalPrice.getClass();
		this.endIndex = datasetLength -1;
	}
	
	public void handleAnalysisResult(RetCode returnCode){
		if (returnCode != RetCode.Success){
			Co.println("Analysis result was not success: " + returnCode.name() + ", " + this.getClass().getSimpleName());
		}
	}
}
