package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.indicator.results.ResultsBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalParameters;
import com.autoStock.signal.SignalDefinitions.SignalParametersForADX;
import com.autoStock.signal.SignalDefinitions.SignalParametersForARDown;
import com.autoStock.signal.SignalDefinitions.SignalParametersForARUp;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCCI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCandlestickGroup;
import com.autoStock.signal.SignalDefinitions.SignalParametersForCrossover;
import com.autoStock.signal.SignalDefinitions.SignalParametersForDI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForEncog;
import com.autoStock.signal.SignalDefinitions.SignalParametersForMACD;
import com.autoStock.signal.SignalDefinitions.SignalParametersForMFI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForROC;
import com.autoStock.signal.SignalDefinitions.SignalParametersForRSI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForSAR;
import com.autoStock.signal.SignalDefinitions.SignalParametersForSTORSI;
import com.autoStock.signal.SignalDefinitions.SignalParametersForTRIX;
import com.autoStock.signal.SignalDefinitions.SignalParametersForUO;
import com.autoStock.signal.SignalDefinitions.SignalParametersForWILLR;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.signalMetrics.SignalOfADX;
import com.autoStock.signal.signalMetrics.SignalOfARDown;
import com.autoStock.signal.signalMetrics.SignalOfARUp;
import com.autoStock.signal.signalMetrics.SignalOfCCI;
import com.autoStock.signal.signalMetrics.SignalOfCandlestickGroup;
import com.autoStock.signal.signalMetrics.SignalOfCrossover;
import com.autoStock.signal.signalMetrics.SignalOfDI;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.signal.signalMetrics.SignalOfEncogNew;
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
import com.autoStock.trading.types.Position;
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
	
	//public SignalOfEncogNew signalOfEncog = new SignalOfEncogNew(new SignalParametersForEncog());
	public SignalOfEncog signalOfEncog = new SignalOfEncog(new SignalParametersForEncog());
	public SignalOfCandlestickGroup signalOfCandlestickGroup = new SignalOfCandlestickGroup(new SignalParametersForCandlestickGroup());
	
	public SignalOfHT signalOfHT = new SignalOfHT(SignalMetricType.none, new SignalParameters(new NormalizeInterface() {
		@Override
		public double normalize(double input) {
			return input;
		}
	}, new MutableInteger(1), null, null, null, null){});
	
	private ArrayList<SignalBase> listOfSignalBase = new ArrayList<SignalBase>();
	private SignalGenerator signalGenerator = new SignalGenerator();
	
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
	
	public void generateSignalsCached(){
		signalGenerator.generateEncogSignal(this, null);
	}
	
	public void generateSignals(CommonAnalysisData commonAnalysisData, Position position){
		for (SignalBase signalBase : listOfSignalBase){
			IndicatorBase<?> indicatorBase = indicatorGroup.getIndicatorByMetric(signalBase.signalMetricType);
			
			if (indicatorBase != null && indicatorBase.getResults() != null){
				signalBase.setInput((ResultsBase) indicatorBase.getResults());
			}
		}
	}
	
	public void processEncog(ArrayList<EncogFrame> encogFrames) {
		signalGenerator.generateEncogSignal(this, encogFrames);
	}
	
	public IndicatorGroup getIndicatorGroup(){
		return indicatorGroup;
	}

	public SignalBase getSignalBaseForType(SignalMetricType signalMetricType) {
		for (SignalBase signalBase : listOfSignalBase){
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
