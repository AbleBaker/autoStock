package com.autoStock.indicator;

import java.util.ArrayList;
import java.util.Arrays;

import com.autoStock.Co;
import com.autoStock.indicator.candleStick.CandleStickDefinitions.CandleStickIdentity;
import com.autoStock.indicator.candleStick.CandleStickIdentifier;
import com.autoStock.indicator.candleStick.CandleStickIdentifierResult;
import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.indicator.results.ResultsAR;
import com.autoStock.indicator.results.ResultsBB;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.indicator.results.ResultsPTD;
import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.indicator.results.ResultsSTORSI;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.indicator.results.ResultsWILLR;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalGroup;
import com.autoStock.taLib.Core;
import com.autoStock.types.basic.MutableInteger;

/**
 * @author Kevin Kowalewski
 * 
 */
public class IndicatorGroup {
	public Core taLibCore = new Core();
	public IndicatorOfADX indicatorOfADX;
	public IndicatorOfCCI indicatorOfCCI;
	public IndicatorOfDI indicatorOfDI;
	public IndicatorOfMACD indicatorOfMACD;
	public IndicatorOfBB indicatorOfBB;
	public IndicatorOfTRIX indicatorOfTRIX;
	public IndicatorOfRSI indicatorOfRSI;
	public IndicatorOfROC indicatorOfROC;
	public IndicatorOfSTORSI indicatorOfSTORSI;
	public IndicatorOfMFI indicatorOfMFI;
	public IndicatorOfWILLR indicatorOfWILLR;
	public IndicatorOfUO indicatorOfUO;
	public IndicatorOfAR indicatorOfAR;
	public IndicatorOfPTD indicatorOfPTD;
	public CandleStickIdentifier candleStickIdentifier;

	public ResultsADX resultsADX;
	public ResultsCCI resultsCCI;
	public ResultsDI resultsDI;
	public ResultsBB resultsBB;
	public ResultsMACD resultsMACD;
	public ResultsRSI resultsRSI;
	public ResultsTRIX resultsTRIX;
	public ResultsROC resultsROC;
	public ResultsSTORSI resultsSTORSI;
	public ResultsMFI resultsMFI;
	public ResultsWILLR resultsWILLR;
	public ResultsUO resultsUO;
	public ResultsAR resultsAR;
	public ResultsPTD resultsPTD;
	public CandleStickIdentifierResult candleStickIdentifierResult;
	
	public SignalGroup signalGroup;
	public CommonAnalysisData commonAnalysisData;

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();
	private ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();

	public IndicatorGroup(CommonAnalysisData commonAnlaysisData, SignalGroup signalGroup) {
		int resultLength = SignalGroup.ENCOG_SIGNAL_INPUT;
		this.signalGroup = signalGroup;
		this.commonAnalysisData = commonAnlaysisData;
	}
	
	public void initialize(){
		listOfIndicatorBase.clear();
		listOfIndicatorBase.add(indicatorOfADX = new IndicatorOfADX(signalGroup.signalOfADX.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_adx));
		listOfIndicatorBase.add(indicatorOfCCI = new IndicatorOfCCI(signalGroup.signalOfCCI.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_cci));
		listOfIndicatorBase.add(indicatorOfDI = new IndicatorOfDI(signalGroup.signalOfDI.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_di));
		listOfIndicatorBase.add(indicatorOfMACD = new IndicatorOfMACD(signalGroup.signalOfMACD.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_macd));
		listOfIndicatorBase.add(indicatorOfBB = new IndicatorOfBB(new MutableInteger(0), 1, commonAnalysisData, taLibCore, SignalMetricType.none));
		listOfIndicatorBase.add(indicatorOfRSI = new IndicatorOfRSI(signalGroup.signalOfRSI.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_rsi));
		listOfIndicatorBase.add(indicatorOfTRIX = new IndicatorOfTRIX(signalGroup.signalOfTRIX.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_trix));
		listOfIndicatorBase.add(indicatorOfROC = new IndicatorOfROC(signalGroup.signalOfROC.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_roc));
		listOfIndicatorBase.add(indicatorOfMFI = new IndicatorOfMFI(signalGroup.signalOfMFI.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_mfi));
		listOfIndicatorBase.add(indicatorOfSTORSI = new IndicatorOfSTORSI(signalGroup.signalOfSTORSI.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_storsi));
		listOfIndicatorBase.add(indicatorOfWILLR = new IndicatorOfWILLR(signalGroup.signalOfWILLR.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_willr));
		listOfIndicatorBase.add(indicatorOfUO = new IndicatorOfUO(signalGroup.signalOfUO.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_uo));
		listOfIndicatorBase.add(indicatorOfAR = new IndicatorOfAR(signalGroup.signalOfARUp.signalParameters.periodLength, 1, commonAnalysisData, taLibCore, SignalMetricType.metric_ar_up));
		listOfIndicatorBase.add(candleStickIdentifier = new CandleStickIdentifier(new MutableInteger(0), 1, commonAnalysisData, taLibCore, SignalMetricType.metric_candlestick_group));
		
		indicatorOfPTD = new IndicatorOfPTD(new MutableInteger(10), 1, commonAnalysisData, taLibCore, SignalMetricType.none);
	}
	
