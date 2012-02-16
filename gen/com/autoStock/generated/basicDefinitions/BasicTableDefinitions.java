package com.autoStock.generated.basicDefinitions;

import java.util.Date;
public class BasicTableDefinitions {

	public class DbExchange {
		public long id;
		public String exchange;
		public String currency;
	}

	public static DbExchange dbExchange = new BasicTableDefinitions(). new DbExchange();

	public class DbMarketOrder {
		public long id;
		public String symbol;
		public String orderType;
		public int quantity;
		public float priceLimit;
		public float priceStop;
		public Date goodAfterDate;
		public Date goodUntilDate;
		public float priceAverageFill;
	}

	public static DbMarketOrder dbMarketOrder = new BasicTableDefinitions(). new DbMarketOrder();

	public class DbStockHistoricalPrice {
		public long id;
		public String symbol;
		public float priceOpen;
		public float priceHigh;
		public float priceLow;
		public float priceClose;
		public int sizeVolume;
		public Date dateTime;
	}

	public static DbStockHistoricalPrice dbStockHistoricalPrice = new BasicTableDefinitions(). new DbStockHistoricalPrice();

	public class DbSymbol {
		public long id;
		public String symbol;
		public String exchange;
	}

	public static DbSymbol dbSymbol = new BasicTableDefinitions(). new DbSymbol();

}
