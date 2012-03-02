/**
 * 
 */
package com.autoStock.algorithm;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.analysis.AnalysisCCI;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.tools.DateTools;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	
	AnalysisCCI analysisOfCCI = new AnalysisCCI();
	//analysisOfCCI.setDataSet(listOfResults);
	ResultsCCI resultsCommodityChannelIndex = analysisOfCCI.analyize();
	
	public void run(){
		
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return this;
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice typeQuoteSlice) {
		Co.println("Received backtest quote: " + DateTools.getPrettyDate(typeQuoteSlice.dateTime) + ", " + typeQuoteSlice.priceClose);
	}
}
