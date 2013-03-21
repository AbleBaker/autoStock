/**
 * 
 */
package com.autoStock.indicator;

import java.util.ArrayList;

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
			throw new IllegalArgumentException("List size was too small: " + listOfQuoteSlice.size());
		}
		
		this.datasetLength = listOfQuoteSlice.size();
		this.endIndex = periodLength.value -1;
		
		arrayOfPriceOpen = ArrayTools.subArray(commonAnlaysisData.arrayOfPriceOpen, datasetLength - periodLength.value, datasetLength);
		arrayOfPriceHigh = ArrayTools.subArray(commonAnlaysisData.arrayOfPriceHigh, datasetLength - periodLength.value, datasetLength);
		arrayOfPriceLow = ArrayTools.subArray(commonAnlaysisData.arrayOfPriceLow, datasetLength - periodLength.value, datasetLength);
		arrayOfPriceClose = ArrayTools.subArray(commonAnlaysisData.arrayOfPriceClose, datasetLength - periodLength.value, datasetLength);
		arrayOfSizeVolume = ArrayTools.subArray(commonAnlaysisData.arrayOfSizeVolume, datasetLength - periodLength.value, datasetLength);
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
