package com.autoStock.signal;

import com.autoStock.indicator.CommonAnlaysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGroup {
	public SignalOfPPC signalOfPPC;
	public SignalOfDI signalOfDI;
	public SignalOfCCI signalOfCCI;
	public SignalOfMACD signalOfMACD;
	public SignalOfRSI signalOfRSI;
	public SignalOfTRIX signalOfTRIX;
	
	public void generateSignals(CommonAnlaysisData commonAnlaysisData, IndicatorGroup indicatorGroup, int periodLength){
		signalOfPPC = new SignalOfPPC(ArrayTools.subArray(commonAnlaysisData.arrayOfPriceClose, 0, periodLength-1), SignalControl.periodAverageForPPC);
		signalOfDI = new SignalOfDI(ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIMinus, 0, 1), SignalControl.periodAverageForDI);
		signalOfCCI = new SignalOfCCI(ArrayTools.subArray(indicatorGroup.resultsCCI.arrayOfCCI, 0, 1), SignalControl.periodAverageForCCI);
		signalOfMACD = new SignalOfMACD(ArrayTools.subArray(indicatorGroup.resultsMACD.arrayOfMACDHistogram, 0, 1), SignalControl.periodAverageForMACD);
		signalOfRSI = new SignalOfRSI(ArrayTools.subArray(indicatorGroup.resultsRSI.arrayOfRSI, 0, 1), SignalControl.periodAverageForRSI);
		signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(indicatorGroup.resultsTRIX.arrayOfTRIX, 0, 1), SignalControl.periodAverageForTRIX);
	}
}
