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
import com.autoStock.signal.SignalDefinitions;
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

	public IndicatorGroup(CommonAnlaysisData commonAnlaysisData) {
		listOfIndicatorBase.add(indicatorOfADX = new IndicatorOfADX(SignalMetricType.metric_adx.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfCCI = new IndicatorOfCCI(SignalMetricType.metric_cci.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfDI = new IndicatorOfDI(SignalMetricType.metric_di.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfMACD = new IndicatorOfMACD(SignalMetricType.metric_macd.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfBB = new IndicatorOfBB(new ImmutableInteger(0), commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfRSI = new IndicatorOfRSI(SignalMetricType.metric_rsi.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfTRIX = new IndicatorOfTRIX(SignalMetricType.metric_trix.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfROC = new IndicatorOfROC(SignalMetricType.metric_roc.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfMFI = new IndicatorOfMFI(SignalMetricType.metric_mfi.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfWILLR = new IndicatorOfWILLR(SignalMetricType.metric_willr.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfUO = new IndicatorOfUO(SignalMetricType.metric_uo.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(indicatorOfAR = new IndicatorOfAR(SignalMetricType.metric_ar_up.periodLength, commonAnlaysisData, taLibCore));
		listOfIndicatorBase.add(candleStickIdentifier = new CandleStickIdentifier(new ImmutableInteger(0), commonAnlaysisData, taLibCore));
	}
	
	public void setDataSet(ArrayList<QuoteSlice> listOfQuoteSlice){
		for (IndicatorBase indicator : listOfIndicatorBase){
			indicator.setDataSet(listOfQuoteSlice);
		}
		
		candleStickIdentifier.setDataSet(listOfQuoteSlice);
	}
	
	public void setActive(ArrayList<SignalMetricType> listOfSignalMetricType) {
		if (listOfSignalMetricType == null){throw new NullPointerException();}
		this.listOfSignalMetricType = listOfSignalMetricType;
	}

	public void analyize() {
		if (listOfSignalMetricType.contains(SignalMetricType.metric_adx)){resultsADX = indicatorOfADX.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_cci)){resultsCCI = indicatorOfCCI.analyize();}
		if (listOfSignalMetricType.contains(SignalMetricType.metric_di)){resultsDI = indicatorOfDI.analize();}
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
	
	public ArrayList<IndicatorBase> getListOfIndicatorBase(){
		return listOfIndicatorBase;
	}

	public int getMinPeriodLength() {
		int min = 0;
		for (IndicatorBase indicator : listOfIndicatorBase){
			if (indicator instanceof CandleStickIdentifier == false){
				if (indicator.periodLength.value > min){
					min = indicator.periodLength.value;
				}
			}
		}
		
//		Co.println("--> Min period: " + min);
		
		return min;
	}
}
