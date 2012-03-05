/**
 * 
 */
package com.autoStock.analysis;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import com.autoStock.Co;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DataConditioner;
import com.autoStock.tools.DataExtractor;
import com.autoStock.tools.DataConditioner.PrecededDataset;
import com.autoStock.types.TypeQuoteSlice;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class Analysis {
	private Core taLibCore = new Core();
	public Object dataSource;
	public Type dateSourceType;
	public DataExtractor dataExtractor;
	
	public int periodLength;
	public int datasetLength;
	public float[] arrayOfPriceOpen;
	public float[] arrayOfPriceHigh;
	public float[] arrayOfPriceLow;
	public float[] arrayOfPriceClose;
	public boolean preceedDataset;
	public int endIndex;
	
	public Analysis(int periodLength, boolean preceedDataset){
		this.periodLength = periodLength;
		this.preceedDataset = preceedDataset;
	}
	
	public Core getTaLibCore(){
		return this.taLibCore;
	}
	
	public void setDataSet(ArrayList<TypeQuoteSlice> listOfQuoteSlice){
		if (listOfQuoteSlice.size() == 0 || listOfQuoteSlice.size() < periodLength){
			throw new IllegalArgumentException();
		}
		
		this.dataSource = listOfQuoteSlice;
		this.datasetLength = listOfQuoteSlice.size();
		this.dateSourceType = listOfQuoteSlice.getClass();
		this.endIndex = preceedDataset ? (periodLength + datasetLength -1) : datasetLength -1;
	}
	
	public void setDataSetFromDatabase(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalArgumentException();
		}
		
		this.dataSource = listOfDbStockHistoricalPrice;
		this.dateSourceType = listOfDbStockHistoricalPrice.getClass();
	}
	
	public void handleAnalysisResult(RetCode returnCode){
		if (returnCode != RetCode.Success){
			Co.println("Analysis result was not success: " + returnCode.name());
		}
	}
	
	public void preceedDatasetWithPeriod(){
		PrecededDataset dataSet = new DataConditioner().preceedDatasetWithPeriod(arrayOfPriceOpen, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength, datasetLength);
		arrayOfPriceOpen = dataSet.arrayOfPriceOpen;
		arrayOfPriceHigh = dataSet.arrayOfPriceHigh;
		arrayOfPriceLow = dataSet.arrayOfPriceLow;
		arrayOfPriceClose = dataSet.arrayOfPriceClose;
	}
}
