package com.autoStock.signal;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalDefinitions.*;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.signal.signalMetrics.SignalOfADX;
import com.autoStock.signal.signalMetrics.SignalOfARDown;
import com.autoStock.signal.signalMetrics.SignalOfARUp;
import com.autoStock.signal.signalMetrics.SignalOfCCI;
import com.autoStock.signal.signalMetrics.SignalOfCandlestickGroup;
import com.autoStock.signal.signalMetrics.SignalOfCrossover;
import com.autoStock.signal.signalMetrics.SignalOfDI;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.signal.signalMetrics.SignalOfHT;
import com.autoStock.signal.signalMetrics.SignalOfMACD;
import com.autoStock.signal.signalMetrics.SignalOfMFI;
import com.autoStock.signal.signalMetrics.SignalOfROC;
import com.autoStock.signal.signalMetrics.SignalOfRSI;
import com.autoStock.signal.signalMetrics.SignalOfSAR;
import com.autoStock.signal.signalMetrics.SignalOfSTORSI;
import com.autoStock.signal.signalMetrics.SignalOfTRIX;
import com.autoStock.signal.signalMetrics.SignalOfUO;
import com.autoStock.signal.signalMetrics.SignalOfWILLR;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.PrintTools;
import com.autoStock.types.basic.MutableInteger;

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
	public SignalOfSAR signalOfSAR = new SignalOfSAR(new SignalParametersForSAR());
	
	public SignalOfCrossover signalOfCrossover = new SignalOfCrossover(SignalMetricType.metric_crossover, new SignalParametersForCrossover());
	
	public SignalOfEncog signalOfEncog = new SignalOfEncog(new SignalParametersForEncog());
	public SignalOfCandlestickGroup signalOfCandlestickGroup = new SignalOfCandlestickGroup(new SignalParametersForCandlestickGroup());
	
	public SignalOfHT signalOfHT = new SignalOfHT(SignalMetricType.none, new SignalParameters(new NormalizeInterface() {
		@Override
		public double normalize(double input) {
			return input;
		}
	}, new MutableInteger(1), null, null, null, null){});
	
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
		listOfSignalBase.add(signalOfSAR);
		
		listOfSignalBase.add(signalOfCrossover);
		
		listOfSignalBase.add(signalOfCandlestickGroup);
		listOfSignalBase.add(signalOfEncog);
	}
	
	public void setIndicatorGroup(IndicatorGroup indicatorGroup){
		this.indicatorGroup = indicatorGroup;
	}
	
	public void generateSignals(CommonAnalysisData commonAnlaysisData){
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
		if (indicatorGroup.resultsSAR != null){signalOfSAR.setInput(ArrayTools.getLastElement(indicatorGroup.resultsSAR.arrayOfSAR), ArrayTools.getLastElement(commonAnlaysisData.arrayOfPriceClose));}
		
		if (indicatorGroup.resultsEMAFirst != null && indicatorGroup.resultsEMASecond != null){
			signalOfCrossover.setInput(ArrayTools.getLastElement(indicatorGroup.resultsEMAFirst.arrayOfEMA), ArrayTools.getLastElement(indicatorGroup.resultsEMASecond.arrayOfEMA));
		}
		
		if (indicatorGroup.candleStickIdentifierResult != null){ } //signalOfCandlestickGroup.addInput(indicatorGroup.candleStickIdentifierResult.getLastValue());}
		
		signalOfHT.analyize(signalOfUO.getStrengthWindow());
		
//		Co.println("--> " + signalOfCCI.listOfNormalizedValuePersist.size());
		
		if (signalOfEncog.isLongEnough(signalOfCCI, signalOfUO)){
//			Co.println("--> Trying to use Encog!");
			
			EncogInputWindow encogWindow = new EncogInputWindow();
			
//			Co.println("--> A: " + PrintTools.getString(signalOfCCI.getNormalizedWindow(4)));
			
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfCCI.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfUO.getNormalizedWindow(SignalOfEncog.INPUT_WINDOW_PS + 1)), 1, SignalOfEncog.INPUT_WINDOW_PS + 1));
			
//			Co.println("--> B: " + encogWindow.getAsWindow().length);
			
//			Co.println(PrintTools.getString(encogWindow.getAsWindow()));
			
//			Co.println("--> C");
			
//			Co.println(PrintTools.getStringFromArray(MathTools.getDeltasAsPercent(signalOfCCI.getStrengthWindow())));
			
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfCCI.getStrengthWindow()), 1, 5));
			
			
//			encogWindow.addInput(signalOfUO.getStrength());
////			encogWindow.addInput(signalOfCCI.getStrength());
//			encogWindow.addInput(signalOfARUp.getStrength());
//			encogWindow.addInput(signalOfARDown.getStrength());
////			encogWindow.addInput(signalOfTRIX.getStrength());
			
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfUO.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfTRIX.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARUp.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputArray(Arrays.copyOfRange(MathTools.getDeltasAsPercent(signalOfARDown.getStrengthWindow()), 0, ENCOG_SIGNAL_INPUT));
//			encogWindow.addInputList(signalOfRSI.listOfNormalizedValue.subList(signalOfRSI.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfRSI.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfUO.listOfNormalizedValue.subList(signalOfUO.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfUO.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARUp.listOfNormalizedValue.subList(signalOfARUp.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARUp.listOfNormalizedValue.size()));
//			encogWindow.addInputList(signalOfARDown.listOfNormalizedValue.subList(signalOfARDown.listOfNormalizedValue.size() - ENCOG_SIGNAL_INPUT, signalOfARDown.listOfNormalizedValue.size()));
//			
//			
//			Co.println("\n");
//			
//			for (int i=0; i<signalOfUO.getStrengthWindow().length; i++){
//				Co.print(" " + signalOfUO.getStrengthWindow()[i]);
//			}
//			
//			Co.print(" | ");
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
