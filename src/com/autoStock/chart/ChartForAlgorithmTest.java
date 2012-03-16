/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.TimeSeriesCollection;

import com.autoStock.analysis.Analysis;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.ResultsTools;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ChartForAlgorithmTest {
	public ArrayList<Integer> listOfSignalPPC = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalADX = new ArrayList<Integer>();
	public ArrayList<Integer> listOfSignalCCI = new ArrayList<Integer>();
	
	public ArrayList<Double> listOfPrice = new ArrayList<Double>();
	public ArrayList<Date> listOfDate = new ArrayList<Date>();
	
	public ArrayList<Double> listOfMACD = new ArrayList<Double>();
	public ArrayList<Double> listOfADX = new ArrayList<Double>();
	public ArrayList<Double> listOfCCI = new ArrayList<Double>();
	public ArrayList<Double> listOfRSI = new ArrayList<Double>();

	public void display() {
		TimeSeriesCollection timeSeriesCollection1 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection2 = new TimeSeriesCollection();
		TimeSeriesCollection timeSeriesCollection3 = new TimeSeriesCollection();
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal PPC ", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalPPC))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal ADX", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalADX))));
		timeSeriesCollection1.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Signal CCI", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertIntegers(listOfSignalCCI))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("ADX Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfADX))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("CCI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfCCI))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("MACD Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfMACD))));
		timeSeriesCollection2.addSeries(new ChartDataFiller().getTimeSeriesFromResults("RSI Value", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfRSI))));
		timeSeriesCollection3.addSeries(new ChartDataFiller().getTimeSeriesFromResults("Price", ResultsTools.getResultsAsListOfBasicTimeValuePair(ArrayTools.convertDates(listOfDate), ArrayTools.convertDoubles(listOfPrice))));
		new CombinedLineChart().new LineChartDisplay(timeSeriesCollection1, timeSeriesCollection2, timeSeriesCollection3);
		// //new LineChart(). new LineChartDisplay(timeSeriesCollection1);
	}
}
