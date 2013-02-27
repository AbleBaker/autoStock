package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.indicator.CommonAnlaysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGroup {
	public IndicatorGroup indicatorGroup;
	public SignalOfCCI signalOfCCI = new SignalOfCCI();
	public SignalOfADX signalOfADX;
	public SignalOfDI signalOfDI;
	public SignalOfMACD signalOfMACD;
	public SignalOfRSI signalOfRSI;
	public SignalOfTRIX signalOfTRIX;
	public SignalOfROC signalOfROC;
	public SignalOfMFI signalOfMFI;
	public SignalOfWILLR signalOfWILLR;
	public SignalOfCandlestickGroup signalOfCandlestickGroup;
	
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	
	public SignalGroup(IndicatorGroup indicatorGroup){
		this.indicatorGroup = indicatorGroup;
	}
	
	public void generateSignals(CommonAnlaysisData commonAnlaysisData, int periodLength){
		if (indicatorGroup.resultsCCI != null){signalOfCCI.addInput(indicatorGroup.resultsCCI.arrayOfCCI[0]);}
		if (indicatorGroup.resultsADX != null){signalOfADX = new SignalOfADX(ArrayTools.subArray(indicatorGroup.resultsADX.arrayOfADX, 0, 1));}
		if (indicatorGroup.resultsDI != null){signalOfDI = new SignalOfDI(ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIMinus, 0, 1));}
		if (indicatorGroup.resultsMACD != null){signalOfMACD = new SignalOfMACD(ArrayTools.subArray(indicatorGroup.resultsMACD.arrayOfMACDHistogram, 0, 1));}
		if (indicatorGroup.resultsRSI != null){signalOfRSI = new SignalOfRSI(ArrayTools.subArray(indicatorGroup.resultsRSI.arrayOfRSI, 0, 1));}
		if (indicatorGroup.resultsTRIX != null){signalOfTRIX = new SignalOfTRIX(ArrayTools.subArray(indicatorGroup.resultsTRIX.arrayOfTRIX, 0, 1));}
		if (indicatorGroup.resultsROC != null){signalOfROC = new SignalOfROC(ArrayTools.subArray(indicatorGroup.resultsROC.arrayOfROC, 0, 1));}
		if (indicatorGroup.resultsMFI != null){signalOfMFI = new SignalOfMFI(ArrayTools.subArray(indicatorGroup.resultsMFI.arrayOfMFI, 0, 1));}
		if (indicatorGroup.resultsWILLR != null){signalOfWILLR = new SignalOfWILLR(ArrayTools.subArray(indicatorGroup.resultsWILLR.arrayOfWILLR, 0, 1));}
		if (indicatorGroup.candleStickIdentifierResult != null){signalOfCandlestickGroup = new SignalOfCandlestickGroup();}
		
		listOfSignalBase.clear();
		listOfSignalBase.add(signalOfCCI);
		listOfSignalBase.add(signalOfADX);
		listOfSignalBase.add(signalOfDI);
		listOfSignalBase.add(signalOfMACD);
		listOfSignalBase.add(signalOfRSI);
		listOfSignalBase.add(signalOfTRIX);
		listOfSignalBase.add(signalOfROC);
		listOfSignalBase.add(signalOfMFI);
		listOfSignalBase.add(signalOfWILLR);
		listOfSignalBase.add(signalOfCandlestickGroup);
	}
	
	public IndicatorGroup getIndicatorGroup(){
		return indicatorGroup;
	}

	public SignalMetric getSignalMetricForType(SignalMetricType signalMetricType) {
		for (SignalBase signalBase : listOfSignalBase){
//			Co.println("--> Signal metric: " + signalBase.signalMetricType.name());
			if (signalBase != null && signalBase.signalMetricType == signalMetricType){
				return signalBase.getSignal();
			}
		}
		
		
		return null;
//		throw new IllegalArgumentException("No SignalMetricType matched: " + signalMetricType.name() + ", " + listOfSignalBase.size());
	}
}
