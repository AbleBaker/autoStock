/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;

import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ResultsTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ChartForAlgorithmTest {
	private String title;
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
	public ArrayList<Integer> listOfSignalTotal = new ArrayList<Integer>();
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
	
	public ChartForAlgorithmTest(String title){
		this.title = title;
	}
	
	public static enum TimeSeriesType {
		type_signal_total("Signal Total"),
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
		TimeSeriesCollection timeSeriesCollectionForSignalTotal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForSignals = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForValue = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForEntryAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExitAtPrice = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForEntryAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExitAtSignal = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForDebug = new TimeSeriesCollection();
		
		DefaultHighLowDataset dataSetForDefaultHighLowDataset = new DefaultHighLowDataset("Series 1", ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPriceHigh), ArrayTools.getArrayFromListOfDouble(listOfPriceLow), ArrayTools.getArrayFromListOfDouble(listOfPriceOpen), ArrayTools.getArrayFromListOfDouble(listOfPriceClose), ArrayTools.getArrayFromListOfDouble(listOfSizeVolume));
		
		timeSeriesCollectionForSignalTotal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal Total", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTotal))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalPPC))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal DI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalDI))));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalCCI))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MACD", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMACD))));
		//timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal STORSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalSTORSI))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal RSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalRSI))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal TRIX", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTRIX))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MFI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMFI))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ROC", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalROC))));
		timeSeriesCollectionForSignals.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal WILLR", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalWILLR))));
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
			timeSeriesCollectionForEntryAtSignal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfEntryAtSignal))));
			timeSeriesCollectionForExitAtSignal.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfExitAtSignal))));
		}
		
		if (listOfDebugAlpha.size() != 0){
			timeSeriesCollectionForDebug.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Debug", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDebugAlpha))));
		}
		
		new CombinedLineChart().new LineChartDisplay(title, 
			dataSetForDefaultHighLowDataset,
			new TimeSeriesTypePair(TimeSeriesType.type_signal_total, timeSeriesCollectionForSignalTotal), 
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
