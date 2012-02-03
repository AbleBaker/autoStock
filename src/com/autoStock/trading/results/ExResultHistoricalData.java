package com.autoStock.trading.results;

import java.util.ArrayList;

import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */

public class ExResultHistoricalData {
	
	public class ExResultSetHistoricalData {
		public TypeHistoricalData typeHistoricalData;
		public ArrayList<ExResultRowHistoricalData> listOfExResultRowHistoricalData = new ArrayList<ExResultRowHistoricalData>();
		
		public ExResultSetHistoricalData(TypeHistoricalData typeHistoricalData){
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
