package com.autoStock.indicator;

import java.util.ArrayList;

import org.apache.http.MethodNotSupportedException;

import com.autoStock.Co;
import com.autoStock.indicator.IndicatorOfCSO.ResultsCSO;
import com.autoStock.indicator.candleStick.CandleStickDefinitions.CandleStickIdentity;
import com.autoStock.indicator.candleStick.CandleStickIdentifier;
import com.autoStock.indicator.candleStick.CandleStickIdentifierResult;
import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.indicator.results.ResultsAR;
import com.autoStock.indicator.results.ResultsBB;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.indicator.results.ResultsEMA;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.indicator.results.ResultsPTD;
import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.indicator.results.ResultsSAR;
import com.autoStock.indicator.results.ResultsSTORSI;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.indicator.results.ResultsWILLR;
import com.autoStock.signal.SignalDefinitions.*;
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
//	public IndicatorOfADX indicatorOfADX;
//	public IndicatorOfCCI indicatorOfCCI;
//	public IndicatorOfDI indicatorOfDI;
//	public IndicatorOfMACD indicatorOfMACD;
//	public IndicatorOfBB indicatorOfBB;
//	public IndicatorOfTRIX indicatorOfTRIX;
//	public IndicatorOfRSI indicatorOfRSI;
//	public IndicatorOfROC indicatorOfROC;
//	public IndicatorOfSTORSI indicatorOfSTORSI;
//	public IndicatorOfMFI indicatorOfMFI;
//	public IndicatorOfWILLR indicatorOfWILLR;
//	public IndicatorOfUO indicatorOfUO;
//	public IndicatorOfAR indicatorOfAR;
//	public IndicatorOfSAR indicatorOfSAR;
//	public IndicatorOfEMA indicatorOfEMAFirst;
//	public IndicatorOfEMA indicatorOfEMASecond;
//	public IndicatorOfCSO indicatorOfCSO;
	
	public CandleStickIdentifier candleStickIdentifier;

