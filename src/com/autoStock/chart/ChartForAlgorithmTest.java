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
	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	
	public ArrayList<Double> listOfMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfDI = new ArrayList<Double>();
	public ArrayList<Double> listOfCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfRSI = new ArrayList<Double>();
	
	public ChartForAlgorithmTest(String title){
		this.title = title;
	}

	public void display() {
		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
//		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection3 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection4 = new TimeSeriesCollection();
		timeSeriesCollection4.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal Total", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTotal))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalPPC))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal DI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalDI))));
//		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalCCI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MACD", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMACD))));
		//timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal STORSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalSTORSI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal RSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalRSI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal TRIX", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalTRIX))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MFI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalMFI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ROC", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalROC))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal WILLR", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfInt(listOfSignalWILLR))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("DI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfDI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfCCI))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfMACD))));
//		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfRSI))));
		timeSeriesCollection3.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.getArrayFromListOfDates(listOfDate), ArrayTools.getArrayFromListOfDouble(listOfPrice))));
		new CombinedLineChart().new LineChartDisplay(title, timeSeriesCollection4, timeSeriesCollection1, timeSeriesCollection3);
		// //new LineChart(). new LineChartDisplay(timeSeriesCollection1);
	}
}
