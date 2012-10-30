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
		if (indicatorGroup.resultsDI != null){signalOfPPC = new SignalOfPPC(ArrayTools.subArray(commonAnlaysisData.arrayOfPriceClose, 0, periodLength-1), SignalControl.periodAverageForPPC);}
		if (indicatorGroup.resultsDI != null){signalOfDI = new SignalOfDI(ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIMinus, 0, 1));}
		if (indicatorGroup.resultsCCI != null){signalOfCCI = new SignalOfCCI(ArrayTools.subArray(indicatorGroup.resultsCCI.arrayOfCCI, 0, 1));}
		if (indicatorGroup.resultsMACD != null){signalOfMACD = new SignalOfMACD(ArrayTools.subArray(indicatorGroup.resultsMACD.arrayOfMACDHistogram, 0, 1));}
		if (indicatorGroup.resultsRSI != null){signalOfRSI = new SignalOfRSI(ArrayTools.subArray(indicatorGroup.resultsRSI.arrayOfRSI, 0, 1));}
		if (indicatorGroup.resultsTRIX != null){signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(indicatorGroup.resultsTRIX.arrayOfTRIX, 0, 1));}
		if (indicatorGroup.resultsROC != null){signalOfROC = new SignalOfROC(ArrayTools.subArray(indicatorGroup.resultsROC.arrayOfROC, 0, 1));}
		if (indicatorGroup.resultsMFI != null){signalOfMFI = new SignalOfMFI(ArrayTools.subArray(indicatorGroup.resultsMFI.arrayOfMFI, 0, 1));}
		if (indicatorGroup.resultsWILLR != null){signalOfWILLR = new SignalOfWILLR(ArrayTools.subArray(indicatorGroup.resultsWILLR.arrayOfWILLR, 0, 1));}
	}
}
