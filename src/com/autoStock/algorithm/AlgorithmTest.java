/**
 * 
 */
package com.autoStock.algorithm;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.analysis.AnalysisCCI;
import com.autoStock.analysis.results.ResultsCCI;
import com.autoStock.balance.BasicBalance;
import com.autoStock.tools.DateTools;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	
	private AnalysisCCI analysisOfCCI = new AnalysisCCI();
	private ArrayList<TypeQuoteSlice> listOfQuoteSlice = new ArrayList<TypeQuoteSlice>();
	private boolean havePosition = false;
	
	public void run(){
		Co.println("Algorithm is running...");
	}
	
	public ReceiverOfQuoteSlice getReceiver(){
		return this;
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice typeQuoteSlice) {
		//Co.println("Received backtest quote: " + DateTools.getPrettyDate(typeQuoteSlice.dateTime) + ", " + typeQuoteSlice.priceClose);
		
		listOfQuoteSlice.add(typeQuoteSlice);
	
		if (listOfQuoteSlice.size() > 30+1){
			analysisOfCCI.setDataSet(listOfQuoteSlice);
			ResultsCCI resultsCommodityChannelIndex = analysisOfCCI.analyize(false);
			float analysisResult = (float) resultsCommodityChannelIndex.arrayOfCCI[listOfQuoteSlice.size()-32];
			float analysisPrice = (float) resultsCommodityChannelIndex.arrayOfPrice[listOfQuoteSlice.size()-32];
			Co.println("Analyized: " + typeQuoteSlice.priceClose + ", " + analysisResult);
			if (analysisResult > 50 && havePosition == false){
				BasicBalance.buy(analysisPrice);
				havePosition = true;
			}else if (analysisResult < -200 && havePosition == true){
				BasicBalance.sell(analysisPrice);
				havePosition = false;
			}
		}else{
			Co.println("Waiting for more data (period condition)... ");
		}
	}
}
