/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;

import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ResultsTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ChartForAlgorithmTest {
	private String title;
	public ArrayList<Integer> listOfSignalADX = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalPPC = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalDI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalCCI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalMACD = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalSTORSI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalRSI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalTRIX = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalMFI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalROC = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalWILLR = new ArrayList<Integer>();
	//public ArrayList<Integer> listOfSignalSTORSID = new ArrayList<Integer>();

	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	public ArrayList<Double> listOfSizeVolume = new ArrayList<Double>();
	
	public ArrayList<Double> listOfPriceOpen = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceHigh = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceLow = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceClose = new ArrayList<Double>();
	
	public ArrayList<Double> listOfValue = new ArrayList<Double>();
	public ArrayList<Double> listOfEntryAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfExitAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfEntryAtSignal = new ArrayList<Double>();
	public ArrayList<Double> listOfExitAtSignal = new ArrayList<Double>();
	
	public ArrayList<Double> listOfMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfDI = new ArrayList<Double>();
	public ArrayList<Double> listOfCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfRSI = new ArrayList<Double>();
	
	public ArrayList<Double> listOfDebugAlpha = new ArrayList<Double>();
	public ArrayList<Double> listOfDebugBeta = new ArrayList<Double>();
	public ArrayList<Double> listOfDebugGamma = new ArrayList<Double>();
	
	public StrategyOptions strategyOptions;
	
	public ChartForAlgorithmTest(String title){
		this.title = title;
	}
	
	public static enum TimeSeriesType {
		type_signals("Signals"),
		type_price("Price"),
		type_value("Value"),
		type_entry_price("Entry"),
		type_exit_price("Exit"),
		type_entry_signal("Entry"),
		type_exit_signal("Exit"),
		type_debug("Debug"),
		;
		
		public String displayName;
		
		TimeSeriesType(String displayName){
			this.displayName = displayName;
		}
	}

	public void display() {
		TimeSeriesCollection timeSeriesCollectionForSignals = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForValue = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForEntryAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExitAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForEntryAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExitAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForDebug = new TimeSeriesCollection();
		
		DefaultHighLowDataset dataSetForDefaultHighLowDataset = new DefaultHighLowDataset("Price series", ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPriceHigh), ArrayTools.getArrayFromListOfDouble(listOfPriceLow), ArrayTools.getArrayFromListOfDouble(listOfPriceOpen), ArrayTools.getArrayFromListOfDouble(listOfPriceClose), ArrayTools.getArrayFromListOfDouble(listOfSizeVolume));
		
//		timeSeriesCollectionForSignalTotal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal Total", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTotal))));
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_adx)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ADX ", SignalMetricType.metric_adx, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalADX))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_ppc)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalPPC))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_di)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal DI", SignalMetricType.metric_di, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalDI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_cci)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", SignalMetricType.metric_cci, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalCCI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_macd)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MACD", SignalMetricType.metric_macd, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMACD))));}
//		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_storsi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal STORSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalSTORSI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_rsi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal RSI", SignalMetricType.metric_rsi, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalRSI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_trix)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal TRIX", SignalMetricType.metric_trix, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTRIX))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_mfi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MFI", SignalMetricType.metric_mfi, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMFI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_roc)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ROC", SignalMetricType.metric_roc, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalROC))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_willr)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal WILLR",SignalMetricType.metric_willr, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalWILLR))));}
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("DI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfCCI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfMACD))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfRSI))));
		timeSeriesCollectionForPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price ($)", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPriceClose))));
		if (listOfValue.size() != 0){
			timeSeriesCollectionForValue.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Value (%)", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfValue))));
		}
		
		if (listOfEntryAtPrice.size() != 0 && listOfExitAtPrice.size() != 0){
			timeSeriesCollectionForEntryAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfEntryAtPrice))));
			timeSeriesCollectionForExitAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfExitAtPrice))));
		}
		
		if (listOfEntryAtSignal.size() != 0 && listOfExitAtSignal.size() != 0){
			timeSeriesCollectionForEntryAtSignal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfEntryAtPrice))));
			timeSeriesCollectionForExitAtSignal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfExitAtPrice))));
		}
		
		if (listOfDebugAlpha.size() != 0){
			timeSeriesCollectionForDebug.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Debug", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDebugAlpha))));
		}
		
		new CombinedLineChart().new LineChartDisplay(title, 
			dataSetForDefaultHighLowDataset,
			new TimeSeriesTypePair(TimeSeriesType.type_signals, timeSeriesCollectionForSignals),
			new TimeSeriesTypePair(TimeSeriesType.type_price, timeSeriesCollectionForPrice), 
			new TimeSeriesTypePair(TimeSeriesType.type_value, timeSeriesCollectionForValue),
			new TimeSeriesTypePair(TimeSeriesType.type_entry_price, timeSeriesCollectionForEntryAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_exit_price, timeSeriesCollectionForExitAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_entry_signal, timeSeriesCollectionForEntryAtSignal),
			new TimeSeriesTypePair(TimeSeriesType.type_exit_signal, timeSeriesCollectionForExitAtSignal),
			new TimeSeriesTypePair(TimeSeriesType.type_debug, timeSeriesCollectionForDebug)
		);
		
	}
	
	public static class TimeSeriesTypePair {
		public TimeSeriesType timeSeriesType;
		public TimeSeriesCollection timeSeriesCollection;
		
		public TimeSeriesTypePair(TimeSeriesType timeSeriesType, TimeSeriesCollection timeSeriesCollection){
			this.timeSeriesType = timeSeriesType;
			this.timeSeriesCollection = timeSeriesCollection;
		}
	}
}
