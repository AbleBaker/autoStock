/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;
import java.util.Random;

import com.autoStock.Co;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
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
	public float[] areayOfPriceClose;
	
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
}
