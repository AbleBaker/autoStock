/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.analysis.tools.DataExtractor;
import com.autoStock.analysis.tools.DataExtractor.PriceExtractorMode;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.RetCode;

/**
 * @author Kevin Kowalewski
 *
 */
public abstract class AnalysisBase {
	private Core taLibCore = new Core();
	public Object source;
	public double[] values;
	public DataExtractor dataExtractor;
	
	public AnalysisBase() {
		
	}
	
	public Core getTaLibCore(){
		return this.taLibCore;
	}
	
	public void setDataSet(ArrayList<DbStockHistoricalPrice> listOfDbStockHistoricalPrice){
		if (listOfDbStockHistoricalPrice.size() == 0){
			throw new IllegalStateException();
		}
		
		this.dataExtractor = new DataExtractor();
		this.dataExtractor.extractFromDbStockHistoricalPrice(listOfDbStockHistoricalPrice, PriceExtractorMode.mode_average);
		
		this.source = listOfDbStockHistoricalPrice;
		this.values = dataExtractor.resultsOfDouble;
	}
	
	public void handleAnalysisResult(RetCode returnCode){
		if (returnCode == RetCode.Success){
			//pass
		}else{
			Co.println("Analysis result was not success: " + returnCode.name());
		}
	}
}