	public void setDataSet(){
		for (IndicatorBase indicator : listOfIndicatorBase){
			for (SignalMetricType signalMetricType : indicator.getSignalMetricType()){
				if (listOfSignalMetricType.contains(signalMetricType)){
					indicator.setDataSet();
				}
			}
		}
		
		candleStickIdentifier.setDataSet(); 
	}
	
	public void setActive(ArrayList<SignalMetricType> listOfSignalMetricType) {
		if (listOfSignalMetricType == null){throw new NullPointerException();}
		this.listOfSignalMetricType = listOfSignalMetricType;
	}

	public void analyize() {
		if (listOfSignalMetricType.contains(SignalMetricType.metric_adx)){resultsADX = indicatorOfADX.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_cci)){resultsCCI = indicatorOfCCI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_di)){resultsDI = indicatorOfDI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_macd)){resultsMACD = indicatorOfMACD.analize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_rsi)){resultsRSI = indicatorOfRSI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_trix)){resultsTRIX = indicatorOfTRIX.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_roc)){resultsROC = indicatorOfROC.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_storsi)){resultsSTORSI = indicatorOfSTORSI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_mfi)){resultsMFI = indicatorOfMFI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_willr)){resultsWILLR = indicatorOfWILLR.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_uo)){resultsUO = indicatorOfUO.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_ar_up)){resultsAR = indicatorOfAR.analyize();}
		
//		Co.println("-->X: " + signalGroup.signalOfUO.getStrengthWindow().length);
		
//		if (listOfSignalMetricType.contains(SignalMetricType.metric_uo) && signalGroup.signalOfUO.getStrengthWindow().length >= 10){
//			resultsPTD = indicatorOfPTD.analyize(signalGroup.signalOfUO.getStrengthWindow());
//			
////			Co.println("--> Have results? " + resultsPTD.arrayOfPTD.length);
//			
//			for (int i=0; i<resultsPTD.arrayOfPTD.length; i++){
//				Co.print(" " + resultsPTD.arrayOfPTD[i]);
//			}
//		}
		
		if (listOfSignalMetricType.contains(SignalMetricType.metric_candlestick_group)){
			candleStickIdentifierResult = candleStickIdentifier.identify(CandleStickIdentity.hanging_man);
		}
	}
	
	public ArrayList<IndicatorBase> getListOfIndicatorBaseActive(){
		ArrayList<IndicatorBase> list = new ArrayList<IndicatorBase>();
		
		for (IndicatorBase indicatorBase : listOfIndicatorBase){
			for (SignalMetricType signalMetricType : indicatorBase.getSignalMetricType()){
				if (listOfSignalMetricType.contains(signalMetricType)){
					list.add(indicatorBase);
				}
			}
		}
		
		return list;
	}
	
	public ArrayList<IndicatorBase> getListOfIndicatorBase(){
		return listOfIndicatorBase;
	}

	public int getMinPeriodLength(boolean includeAll) {
		int min = 0;
		IndicatorBase indcatorBase = null;
		
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (indicator instanceof CandleStickIdentifier == false){
				for (SignalMetricType signalMetricType : indicator.getSignalMetricType()){
					if (listOfSignalMetricType.contains(signalMetricType) || includeAll){
						if (indicator.getRequiredDatasetLength() > min){
							min = indicator.getRequiredDatasetLength();
							indcatorBase = indicator;
						}
					}
				}
			}
		}
		
//		Co.println("--> Longest required period length from: " + indcatorBase.getClass().getSimpleName() + ", with: " + min);
		return min;
	}
}
