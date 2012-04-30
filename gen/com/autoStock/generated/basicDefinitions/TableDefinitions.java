package com.autoStock.generated.basicDefinitions;

import java.util.Date;

import com.autoStock.types.basic.Time;
public class TableDefinitions {

	public static class DbExchange {
		public long id;
		public String exchange;
		public String currency;
		public Time timeOpen;
		public Time timeClose;
		public Time timeOffset;
	}

	public static DbExchange dbExchange = new TableDefinitions.DbExchange();

	public static class DbMarketOrder {
		public long id;
		public String symbol;
		public String orderType;
		public int quantity;
		public double priceLimit;
		public double priceStop;
		public Date goodAfterDate;
		public Date goodUntilDate;
		public double priceAverageFill;
	}

	public static DbMarketOrder dbMarketOrder = new TableDefinitions.DbMarketOrder();

	public static class DbStockHistoricalPrice {
		public long id;
		public String symbol;
		public double priceOpen;
		public double priceHigh;
		public double priceLow;
		public double priceClose;
		public int sizeVolume;
		public Date dateTime;
	}

	public static DbStockHistoricalPrice dbStockHistoricalPrice = new TableDefinitions.DbStockHistoricalPrice();

	public static class DbSymbol {
		public long id;
		public String symbol;
		public String exchange;
		public String description;
	}

	public static DbSymbol dbSymbol = new TableDefinitions.DbSymbol();

}
