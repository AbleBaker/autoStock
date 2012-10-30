/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.TimeSeriesCollection;

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
	
	public ArrayList<Double> listOfPrice = new ArrayList<Double>();
	public ArrayList<Double> listOfValue = new ArrayList<Double>();
	public ArrayList<Double> listOfEntry = new ArrayList<Double>();
	public ArrayList<Double> listOfExit = new ArrayList<Double>();
	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	
	public ArrayList<Double> listOfMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfDI = new ArrayList<Double>();
	public ArrayList<Double> listOfCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfRSI = new ArrayList<Double>();
	
	public ChartForAlgorithmTest(String title){
		this.title = title;
	}
	
	public static enum TimeSeriesType {
		type_signal_total("Signal Total"),
		type_signals("Signals"),
		type_price("Price"),
		type_value("Value"),
		type_entry("Entry"),
		type_exit("Exit"),
		
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
		TimeSeriesCollection timeSeriesCollectionForEntry = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollectionForExit = new TimeSeriesCollection();
		
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
		timeSeriesCollectionForPrice.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPrice))));
		if (listOfValue.size() != 0){
			timeSeriesCollectionForValue.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfValue))));
		}
		
		if (listOfEntry.size() != 0 && listOfExit.size() != 0){
			timeSeriesCollectionForEntry.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Entry", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfEntry))));
			timeSeriesCollectionForExit.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Exit", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfExit))));
		}
		
		new CombinedLineChart().new LineChartDisplay(title, 
			new TimeSeriesTypePair(TimeSeriesType.type_signal_total, timeSeriesCollectionForSignalTotal), 
			new TimeSeriesTypePair(TimeSeriesType.type_signals, timeSeriesCollectionForSignals), 
			new TimeSeriesTypePair(TimeSeriesType.type_price, timeSeriesCollectionForPrice), 
			new TimeSeriesTypePair(TimeSeriesType.type_value, timeSeriesCollectionForValue),
			new TimeSeriesTypePair(TimeSeriesType.type_entry, timeSeriesCollectionForEntry),
			new TimeSeriesTypePair(TimeSeriesType.type_exit, timeSeriesCollectionForExit)
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
