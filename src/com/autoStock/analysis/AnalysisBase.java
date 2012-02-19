/**
 * 
 */
package com.autoStock.analysis;

import java.util.ArrayList;

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
	
	public AnalysisBase() {
		
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
}
