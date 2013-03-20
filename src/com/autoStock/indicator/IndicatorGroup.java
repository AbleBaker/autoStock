package com.autoStock.indicator;

import java.util.ArrayList;

import com.autoStock.indicator.candleStick.CandleStickDefinitions.CandleStickIdentity;
import com.autoStock.indicator.candleStick.CandleStickIdentifier;
import com.autoStock.indicator.candleStick.CandleStickIdentifierResult;
import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.indicator.results.ResultsBB;
import com.autoStock.indicator.results.ResultsCCI;
import com.autoStock.indicator.results.ResultsDI;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.indicator.results.ResultsMFI;
import com.autoStock.indicator.results.ResultsROC;
import com.autoStock.indicator.results.ResultsRSI;
import com.autoStock.indicator.results.ResultsTRIX;
import com.autoStock.indicator.results.ResultsWILLR;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.taLib.Core;
import com.autoStock.tools.AnalysisTools;
import com.autoStock.types.QuoteSlice;
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
	public CandleStickIdentifierResult candleStickIdentifierResult;
	
	public static final ImmutableInteger immutableIntegerForADX = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForCCI = new ImmutableInteger(38);
	public static final ImmutableInteger immutableIntegerForDI = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForMACD = new ImmutableInteger(45);
	public static final ImmutableInteger immutableIntegerForBB = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForRSI = new ImmutableInteger(60);
	public static final ImmutableInteger immutableIntegerForTRIX = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForROC = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForMFI = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForWILLR = new ImmutableInteger(30);
	public static final ImmutableInteger immutableIntegerForCandleStickIdentifier = new ImmutableInteger(30);

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();

	public IndicatorGroup(CommonAnlaysisData commonAnlaysisData) {
//		AnalysisTools.setIndicatorPeriodLength(periodLength, listOfIndicatorBase);
		
		indicatorOfADX = new IndicatorOfADX(immutableIntegerForADX, commonAnlaysisData, taLibCore);
		indicatorOfCCI = new IndicatorOfCCI(immutableIntegerForCCI, commonAnlaysisData, taLibCore);
		indicatorOfDI = new IndicatorOfDI(immutableIntegerForDI, commonAnlaysisData, taLibCore);
		indicatorOfMACD = new IndicatorOfMACD(immutableIntegerForMACD, commonAnlaysisData, taLibCore);
		indicatorOfBB = new IndicatorOfBB(immutableIntegerForBB, commonAnlaysisData, taLibCore);
		indicatorOfRSI = new IndicatorOfRSI(immutableIntegerForRSI, commonAnlaysisData, taLibCore);
		indicatorOfTRIX = new IndicatorOfTRIX(immutableIntegerForTRIX, commonAnlaysisData, taLibCore);
		indicatorOfROC = new IndicatorOfROC(immutableIntegerForROC, commonAnlaysisData, taLibCore);
		indicatorOfMFI = new IndicatorOfMFI(immutableIntegerForMFI, commonAnlaysisData, taLibCore);
		indicatorOfWILLR = new IndicatorOfWILLR(immutableIntegerForWILLR, commonAnlaysisData, taLibCore);
		candleStickIdentifier = new CandleStickIdentifier(immutableIntegerForCandleStickIdentifier, commonAnlaysisData, taLibCore);
		
		listOfIndicatorBase.add(indicatorOfADX);
		listOfIndicatorBase.add(indicatorOfCCI);
		listOfIndicatorBase.add(indicatorOfDI);
		listOfIndicatorBase.add(indicatorOfMACD);
		listOfIndicatorBase.add(indicatorOfBB);
		listOfIndicatorBase.add(indicatorOfRSI);
		listOfIndicatorBase.add(indicatorOfTRIX);
		listOfIndicatorBase.add(indicatorOfROC);
		listOfIndicatorBase.add(indicatorOfMFI);
		listOfIndicatorBase.add(indicatorOfWILLR);
	}
	
	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice, int periodLength){
		for (IndicatorBase indicator : listOfIndicatorBase){
			indicator.setDataSet(listOfQuoteSlice);
//			indicator.periodLength = periodLength;
		}
		
		candleStickIdentifier.setDataSet(listOfQuoteSlice);
	}

	public void analyize(ArrayList<SignalMetricType> listOfSignalMetricType) {
		if (listOfSignalMetricType.contains(SignalMetricType.metric_adx)){resultsADX = indicatorOfADX.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_cci)){resultsCCI = indicatorOfCCI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_di)){resultsDI = indicatorOfDI.analize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_macd)){resultsMACD = indicatorOfMACD.analize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_rsi)){resultsRSI = indicatorOfRSI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_trix)){resultsTRIX = indicatorOfTRIX.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_roc)){resultsROC = indicatorOfROC.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_mfi)){resultsMFI = indicatorOfMFI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_willr)){resultsWILLR = indicatorOfWILLR.analyize();}
		
		if (listOfSignalMetricType.contains(SignalMetricType.metric_candlestick_group)){
			candleStickIdentifierResult = candleStickIdentifier.identify(CandleStickIdentity.hanging_man);
		}
	}
	
	public ArrayList<IndicatorBase> getListOfIndicatorBase(){
		return listOfIndicatorBase;
	}

	public int getMinPeriodLength() {
		int min = 0;
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (indicator.periodLength.value > min){
				min = indicator.periodLength.value;
			}
		}
		
		return min;
	}
}
