/**
 * 
 */
package com.autoStock.tools;

import java.util.Random;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class DataConditioner {
	//TODO: Implement me from AnalysisBase
	
	public PrecededDataset preceedDatasetWithPeriod(float[] arrayOfPriceOpen, float[] arrayOfPriceHigh, float[] arrayOfPriceLow, float[] arrayOfPriceClose, int periodLength, int datasetLength){
		float[] tempValuesPriceOpen = new float[datasetLength+periodLength];
		float[] tempValuesPriceHigh = new float[datasetLength+periodLength];
		float[] tempValuesPriceLow = new float[datasetLength+periodLength];
		float[] tempValuesPriceClose = new float[datasetLength+periodLength];
		
		Co.println("periodLength: " + periodLength + "," + datasetLength);
		
		for (int i=0; i<=periodLength; i++){
			int preceedWith = new Random().nextInt(periodLength);
			tempValuesPriceHigh[i] = arrayOfPriceOpen[preceedWith];
			tempValuesPriceHigh[i] = arrayOfPriceOpen[preceedWith];
			tempValuesPriceLow[i] = arrayOfPriceOpen[preceedWith];
			tempValuesPriceClose[i] = arrayOfPriceOpen[preceedWith];
		}
		
		for (int i=periodLength; i<datasetLength+periodLength; i++){
			tempValuesPriceOpen[i] = arrayOfPriceOpen[i-periodLength];
			tempValuesPriceHigh[i] = arrayOfPriceHigh[i-periodLength];
			tempValuesPriceLow[i] = arrayOfPriceLow[i-periodLength];
			tempValuesPriceClose[i] = arrayOfPriceClose[i-periodLength];
		}
		
		return new PrecededDataset(tempValuesPriceOpen, tempValuesPriceHigh, tempValuesPriceLow, tempValuesPriceClose);
	}
	
	public class PrecededDataset{
		public float[] arrayOfPriceOpen;
		public float[] arrayOfPriceHigh;
		public float[] arrayOfPriceLow;
		public float[] arrayOfPriceClose;
		
		public PrecededDataset(float[] arrayOfPriceOpen, float[] arrayOfPriceHigh, float[] arrayOfPriceLow, float[] arrayOfPriceClose){
			this.arrayOfPriceOpen = arrayOfPriceOpen;
			this.arrayOfPriceHigh = arrayOfPriceHigh;
			this.arrayOfPriceLow = arrayOfPriceClose;
			this.arrayOfPriceClose = arrayOfPriceClose;
		}
	}
}
