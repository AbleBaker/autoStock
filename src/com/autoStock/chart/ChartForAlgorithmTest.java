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
	public ArrayList<Integer> listOfSignalPPC = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalDI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalCCI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalMACD = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalSTORSI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalRSI = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalTRIX = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalTotal = new ArrayList<Integer>();
	//public ArrayList<Integer> listOfSignalSTORSID = new ArrayList<Integer>();
	
	public ArrayList<Double> listOfPrice = new ArrayList<Double>();
	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	
	public ArrayList<Double> listOfMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfDI = new ArrayList<Double>();
	public ArrayList<Double> listOfCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfRSI = new ArrayList<Double>();

	public void display() {
		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection3 = new TimeSeriesCollection();
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalPPC))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal DI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalDI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalCCI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal MACD", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalMACD))));
		//timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal STORSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalSTORSI))));
		//timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal RSI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalRSI))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal TRIX", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalTRIX))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal Total", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalTotal))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("DI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfDI))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfCCI))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfMACD))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfRSI))));
		timeSeriesCollection3.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfPrice))));
		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2, timeSeriesCollection3);
		// //new LineChart(). new LineChartDisplay(timeSeriesCollection1);
	}
}
