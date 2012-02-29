/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Random;

import com.autoStock.Co;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DataConditioner;
import com.autoStock.tools.DataExtractor;
import com.autoStock.tools.DataConditioner.PrecededDataset;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class AnalysisBase {
	private Core taLibCore = new Core();
	public Object dataSource;
	public DataExtractor dataExtractor;
	
	public int periodLength;
	public int datasetLength;
	public float[] arrayOfPriceOpen;
	public float[] arrayOfPriceHigh;
	public float[] arrayOfPriceLow;
	public float[] arrayOfPriceClose;
	
	public void initializeTypicalAnalys(int periodLength, int datasetLength) {
		this.periodLength = periodLength;
		this.datasetLength = datasetLength;
	}
	
	public Core getTaLibCore(){
		return this.taLibCore;
	}
	
	public void setDataSet(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalStateException();
		}
		
		this.dataSource = listOfDbStockHistoricalPrice;
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
