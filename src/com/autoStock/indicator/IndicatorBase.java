/**
 * 
 */
package com.autoStock.indicator;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.Co;
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
	public CommonAnalysisData commonAnlaysisData;
	
	public final ImmutableInteger periodLength;
	public final int resultsetLength;
	private int requiredInputLength;
	public int datasetLength;
	public double[] arrayOfPriceOpen;
	public double[] arrayOfPriceHigh;
	public double[] arrayOfPriceLow;
	public double[] arrayOfPriceClose;
	public int[] arrayOfSizeVolume;
	public int endIndex = 0;
	
	public IndicatorBase(ImmutableInteger periodLength, int resultsetLength, CommonAnalysisData commonAnlaysisData, Core taLibCore){
		this.periodLength = periodLength;
		this.commonAnlaysisData = commonAnlaysisData;
		this.taLibCore = taLibCore;
		this.resultsetLength = resultsetLength;
		
		requiredInputLength = periodLength.value + resultsetLength -1;
	}

	public void setDataSet(){
		if (commonAnlaysisData.arrayOfDates.length < periodLength.value){
			throw new IllegalArgumentException("List size was too small: " + commonAnlaysisData.arrayOfDates.length + ", expected: " + periodLength.value);
		}
		
		if (periodLength.value == 0){
			return;
		}
		
		int initialLength = commonAnlaysisData.arrayOfDates.length;
		
		if (requiredInputLength > initialLength){
			throw new IllegalArgumentException("Input length is smaller than required length (needed, supplied): " + requiredInputLength + ", " + initialLength);
		}
	
		if (initialLength != requiredInputLength){
			arrayOfPriceOpen = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceOpen, initialLength - requiredInputLength, initialLength);
			arrayOfPriceHigh = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceHigh, initialLength -requiredInputLength, initialLength);
			arrayOfPriceLow = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceLow, initialLength - requiredInputLength, initialLength);
			arrayOfPriceClose = Arrays.copyOfRange(commonAnlaysisData.arrayOfPriceClose, initialLength - requiredInputLength, initialLength);
			arrayOfSizeVolume = Arrays.copyOfRange(commonAnlaysisData.arrayOfSizeVolume, initialLength - requiredInputLength, initialLength);
		}else{
			arrayOfPriceOpen = commonAnlaysisData.arrayOfPriceOpen;
			arrayOfPriceHigh = commonAnlaysisData.arrayOfPriceHigh;
			arrayOfPriceLow = commonAnlaysisData.arrayOfPriceLow;
			arrayOfPriceClose = commonAnlaysisData.arrayOfPriceClose;
			arrayOfSizeVolume = commonAnlaysisData.arrayOfSizeVolume;
		}
		
		datasetLength = arrayOfPriceClose.length;
		endIndex = datasetLength-1;
		
//		Co.println("--> this: " + this.getClass().getName() + ", " + resultsetLength);
	}
	
	public int getRequiredDatasetLength(){
		return requiredInputLength;
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
