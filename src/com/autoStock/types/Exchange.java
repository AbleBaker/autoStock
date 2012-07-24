package com.autoStock.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.exchange.ExchangeHelper;
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
	public Time timeOpen;
	public Time timeClose;
	public Time timeOffset;
	public String timeZone;
	
	public Date dateLocalOpen;
	public Date dateLocalClose;
	
	/**
	 * An exchange object. Fields like currency open and close times will automatically be filled from the database.
	 * @param name - The string name for the exchange
	 */
	public Exchange(String name){
		ArrayList<DbExchange> listOfQrExchange = (ArrayList<DbExchange>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_exchange_info, QueryArgs.exchange.setValue(name));
		DbExchange queryResultOfExchange = listOfQrExchange.get(0);
		this.name = name;
		currency = CurrencyDefinitions.valueOf(queryResultOfExchange.currency);
		timeOpen = queryResultOfExchange.timeOpen;
		timeClose = queryResultOfExchange.timeClose;
		timeOffset = queryResultOfExchange.timeOffset;
		timeZone = queryResultOfExchange.timeZone;
		dateLocalOpen = super.getLocalTimeFromForeignTime(timeOpen, timeZone);
		dateLocalClose = super.getLocalTimeFromForeignTime(timeClose, timeZone);
	}
	
	public boolean isOpen(){
		boolean isOpen = new Date().after(dateLocalOpen) && new Date().before(dateLocalClose);
		Co.println("--> Exchange opens at (foreign): " + DateTools.getPrettyDate(DateTools.getDateFromTime(timeOpen)));
		Co.println("--> Exchange closes at (foreign): " + DateTools.getPrettyDate(DateTools.getDateFromTime(timeClose)));
		
		Co.println("--> Exchange opens at (local): " + DateTools.getPrettyDate(dateLocalOpen));
		Co.println("--> Exchange closes at (local): " + DateTools.getPrettyDate(dateLocalClose));
		
		Co.println("--> Current local time is: " + DateTools.getPrettyDate(new Date()));		
		Co.println("--> Exchange is open: " + isOpen);
		
		return isOpen;
	}
}
