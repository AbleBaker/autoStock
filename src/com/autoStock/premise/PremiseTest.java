/**
 * 
 */
package com.autoStock.premise;

import com.autoStock.Co;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 *
 */
public class PremiseTest {
	private PremiseController premiseController = new PremiseController();
	public void run(){
		Co.println("--> Premise test");
		
		premiseController.addPremise(new PremiseOfOHLC(new Exchange("NYSE"), new Symbol("MS"), DateTools.getDateFromString("03/09/2012"), Resolution.min_15, 3));
		premiseController.addPremise(new PremiseOfOHLC(new Exchange("NYSE"), new Symbol("MS"), DateTools.getDateFromString("03/09/2012"), Resolution.day, 5));
		
		premiseController.determinePremise();
		
		for (EncogFrame encogFrame : premiseController.getEncogFrames()){
			Co.println("--> Have encog frame: " + encogFrame.description);
		}
	}
}
