package com.autoStock.indicator;

import java.util.ArrayList;

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
import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.indicator.results.ResultsUO;
import com.autoStock.indicator.results.ResultsWILLR;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalGroup;
import com.autoStock.taLib.Core;
import com.autoStock.types.basic.ImmutableInteger;

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
	public IndicatorOfMFI indicatorOfMFI;
	public IndicatorOfWILLR indicatorOfWILLR;
	public IndicatorOfUO indicatorOfUO;
	public IndicatorOfAR indicatorOfAR;
	public CandleStickIdentifier candleStickIdentifier;

	public ResultsADX resultsADX;
	public ResultsCCI resultsCCI;
	public ResultsDI resultsDI;
	public ResultsBB resultsBB;
	public ResultsMACD resultsMACD;
	public ResultsRSI resultsRSI;
	public ResultsTRIX resultsTRIX;
	public ResultsROC resultsROC;
	public ResultsMFI resultsMFI;
	public ResultsWILLR resultsWILLR;
	public ResultsUO resultsUO;
	public ResultsAR resultsAR;
	public CandleStickIdentifierResult candleStickIdentifierResult;
	
//	public static final ImmutableInteger immutableIntegerForADX = new ImmutableInteger(26);
//	public static final ImmutableInteger immutableIntegerForCCI = new ImmutableInteger(26);
//	public static final ImmutableInteger immutableIntegerForDI = new ImmutableInteger(30);
//	public static final ImmutableInteger immutableIntegerForMACD = new ImmutableInteger(26);
//	public static final ImmutableInteger immutableIntegerForBB = new ImmutableInteger(10);
//	public static final ImmutableInteger immutableIntegerForRSI = new ImmutableInteger(26);
//	public static final ImmutableInteger immutableIntegerForTRIX = new ImmutableInteger(26);
//	public static final ImmutableInteger immutableIntegerForROC = new ImmutableInteger(10);
//	public static final ImmutableInteger immutableIntegerForMFI = new ImmutableInteger(10);
//	public static final ImmutableInteger immutableIntegerForWILLR = new ImmutableInteger(10);
//	public static final ImmutableInteger immutableIntegerForCandleStickIdentifier = new ImmutableInteger(10);

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();
	private ArrayList<SignalMetricType> listOfSignalMetricType = new ArrayList<SignalMetricType>();

	public IndicatorGroup(CommonAnalysisData commonAnlaysisData, SignalGroup signalGroup) {
		int resultLength = SignalGroup.ENCOG_SIGNAL_INPUT;
		
		listOfIndicatorBase.add(indicatorOfADX = new IndicatorOfADX(signalGroup.signalOfADX.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_adx));
		listOfIndicatorBase.add(indicatorOfCCI = new IndicatorOfCCI(signalGroup.signalOfCCI.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_cci));
		listOfIndicatorBase.add(indicatorOfDI = new IndicatorOfDI(signalGroup.signalOfDI.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_di));
		listOfIndicatorBase.add(indicatorOfMACD = new IndicatorOfMACD(signalGroup.signalOfMACD.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_macd));
		listOfIndicatorBase.add(indicatorOfBB = new IndicatorOfBB(new ImmutableInteger(0), 1, commonAnlaysisData, taLibCore, SignalMetricType.none));
		listOfIndicatorBase.add(indicatorOfRSI = new IndicatorOfRSI(signalGroup.signalOfRSI.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_rsi));
		listOfIndicatorBase.add(indicatorOfTRIX = new IndicatorOfTRIX(signalGroup.signalOfTRIX.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_trix));
		listOfIndicatorBase.add(indicatorOfROC = new IndicatorOfROC(signalGroup.signalOfROC.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_roc));
		listOfIndicatorBase.add(indicatorOfMFI = new IndicatorOfMFI(signalGroup.signalOfMFI.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_mfi));
		listOfIndicatorBase.add(indicatorOfWILLR = new IndicatorOfWILLR(signalGroup.signalOfWILLR.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_willr));
		listOfIndicatorBase.add(indicatorOfUO = new IndicatorOfUO(signalGroup.signalOfUO.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_uo));
		listOfIndicatorBase.add(indicatorOfAR = new IndicatorOfAR(signalGroup.signalOfARUp.signalParameters.periodLength, 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_ar_up));
		listOfIndicatorBase.add(candleStickIdentifier = new CandleStickIdentifier(new ImmutableInteger(0), 1, commonAnlaysisData, taLibCore, SignalMetricType.metric_candlestick_group));
		
		Co.println("--> Check: " + signalGroup.signalOfCCI.signalParameters.periodLength.toString());
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
		if (listOfSignalMetricType.contains(SignalMetricType.metric_mfi)){resultsMFI = indicatorOfMFI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_willr)){resultsWILLR = indicatorOfWILLR.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_uo)){resultsUO = indicatorOfUO.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_ar_up)){resultsAR = indicatorOfAR.analyize();}
		
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

	public int getMinPeriodLength() {
		int min = 0;
		IndicatorBase indcatorBase = null;
		
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (indicator instanceof CandleStickIdentifier == false){
				for (SignalMetricType signalMetricType : indicator.getSignalMetricType()){
					if (listOfSignalMetricType.contains(signalMetricType)){
						if (indicator.getRequiredDatasetLength() > min){
							min = indicator.getRequiredDatasetLength();
							indcatorBase = indicator;
						}
					}
				}
			}
		}
		
		Co.println("--> Longest required period length from: " + indcatorBase.getClass().getSimpleName() + ", with: " + min);
		return min;
	}
}
