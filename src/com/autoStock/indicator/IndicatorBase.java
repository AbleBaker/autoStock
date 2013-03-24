/**
 * 
 */
package com.autoStock.indicator;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.RetCode;
import com.autoStock.tools.ArrayTools;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.basic.ImmutableInteger;
import com.sun.org.apache.bcel.internal.generic.IMUL;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class IndicatorBase {
	public Core taLibCore;
	public CommonAnlaysisData commonAnlaysisData;
	
	public final ImmutableInteger periodLength;
	public int datasetLength;
	public double[] arrayOfPriceOpen;
	public double[] arrayOfPriceHigh;
	public double[] arrayOfPriceLow;
	public double[] arrayOfPriceClose;
	public int[] arrayOfSizeVolume;
	public int endIndex = 0;
	
	public IndicatorBase(ImmutableInteger periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore){
		this.periodLength = periodLength;
		this.commonAnlaysisData = commonAnlaysisData;
		this.taLibCore = taLibCore;
	}

	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice){
		if (listOfQuoteSlice.size() == 0 || listOfQuoteSlice.size() < periodLength.value){
			throw new IllegalArgumentException("List size was too small: " + listOfQuoteSlice.size() + ", expected: " + periodLength.value);
		}
		
		this.datasetLength = listOfQuoteSlice.size();
		this.endIndex = periodLength.value -1;
	
		if (periodLength.value != datasetLength){
			arrayOfPriceOpen = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceOpen, datasetLength - periodLength.value, datasetLength);
			arrayOfPriceHigh = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceHigh, datasetLength - periodLength.value, datasetLength);
			arrayOfPriceLow = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceLow, datasetLength - periodLength.value, datasetLength);
			arrayOfPriceClose = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceClose, datasetLength - periodLength.value, datasetLength);
			arrayOfSizeVolume = Arrays.copyOfRange(commonAnlaysisData.arrayOfSizeVolume, datasetLength - periodLength.value, datasetLength);
		}else{
			arrayOfPriceOpen = commonAnlaysisData.arrayOfPriceOpen;
			arrayOfPriceHigh = commonAnlaysisData.arrayOfPriceHigh;
			arrayOfPriceLow = commonAnlaysisData.arrayOfPriceLow;
			arrayOfPriceClose = commonAnlaysisData.arrayOfPriceClose;
			arrayOfSizeVolume = commonAnlaysisData.arrayOfSizeVolume;
		}
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
