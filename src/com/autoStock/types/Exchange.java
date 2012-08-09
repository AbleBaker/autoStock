package com.autoStock.types;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.exchange.ExchangeHelper;
import com.autoStock.exchange.ExchangeStatusListener.ExchangeState;
import com.autoStock.finance.Currency.CurrencyDefinitions;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbExchange;
import com.autoStock.tools.DateTools;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski 
 */

public class Exchange extends ExchangeHelper {
	public String name;
	public CurrencyDefinitions currency;
	public Time timeOpenForeign;
	public Time timeCloseForeign;
	public Time timeOffset;
	public Date dateOpenLocal;
	public Date dateCloseLocal;
	public String timeZone;
	public ExchangeState exchangeState = ExchangeState.status_unknown;
	public ExchangeDesignation exchangeDesignation;
	
	/**
	 * An exchange object. Fields like currency open and close times will automatically be filled from the database.
	 * @param name - The string name for the exchange
	 */
	public Exchange(String name){
		ArrayList<DbExchange> listOfQrExchange = (ArrayList<DbExchange>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_exchange_info, QueryArgs.exchange.setValue(name));
		DbExchange queryResultOfExchange = listOfQrExchange.get(0);
		this.name = name;
		currency = CurrencyDefinitions.valueOf(queryResultOfExchange.currency);
		timeOpenForeign = queryResultOfExchange.timeOpen;
		timeCloseForeign = queryResultOfExchange.timeClose;
		timeOffset = queryResultOfExchange.timeOffset;
		timeZone = queryResultOfExchange.timeZone;
		dateOpenLocal = DateTools.getLocalDateFromForeignTime(timeOpenForeign, timeZone);
		dateCloseLocal = DateTools.getLocalDateFromForeignTime(timeCloseForeign, timeZone);
		exchangeDesignation = ExchangeDesignation.valueOf(name);
	}
	
	public boolean isOpen(){
		return new Date().after(dateOpenLocal) && new Date().before(dateCloseLocal);
	}
	
	public boolean isClosed(){
		return !isOpen();
	}
}
