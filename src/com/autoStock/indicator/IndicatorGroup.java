package com.autoStock.indicator;

import java.util.ArrayList;

import com.autoStock.indicator.candleStick.CandleStickDefinitions.CandleStickIdentity;
import com.autoStock.indicator.candleStick.CandleStickIdentifier;
import com.autoStock.indicator.candleStick.CandleStickIdentifierResult;
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

/**
 * @author Kevin Kowalewski
 * 
 */
public class IndicatorGroup {
	public Core taLibCore = new Core();
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

	private ArrayList<IndicatorBase> listOfIndicatorBase = new ArrayList<IndicatorBase>();

	public IndicatorGroup(int periodLength, CommonAnlaysisData commonAnlaysisData) {
		AnalysisTools.setIndicatorPeriodLength(periodLength, listOfIndicatorBase);
		
		indicatorOfCCI = new IndicatorOfCCI(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfDI = new IndicatorOfDI(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfMACD = new IndicatorOfMACD(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfBB = new IndicatorOfBB(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfRSI = new IndicatorOfRSI(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfTRIX = new IndicatorOfTRIX(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfROC = new IndicatorOfROC(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfMFI = new IndicatorOfMFI(periodLength, commonAnlaysisData, taLibCore);
		indicatorOfWILLR = new IndicatorOfWILLR(periodLength, commonAnlaysisData, taLibCore);
		candleStickIdentifier = new CandleStickIdentifier(periodLength, commonAnlaysisData, taLibCore);
		
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
			indicator.periodLength = periodLength;
		}
		
		candleStickIdentifier.setDataSet(listOfQuoteSlice);
	}

	public void analyize(ArrayList<SignalMetricType> listOfSignalMetricType) {
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
}
