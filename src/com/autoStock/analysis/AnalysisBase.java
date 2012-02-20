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
	public float[] valuesPriceOpen;
	public float[] valuesPriceHigh;
	public float[] valuesPriceLow;
	public float[] valuesPriceClose;
	
	public AnalysisBase(){
		
	}
	
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
		if (returnCode == RetCode.Success){
			//pass
		}else{
			Co.println("Analysis result was not success: " + returnCode.name());
		}
	}
	
	public void preceedDataSetWithPeriod(){
		float[] tempValuesPriceOpen = new float[datasetLength+periodLength];
		float[] tempValuesPriceHigh = new float[datasetLength+periodLength];
		float[] tempValuesPriceLow = new float[datasetLength+periodLength];
		float[] tempValuesPriceClose = new float[datasetLength+periodLength];
		
		//Co.println("periodLength: " + periodLength + "," + datasetLength);
		
		for (int i=0; i<=periodLength; i++){
			tempValuesPriceHigh[i] = valuesPriceOpen[new Random().nextInt(periodLength)];
			tempValuesPriceHigh[i] = valuesPriceHigh[new Random().nextInt(periodLength)];
			tempValuesPriceLow[i] = valuesPriceLow[new Random().nextInt(periodLength)];
			tempValuesPriceClose[i] = valuesPriceClose[new Random().nextInt(periodLength)];
		}
		
		for (int i=periodLength; i<datasetLength+periodLength; i++){
			tempValuesPriceOpen[i] = valuesPriceOpen[i-periodLength];
			tempValuesPriceHigh[i] = valuesPriceHigh[i-periodLength];
			tempValuesPriceLow[i] = valuesPriceLow[i-periodLength];
			tempValuesPriceClose[i] = valuesPriceClose[i-periodLength];
		}
		
		valuesPriceOpen = tempValuesPriceOpen;
		valuesPriceHigh = tempValuesPriceHigh;
		valuesPriceLow = tempValuesPriceLow;
		valuesPriceClose = tempValuesPriceClose;
	}
}