//	public ResultsADX resultsADX;
//	public ResultsCCI resultsCCI;
//	public ResultsDI resultsDI;
//	public ResultsBB resultsBB;
//	public ResultsMACD resultsMACD;
//	public ResultsRSI resultsRSI;
//	public ResultsTRIX resultsTRIX;
//	public ResultsROC resultsROC;
//	public ResultsSTORSI resultsSTORSI;
//	public ResultsMFI resultsMFI;
//	public ResultsWILLR resultsWILLR;
//	public ResultsUO resultsUO;
//	public ResultsAR resultsAR;
//	public ResultsPTD resultsPTD;
//	public ResultsSAR resultsSAR;
//	public ResultsCSO resultsCSO;
	
	public ResultsEMA resultsEMAFirst;
	public ResultsEMA resultsEMASecond;
	
	public CandleStickIdentifierResult candleStickIdentifierResult;
	
	public SignalGroup signalGroup;
	public CommonAnalysisData commonAnalysisData;

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();
	private ArrayList<SignalMetricType> listOfSignalMetricTypeAnalyze = new ArrayList<SignalMetricType>();
	private ArrayList<SignalMetricType> listOfSignalMetricTypeActive = new ArrayList<SignalMetricType>();

	public IndicatorGroup(CommonAnalysisData commonAnlaysisData, SignalGroup signalGroup) {
		this.signalGroup = signalGroup;
		this.commonAnalysisData = commonAnlaysisData;
		
		listOfIndicatorBase.clear();
		listOfIndicatorBase.add(new IndicatorOfADX(new IndicatorParametersForADX(), commonAnalysisData, taLibCore, SignalMetricType.metric_adx));
		listOfIndicatorBase.add(new IndicatorOfCCI(new IndicatorParametersForCCI(), commonAnalysisData, taLibCore, SignalMetricType.metric_cci));
		listOfIndicatorBase.add(new IndicatorOfDI(new IndicatorParametersForDI(), commonAnalysisData, taLibCore, SignalMetricType.metric_di));
		listOfIndicatorBase.add(new IndicatorOfMACD(new IndicatorParametersForMACD(), commonAnalysisData, taLibCore, SignalMetricType.metric_macd));
//		listOfIndicatorBase.add(new IndicatorOfBB(new MutableInteger(0), 1, commonAnalysisData, taLibCore, SignalMetricType.none));
		listOfIndicatorBase.add(new IndicatorOfRSI(new IndicatorParametersForRSI(), commonAnalysisData, taLibCore, SignalMetricType.metric_rsi));
		listOfIndicatorBase.add(new IndicatorOfTRIX(new IndicatorParametersForTRIX(), commonAnalysisData, taLibCore, SignalMetricType.metric_trix));
		listOfIndicatorBase.add(new IndicatorOfROC(new IndicatorParametersForROC(), commonAnalysisData, taLibCore, SignalMetricType.metric_roc));
		listOfIndicatorBase.add(new IndicatorOfMFI(new IndicatorParametersForMFI(), commonAnalysisData, taLibCore, SignalMetricType.metric_mfi));
		listOfIndicatorBase.add(new IndicatorOfSTORSI(new IndicatorParametersForSTORSI(), commonAnalysisData, taLibCore, SignalMetricType.metric_storsi));
		listOfIndicatorBase.add(new IndicatorOfWILLR(new IndicatorParametersForWILLR(), commonAnalysisData, taLibCore, SignalMetricType.metric_willr));
		listOfIndicatorBase.add(new IndicatorOfUO(new IndicatorParametersForUO(), commonAnalysisData, taLibCore, SignalMetricType.metric_uo));
		listOfIndicatorBase.add(new IndicatorOfAR(new IndicatorParametersForARUp(), commonAnalysisData, taLibCore, SignalMetricType.metric_ar_up));
		listOfIndicatorBase.add(new IndicatorOfSAR(new IndicatorParametersForSAR(), commonAnalysisData, taLibCore, SignalMetricType.metric_sar));
		listOfIndicatorBase.add(new IndicatorOfCSO(new IndicatorParametersForBasic(), commonAnlaysisData, taLibCore, SignalMetricType.none));
		
		listOfIndicatorBase.add(new IndicatorOfEMA(new IndicatorParametersForEMAFirst(), commonAnalysisData, taLibCore, SignalMetricType.metric_crossover));
		listOfIndicatorBase.add(new IndicatorOfEMA(new IndicatorParametersForEMASecond(), commonAnalysisData, taLibCore, SignalMetricType.metric_crossover));
		
		
//		listOfIndicatorBase.add(candleStickIdentifier = new CandleStickIdentifier(new IndicatorParameters(new MutableInteger(30), 1) {}, commonAnalysisData, taLibCore, SignalMetricType.metric_candlestick_group));
	}
	
	@SuppressWarnings("rawtypes")
	public IndicatorBase getIndicatorByClass(Class clazz){
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (clazz.isInstance(indicator)){
				return indicator;
			}
		}
		
		return null;
	}
	
	public IndicatorBase getIndicatorByMetric(SignalMetricType metricType){
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (indicator.getSignalMetricType() == metricType){
				return indicator;
			}
		}
		
		return null;
	}
	
	public void setDataSet(){
		for (IndicatorBase<?> indicator : listOfIndicatorBase){
			for (SignalMetricType signalMetricType : indicator.getSignalMetricTypeList()){
				if (listOfSignalMetricTypeAnalyze.contains(signalMetricType)){
					indicator.setDataSet();
				}
			}
		}
		
		if (candleStickIdentifier != null){candleStickIdentifier.setDataSet();} 
	}
	
	public void setAnalyze(ArrayList<SignalMetricType> listOfSignalMetricType) {
		if (listOfSignalMetricType == null){throw new NullPointerException();}
		this.listOfSignalMetricTypeAnalyze = (ArrayList<SignalMetricType>) listOfSignalMetricType.clone();
	}
	
	public void setActive(ArrayList<SignalMetricType> listOfSignalMetricType) {
		if (listOfSignalMetricType == null){throw new NullPointerException();}
		this.listOfSignalMetricTypeActive = (ArrayList<SignalMetricType>) listOfSignalMetricType.clone();
	}
	
	public void analyze(){
		for (IndicatorBase indicatorBase : listOfIndicatorBase){
			if (listOfSignalMetricTypeAnalyze.contains(indicatorBase.getSignalMetricType())){
				indicatorBase.analyze();
			}
		}
	}

	public void analyzeOld() {
		throw new NoSuchMethodError("Don't call this");
		
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_adx)){resultsADX = indicatorOfADX.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_cci)){resultsCCI = indicatorOfCCI.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_di)){resultsDI = indicatorOfDI.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_macd)){resultsMACD = indicatorOfMACD.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_rsi)){resultsRSI = indicatorOfRSI.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_trix)){resultsTRIX = indicatorOfTRIX.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_roc)){resultsROC = indicatorOfROC.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_storsi)){resultsSTORSI = indicatorOfSTORSI.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_mfi)){resultsMFI = indicatorOfMFI.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_willr)){resultsWILLR = indicatorOfWILLR.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_uo)){resultsUO = indicatorOfUO.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_ar_up)){resultsAR = indicatorOfAR.analyize();}
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_sar)){resultsSAR = indicatorOfSAR.analyize();}
//		
////		resultsCSO = (ResultsCSO) indicatorOfCSO.analyize();
//		
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_crossover)){
//			resultsEMAFirst = indicatorOfEMAFirst.analyize();
//			resultsEMASecond = indicatorOfEMASecond.analyize();
//		}
//		
////		Co.println("-->X: " + signalGroup.signalOfUO.getStrengthWindow().length);
//		
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_uo) && signalGroup.signalOfUO.getStrengthWindow().length >= 10){
////			resultsPTD = indicatorOfPTD.analyize(signalGroup.signalOfUO.getStrengthWindow());
//			
////			Co.println("--> Have results? " + resultsPTD.arrayOfPTD.length);
//			
////			for (int i=0; i<resultsPTD.arrayOfPTD.length; i++){
////				Co.print(" " + resultsPTD.arrayOfPTD[i]);
////			}
//		}
//		
//		if (listOfSignalMetricTypeAnalyze.contains(SignalMetricType.metric_candlestick_group) && candleStickIdentifier != null){
//			candleStickIdentifierResult = candleStickIdentifier.identify(CandleStickIdentity.hanging_man);
//		}
	}
	
	public ArrayList<IndicatorBase> getListOfIndicatorBaseActive(){
		ArrayList<IndicatorBase> list = new ArrayList<IndicatorBase>();
		
		for (IndicatorBase<?> indicatorBase : listOfIndicatorBase){
			for (SignalMetricType signalMetricType : indicatorBase.getSignalMetricTypeList()){
				if (listOfSignalMetricTypeActive.contains(signalMetricType)){
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
		IndicatorBase<?> indcatorBase = null;
		
		for (IndicatorBase<?> indicator : listOfIndicatorBase){
			if (indicator instanceof CandleStickIdentifier == false){
				for (SignalMetricType signalMetricType : indicator.getSignalMetricTypeList()){
					if (listOfSignalMetricTypeAnalyze.contains(signalMetricType) || includeAll){
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
