/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;

import com.autoStock.algorithm.AlgorithmBase;
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
	private AlgorithmBase algorithmBase;
	public ArrayList<Double> listOfSignalADX = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalPPC = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalDI = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalSTORSI = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalRSI = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalTRIX = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalMFI = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalROC = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalWILLR = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalUO = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalARUp = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalARDown = new ArrayList<Double>();
	public ArrayList<Double> listOfSignalSAR = new ArrayList<Double>();
	public ArrayList<Double> listOfIndicatorSAR = new ArrayList<Double>();
	
	public ArrayList<Double> listOfIndicatorEMAFirst = new ArrayList<Double>();
	public ArrayList<Double> listOfIndicatorEMASecond = new ArrayList<Double>();
	
	//public ArrayList<Integer> listOfSignalSTORSID = new ArrayList<Integer>();

	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	public ArrayList<Double> listOfSizeVolume = new ArrayList<Double>();
	
	public ArrayList<Double> listOfPriceOpen = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceHigh = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceLow = new ArrayList<Double>();
	public ArrayList<Double> listOfPriceClose = new ArrayList<Double>();
	
	public ArrayList<Double> listOfValue = new ArrayList<Double>();
	public ArrayList<Double> listOfYield = new ArrayList<Double>();
	
	public ArrayList<Double> listOfLongEntryAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfShortEntryAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfReEntryAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfLongExitAtPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfShortExitAtPrice = new ArrayList<Double>();
	
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
	
	public ChartForAlgorithmTest(String title, AlgorithmBase algorithmBase){
		this.title = title;
		this.algorithmBase = algorithmBase;
	}
	
	public static enum TimeSeriesType {
		type_signals("Signals"),
		type_price("Price"),
		type_value("Value"),
		type_yield("Yield"),
		type_long_entry_price("Long Entry"),
		type_short_entry_price("Short Entry"),
		type_reentry_price("Reentry"),
		type_long_exit_price("Exit"),
		type_short_exit_price("Exit"),
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
		TimeSeriesCollection timeSeriesCollectionForYield = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForLongEntryAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForShortEntryAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForReEntryAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForLongExitAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForShortExitAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForEntryAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExitAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForDebug = new TimeSeriesCollection();
		
		DefaultHighLowDataset dataSetForDefaultHighLowDataset = new DefaultHighLowDataset("Price series", ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPriceHigh), ArrayTools.getArrayFromListOfDouble(listOfPriceLow), ArrayTools.getArrayFromListOfDouble(listOfPriceOpen), ArrayTools.getArrayFromListOfDouble(listOfPriceClose), ArrayTools.getArrayFromListOfDouble(listOfSizeVolume));

		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_adx)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ADX ", SignalMetricType.metric_adx, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalADX))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_ppc)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalPPC))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_di)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal DI", SignalMetricType.metric_di, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalDI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_cci)){
			timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", SignalMetricType.metric_cci, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalCCI))));
		}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_macd)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MACD", SignalMetricType.metric_macd, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalMACD))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_storsi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal STORSI", SignalMetricType.metric_storsi, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalSTORSI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_rsi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal RSI", SignalMetricType.metric_rsi, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalRSI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_trix)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal TRIX", SignalMetricType.metric_trix, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalTRIX))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_mfi)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MFI", SignalMetricType.metric_mfi, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalMFI))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_roc)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ROC", SignalMetricType.metric_roc, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalROC))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_willr)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal WILLR", SignalMetricType.metric_willr, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalWILLR))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_uo)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal UO", SignalMetricType.metric_uo, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalUO))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_ar_up)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal AR", SignalMetricType.metric_ar_up, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalARUp))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_ar_down)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal AR", SignalMetricType.metric_ar_down, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalARDown))));}
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_sar)){timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal SAR", SignalMetricType.metric_sar, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfSignalSAR))));}
				
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_crossover)){
			timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Indicator EMA First", SignalMetricType.metric_crossover, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfIndicatorEMAFirst))));
			timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Indicator EMA Second", SignalMetricType.metric_crossover, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfIndicatorEMASecond))));
		}
		
		if (strategyOptions.listOfSignalMetricType.contains(SignalMetricType.metric_sar)){
			timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Indicator SAR", SignalMetricType.metric_sar, ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfIndicatorSAR))));
		}

//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("DI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfCCI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfMACD))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfRSI))));
		timeSeriesCollectionForPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price ($)", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPriceClose))));
		if (listOfValue.size() != 0){
			timeSeriesCollectionForValue.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Value (%)", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfValue))));
		}
		
		if (listOfYield.size() != 0){
			timeSeriesCollectionForYield.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Yield (%)", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfYield))));
		}
		
		if (listOfLongEntryAtPrice.size() != 0){
			timeSeriesCollectionForLongEntryAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Long Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfLongEntryAtPrice))));
		}
		
		if (listOfShortEntryAtPrice.size() != 0){
			timeSeriesCollectionForShortEntryAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Short Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfShortEntryAtPrice))));
		}
		
		if (listOfReEntryAtPrice.size() != 0){
			timeSeriesCollectionForReEntryAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Reentry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfReEntryAtPrice))));
		}
		
		if (listOfLongExitAtPrice.size() != 0){
			timeSeriesCollectionForLongExitAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfLongExitAtPrice))));
		}
		
		if (listOfShortExitAtPrice.size() != 0){
			timeSeriesCollectionForShortExitAtPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfShortExitAtPrice))));
		}
		
		if (listOfDebugAlpha.size() != 0){
			timeSeriesCollectionForDebug.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Debug", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDebugAlpha))));
		}
		
		
//		for (int i=0; i<listOfSignalCCI.size(); i++){
//			Co.print("," + listOfSignalCCI.get(i));
//		}
		
		new CombinedLineChart().new LineChartDisplay(title, 
			dataSetForDefaultHighLowDataset,
			algorithmBase,
			new TimeSeriesTypePair(TimeSeriesType.type_signals, timeSeriesCollectionForSignals),
			new TimeSeriesTypePair(TimeSeriesType.type_price, timeSeriesCollectionForPrice), 
			new TimeSeriesTypePair(TimeSeriesType.type_value, timeSeriesCollectionForValue),
			new TimeSeriesTypePair(TimeSeriesType.type_yield, timeSeriesCollectionForYield),
			new TimeSeriesTypePair(TimeSeriesType.type_long_entry_price, timeSeriesCollectionForLongEntryAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_short_entry_price, timeSeriesCollectionForShortEntryAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_reentry_price, timeSeriesCollectionForReEntryAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_long_exit_price, timeSeriesCollectionForLongExitAtPrice),
			new TimeSeriesTypePair(TimeSeriesType.type_short_exit_price, timeSeriesCollectionForShortExitAtPrice),
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
