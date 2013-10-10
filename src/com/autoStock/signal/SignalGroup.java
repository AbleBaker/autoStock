package com.autoStock.signal;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParametersForADX;
import com.autoStock.signal.SignalDefinitions.SignalParametersForARDown;
import com.autoStock.signal.SignalDefinitions.SignalParametersForARUp;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCCI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCandlestickGroup;
import com.autoStock.signal.SignalDefinitions.SignalParametersForDI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForEncog;
import com.autoStock.signal.SignalDefinitions.SignalParametersForMACD;
import com.autoStock.signal.SignalDefinitions.SignalParametersForMFI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForROC;
import com.autoStock.signal.SignalDefinitions.SignalParametersForRSI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForSTORSI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForTRIX;
import com.autoStock.signal.SignalDefinitions.SignalParametersForUO;
import com.autoStock.signal.SignalDefinitions.SignalParametersForWILLR;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.tools.ArrayTools;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGroup {
	public IndicatorGroup indicatorGroup;
	public SignalOfCCI signalOfCCI = new SignalOfCCI(new SignalParametersForCCI());
	public SignalOfADX signalOfADX = new SignalOfADX(new SignalParametersForADX());
	public SignalOfDI signalOfDI = new SignalOfDI(new SignalParametersForDI());
	public SignalOfMACD signalOfMACD = new SignalOfMACD(new SignalParametersForMACD());
	public SignalOfRSI signalOfRSI = new SignalOfRSI(new SignalParametersForRSI());
	public SignalOfTRIX signalOfTRIX = new SignalOfTRIX(new SignalParametersForTRIX());
	public SignalOfROC signalOfROC = new SignalOfROC(new SignalParametersForROC());
	public SignalOfSTORSI signalOfSTORSI = new SignalOfSTORSI(new SignalParametersForSTORSI());
	public SignalOfMFI signalOfMFI = new SignalOfMFI(new SignalParametersForMFI());
	public SignalOfWILLR signalOfWILLR = new SignalOfWILLR(new SignalParametersForWILLR());
	public SignalOfUO signalOfUO = new SignalOfUO(new SignalParametersForUO());
	public SignalOfARUp signalOfARUp = new SignalOfARUp(new SignalParametersForARUp());
	public SignalOfARDown signalOfARDown = new SignalOfARDown(new SignalParametersForARDown());
	public SignalOfEncog signalOfEncog = new SignalOfEncog(new SignalParametersForEncog());
	public SignalOfCandlestickGroup signalOfCandlestickGroup = new SignalOfCandlestickGroup(new SignalParametersForCandlestickGroup());
	
	public static final int ENCOG_SIGNAL_INPUT = 10;
	
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	
	public SignalGroup(){
		listOfSignalBase.add(signalOfCCI);
		listOfSignalBase.add(signalOfADX);
		listOfSignalBase.add(signalOfDI);
		listOfSignalBase.add(signalOfMACD);
		listOfSignalBase.add(signalOfRSI);
		listOfSignalBase.add(signalOfTRIX);
		listOfSignalBase.add(signalOfROC);
		listOfSignalBase.add(signalOfSTORSI);
		listOfSignalBase.add(signalOfMFI);
		listOfSignalBase.add(signalOfWILLR);
		listOfSignalBase.add(signalOfUO);
		listOfSignalBase.add(signalOfARUp);
		listOfSignalBase.add(signalOfARDown);
		listOfSignalBase.add(signalOfCandlestickGroup);
		listOfSignalBase.add(signalOfEncog);
	}
	
	public void setIndicatorGroup(IndicatorGroup indicatorGroup){
		this.indicatorGroup = indicatorGroup;
	}
	
	public void generateSignals(CommonAnalysisData commonAnlaysisData, int periodLength){
		if (indicatorGroup.resultsCCI != null){signalOfCCI.setInput(ArrayTools.getLastElement(indicatorGroup.resultsCCI.arrayOfCCI));}
		if (indicatorGroup.resultsADX != null){signalOfADX.setInput(ArrayTools.getLastElement(indicatorGroup.resultsADX.arrayOfADX));}
		if (indicatorGroup.resultsDI != null){signalOfDI.setInput(indicatorGroup.resultsDI.arrayOfDIPlus[0], indicatorGroup.resultsDI.arrayOfDIMinus[0]);}
		if (indicatorGroup.resultsMACD != null){signalOfMACD.setInput(ArrayTools.getLastElement(indicatorGroup.resultsMACD.arrayOfMACDHistogram));}
		if (indicatorGroup.resultsRSI != null){signalOfRSI.setInput(ArrayTools.getLastElement(indicatorGroup.resultsRSI.arrayOfRSI));}
		if (indicatorGroup.resultsTRIX != null){signalOfTRIX.setInput(ArrayTools.getLastElement(indicatorGroup.resultsTRIX.arrayOfTRIX));}
		if (indicatorGroup.resultsROC != null){signalOfROC.setInput(ArrayTools.getLastElement(indicatorGroup.resultsROC.arrayOfROC));}
		if (indicatorGroup.resultsSTORSI != null){signalOfSTORSI.setInput(ArrayTools.getLastElement(indicatorGroup.resultsSTORSI.arrayOfPercentK));}
		if (indicatorGroup.resultsMFI != null){signalOfMFI.setInput(ArrayTools.getLastElement(indicatorGroup.resultsMFI.arrayOfMFI));}
		if (indicatorGroup.resultsWILLR != null){signalOfWILLR.setInput(ArrayTools.getLastElement(indicatorGroup.resultsWILLR.arrayOfWILLR));}
		if (indicatorGroup.resultsUO != null){signalOfUO.setInput(ArrayTools.getLastElement(indicatorGroup.resultsUO.arrayOfUO));}
		if (indicatorGroup.resultsAR != null){signalOfARUp.setInput(ArrayTools.getLastElement(indicatorGroup.resultsAR.arrayOfARUp));}
		if (indicatorGroup.resultsAR != null){signalOfARDown.setInput(ArrayTools.getLastElement(indicatorGroup.resultsAR.arrayOfARDown));}
		if (indicatorGroup.candleStickIdentifierResult != null){ } //signalOfCandlestickGroup.addInput(indicatorGroup.candleStickIdentifierResult.getLastValue());}
		
		if (signalOfCCI.listOfNormalizedValue.size() >= ENCOG_SIGNAL_INPUT){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			encogWindow.addInputArray(Arrays.copyOfRange(signalOfCCI.getStrengthWindow(), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputList(signalOfRSI.listOfNormalizedValue.subList(signalOfRSI.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfRSI.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfUO.listOfNormalizedValue.subList(signalOfUO.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfUO.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARUp.listOfNormalizedValue.subList(signalOfARUp.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARUp.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARDown.listOfNormalizedValue.subList(signalOfARDown.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARDown.listOfNormalizedValue.size()));
//			
//			for (int i=0; i<encogWindow.getAsWindow().length; i++){
//				Co.print(" " + encogWindow.getAsWindow()[i]);
//			}
			
			signalOfEncog.setInput(encogWindow);
		}
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
	
	public ArrayList<SignalBase> getListOfSignalBase(){
		return listOfSignalBase;
	}
	
	public void reset(){
		for (SignalBase signalBase : listOfSignalBase){
			signalBase.reset();
		}
	}
}
