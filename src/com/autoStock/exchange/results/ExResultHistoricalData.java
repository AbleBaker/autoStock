package com.autoStock.exchange.results;

import java.util.ArrayList;

import com.autoStock.trading.types.HistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */

public class ExResultHistoricalData {
	public class ExResultSetHistoricalData {
		public HistoricalData typeHistoricalData;
		public ArrayList<ExResultRowHistoricalData> listOfExResultRowHistoricalData = new ArrayList<ExResultRowHistoricalData>();
		
		public ExResultSetHistoricalData(HistoricalData typeHistoricalData){
			this.typeHistoricalData = typeHistoricalData;
		}
	}
	
	public class ExResultRowHistoricalData {
		public long date;
		public double price;
		public int volume;
		public int count;
		
		public ExResultRowHistoricalData(long date, double price, int volume, int count){
			this.date = date*1000;
			this.price = price;
			this.volume = volume;
			this.count = count;
		}
	}
}
