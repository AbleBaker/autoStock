package com.autoStock.signal;

import java.util.ArrayList;

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
	public final SignalOfCCI signalOfCCI = new SignalOfCCI();
	public final SignalOfADX signalOfADX = new SignalOfADX();
	public SignalOfDI signalOfDI = new SignalOfDI();
	public SignalOfMACD signalOfMACD = new SignalOfMACD();
	public SignalOfRSI signalOfRSI = new SignalOfRSI();
	public SignalOfTRIX signalOfTRIX = new SignalOfTRIX();
	public SignalOfROC signalOfROC = new SignalOfROC();
	public SignalOfMFI signalOfMFI = new SignalOfMFI();
	public SignalOfWILLR signalOfWILLR = new SignalOfWILLR();
	public SignalOfCandlestickGroup signalOfCandlestickGroup = new SignalOfCandlestickGroup();
	
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	
	public SignalGroup(IndicatorGroup indicatorGroup){
		this.indicatorGroup = indicatorGroup;
	}
	
	public void generateSignals(CommonAnlaysisData commonAnlaysisData, int periodLength){
		if (indicatorGroup.resultsCCI != null){signalOfCCI.addInput(indicatorGroup.resultsCCI.arrayOfCCI[0]);}
		if (indicatorGroup.resultsADX != null){signalOfADX.addInput(indicatorGroup.resultsADX.arrayOfADX[0]);}
		if (indicatorGroup.resultsDI != null){signalOfDI.addInput(ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIPlus, 0, 1), ArrayTools.subArray(indicatorGroup.resultsDI.arrayOfDIMinus, 0, 1));}
		if (indicatorGroup.resultsMACD != null){signalOfMACD.addInput(indicatorGroup.resultsMACD.arrayOfMACDHistogram[0]);}
		if (indicatorGroup.resultsRSI != null){signalOfRSI.addInput(indicatorGroup.resultsRSI.arrayOfRSI[0]);}
		if (indicatorGroup.resultsTRIX != null){signalOfTRIX.addInput(indicatorGroup.resultsTRIX.arrayOfTRIX[0]);}
		if (indicatorGroup.resultsROC != null){signalOfROC.addInput(indicatorGroup.resultsROC.arrayOfROC[0]);}
		if (indicatorGroup.resultsMFI != null){signalOfMFI.addInput(indicatorGroup.resultsMFI.arrayOfMFI[0]);}
		if (indicatorGroup.resultsWILLR != null){signalOfWILLR.addInput(indicatorGroup.resultsWILLR.arrayOfWILLR[0]);}
		if (indicatorGroup.candleStickIdentifierResult != null){ } //signalOfCandlestickGroup.addInput(indicatorGroup.candleStickIdentifierResult.getLastValue());}
		
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

	public SignalBase getSignalBaseForType(SignalMetricType signalMetricType) {
		for (SignalBase signalBase : listOfSignalBase){
//			Co.println("--> Signal metric: " + signalBase.signalMetricType.name());
			if (signalBase != null && signalBase.signalMetricType == signalMetricType){
				return signalBase;
			}
		}
		
		
		return null;
//		throw new IllegalArgumentException("No SignalMetricType matched: " + signalMetricType.name() + ", " + listOfSignalBase.size());
	}

	public void prune(int toLength) {
		for (SignalBase signalBase : listOfSignalBase){
			signalBase.prune(toLength);
		}
	}
}
