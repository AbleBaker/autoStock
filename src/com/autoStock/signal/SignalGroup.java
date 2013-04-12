package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalOfEncog.EncogInputWindow;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGroup {
	public IndicatorGroup indicatorGroup;
	public SignalOfCCI signalOfCCI = new SignalOfCCI();
	public SignalOfADX signalOfADX = new SignalOfADX();
	public SignalOfDI signalOfDI = new SignalOfDI();
	public SignalOfMACD signalOfMACD = new SignalOfMACD();
	public SignalOfRSI signalOfRSI = new SignalOfRSI();
	public SignalOfTRIX signalOfTRIX = new SignalOfTRIX();
	public SignalOfROC signalOfROC = new SignalOfROC();
	public SignalOfMFI signalOfMFI = new SignalOfMFI();
	public SignalOfWILLR signalOfWILLR = new SignalOfWILLR();
	public SignalOfUO signalOfUO = new SignalOfUO();
	public SignalOfARUp signalOfARUp = new SignalOfARUp();
	public SignalOfARDown signalOfARDown = new SignalOfARDown();
	public SignalOfCandlestickGroup signalOfCandlestickGroup = new SignalOfCandlestickGroup();
	public SignalOfEncog signalOfEncog = new SignalOfEncog();
	
	public static final int ENCOG_SIGNAL_INPUT = 5;
	
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	
	public SignalGroup(IndicatorGroup indicatorGroup){
		this.indicatorGroup = indicatorGroup;
	}
	
	public void generateSignals(CommonAnalysisData commonAnlaysisData, int periodLength){
		if (indicatorGroup.resultsCCI != null){signalOfCCI.setInput(indicatorGroup.resultsCCI.arrayOfCCI);}
		if (indicatorGroup.resultsADX != null){signalOfADX.setInput(indicatorGroup.resultsADX.arrayOfADX[0]);}
		if (indicatorGroup.resultsDI != null){signalOfDI.setInput(indicatorGroup.resultsDI.arrayOfDIPlus[0], indicatorGroup.resultsDI.arrayOfDIMinus[0]);}
		if (indicatorGroup.resultsMACD != null){signalOfMACD.setInput(indicatorGroup.resultsMACD.arrayOfMACDHistogram[0]);}
		if (indicatorGroup.resultsRSI != null){signalOfRSI.setInput(indicatorGroup.resultsRSI.arrayOfRSI);}
		if (indicatorGroup.resultsTRIX != null){signalOfTRIX.setInput(indicatorGroup.resultsTRIX.arrayOfTRIX[0]);}
		if (indicatorGroup.resultsROC != null){signalOfROC.setInput(indicatorGroup.resultsROC.arrayOfROC[0]);}
		if (indicatorGroup.resultsMFI != null){signalOfMFI.setInput(indicatorGroup.resultsMFI.arrayOfMFI[0]);}
		if (indicatorGroup.resultsWILLR != null){signalOfWILLR.setInput(indicatorGroup.resultsWILLR.arrayOfWILLR[0]);}
		if (indicatorGroup.resultsUO != null){signalOfUO.setInput(indicatorGroup.resultsUO.arrayOfUO);}
		if (indicatorGroup.resultsAR != null){signalOfARUp.setInput(indicatorGroup.resultsAR.arrayOfARUp);}
		if (indicatorGroup.resultsAR != null){signalOfARDown.setInput(indicatorGroup.resultsAR.arrayOfARDown);}
		if (indicatorGroup.candleStickIdentifierResult != null){ } //signalOfCandlestickGroup.addInput(indicatorGroup.candleStickIdentifierResult.getLastValue());}
		
		if (signalOfCCI.listOfNormalizedValue.size() >= ENCOG_SIGNAL_INPUT){
//			Co.println("--> Size: " + signalOfCCI.listOfNormalizedValue.size());
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			encogWindow.addInputList(signalOfCCI.listOfNormalizedValue.subList(signalOfCCI.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfCCI.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfRSI.listOfNormalizedValue.subList(signalOfRSI.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfRSI.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfUO.listOfNormalizedValue.subList(signalOfUO.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfUO.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARUp.listOfNormalizedValue.subList(signalOfARUp.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARUp.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARDown.listOfNormalizedValue.subList(signalOfARDown.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARDown.listOfNormalizedValue.size()));
			
//			for (int i=0; i<encogWindow.getAsWindow().length; i++){
//				Co.print(" " + encogWindow.getAsWindow()[i]);
//			}
			
			signalOfEncog.setInput(encogWindow);
		}
		
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
		listOfSignalBase.add(signalOfUO);
		listOfSignalBase.add(signalOfARUp);
		listOfSignalBase.add(signalOfARDown);
		listOfSignalBase.add(signalOfCandlestickGroup);
		listOfSignalBase.add(signalOfEncog);
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
