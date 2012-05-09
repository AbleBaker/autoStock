package com.autoStock.types;

import java.util.ArrayList;

import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.Currency.CurrencyDefinitions;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbExchange;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 *
 */
public class Exchange {
	public String exchange;
	public CurrencyDefinitions currency;
	public Time timeOpen;
	public Time timeClose;
	public Time timeOffset;
	
	public Exchange(String exchange){
		ArrayList<DbExchange> listOfQrExchange = (ArrayList<DbExchange>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_exchange_info, QueryArgs.exchange.setValue(exchange));
		DbExchange queryResultOfExchange = listOfQrExchange.get(0);
		this.exchange = exchange;
		this.currency = CurrencyDefinitions.valueOf(queryResultOfExchange.currency);
		this.timeOpen = queryResultOfExchange.timeOpen;
		this.timeClose = queryResultOfExchange.timeClose;
		this.timeOffset = queryResultOfExchange.timeOffset;
	}
	
	public Exchange(String exchange, CurrencyDefinitions currency, Time timeOpen, Time timeClose) {
		this.exchange = exchange;
		this.currency = currency;
		this.timeOpen = timeOpen;
		this.timeClose = timeClose;
	}
}
