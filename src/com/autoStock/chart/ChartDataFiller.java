/**
 * 
 */
package com.autoStock.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

/**
 * @author Kevin Kowalewski
 *
 */
public class ChartDataFiller {
	public TimeSeries getTimeSeriesFromResults(String label, ArrayList<BasicTimeValuePair> listOfBasicTimeValuePair){
		TimeSeries timeSeries = new TimeSeries(label);
		
		for (BasicTimeValuePair basicTimeValuePair : listOfBasicTimeValuePair){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(basicTimeValuePair.date);
			if (Double.valueOf(basicTimeValuePair.value) != 0){
				timeSeries.add(new Minute(calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)), Double.valueOf(basicTimeValuePair.value));
			}
		}
		
		return timeSeries;
	}
	
	public class BasicTimeValuePair{
		public Date date;
		public String value;
		
		public BasicTimeValuePair(Date date, String value){
			this.date = date;
			this.value = value;
		}
	}
}
