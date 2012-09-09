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
	public SignalOfROC signalOfROC;
	public SignalOfMFI signalOfMFI;
	public SignalOfWILLR signalOfWILLR;
	
	public void generateSignals(CommonAnlaysisData commonAnlaysisData, IndicatorGroup indicatorGroup, int periodLength){
		signalOfPPC = new SignalOfPPC(ArrayTools.subArray(commonAnlaysisData.arrayOfPriceClose, 0, periodLength-1), SignalControl.periodAverageForPPC);
		signalOfDI = new SignalOfDI(ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIMinus, 0, 1), SignalControl.periodAverageForDI);
		signalOfCCI = new SignalOfCCI(ArrayTools.subArray(indicatorGroup.resultsCCI.arrayOfCCI, 0, 1), SignalControl.periodAverageForCCI);
		signalOfMACD = new SignalOfMACD(ArrayTools.subArray(indicatorGroup.resultsMACD.arrayOfMACDHistogram, 0, 1), SignalControl.periodAverageForMACD);
		signalOfRSI = new SignalOfRSI(ArrayTools.subArray(indicatorGroup.resultsRSI.arrayOfRSI, 0, 1), SignalControl.periodAverageForRSI);
		signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(indicatorGroup.resultsTRIX.arrayOfTRIX, 0, 1), SignalControl.periodAverageForTRIX);
		signalOfROC = new SignalOfROC(ArrayTools.subArray(indicatorGroup.resultsROC.arrayOfROC, 0, 1), 0);
		signalOfMFI = new SignalOfMFI(ArrayTools.subArray(indicatorGroup.resultsMFI.arrayOfMFI, 0, 1), 0);
		signalOfWILLR = new SignalOfWILLR(ArrayTools.subArray(indicatorGroup.resultsWILLR.arrayOfWILLR, 0, 1), 0);
	}
}
