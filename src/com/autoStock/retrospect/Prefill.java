package com.autoStock.retrospect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.signal.SignalControl;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 *
 */
public class Prefill {
	private Symbol symbol;
	private Exchange exchange;
	private PrefillMethod prefillMethod;
	private Calendar calendarForStart = GregorianCalendar.getInstance();
	private Calendar calendarForEnd = GregorianCalendar.getInstance();
	
	public static enum PrefillMethod {
		method_database,
		method_broker,
	}
	
	public Prefill(Symbol symbol, Exchange exchange, PrefillMethod prefillMethod){
		this.symbol = symbol;
		this.exchange = exchange;
		this.prefillMethod = prefillMethod;
	}
	
	public void prefillAlgorithm(AlgorithmBase algorithmBase){
		if (prefillMethod == PrefillMethod.method_database){
			setupPrefill(algorithmBase.startingDate, algorithmBase.exchange.timeOpenForeign, algorithmBase.exchange.timeCloseForeign);
						
			HistoricalData historicalData = new HistoricalData(exchange, symbol, calendarForStart.getTime(), calendarForEnd.getTime(), Resolution.min);
			ArrayList<QuoteSlice> listOfQuoteSlice = QuoteSliceTools.getListOfQuoteSlice((ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, QueryArgs.symbol.setValue(historicalData.symbol.symbolName), QueryArgs.startDate.setValue(DateTools.getSqlDate(historicalData.startDate)), QueryArgs.endDate.setValue(DateTools.getSqlDate(historicalData.endDate))));

			algorithmBase.listOfQuoteSlice.addAll(listOfQuoteSlice);
			
			Co.println("--> Added: " + algorithmBase.listOfQuoteSlice.size());
		}
	}
	
	public void setupPrefill(Date startingDate, Time timeOpenForeign, Time timeCloseForeign){
		calendarForStart.setTime(startingDate);
		calendarForStart.set(Calendar.SECOND, 0);
		calendarForEnd.setTime(startingDate);
		
		Time time = DateTools.getTimeUntilTime(DateTools.getTimeFromDate(startingDate), timeOpenForeign);
		
		int minutesFromDatabase = (SignalControl.periodLengthStart.value - (time.getSeconds() / 60));
		int minutesFromBroker = time.getSeconds() / 60; 
		
		Co.println("--> Minutes from database, broker: " + minutesFromDatabase + ", " + minutesFromBroker);
		
		if (minutesFromBroker == 0){
			Co.println("--> Only database");
			calendarForStart.add(Calendar.DAY_OF_MONTH, -1);
			calendarForStart.set(Calendar.HOUR, timeCloseForeign.hours);
			calendarForStart.set(Calendar.MINUTE, (minutesFromDatabase -1) * -1);
			
			calendarForEnd = (Calendar) calendarForStart.clone();
			calendarForEnd.set(Calendar.HOUR, timeCloseForeign.hours);
			calendarForEnd.set(Calendar.MINUTE, timeCloseForeign.minutes);
		}else if (minutesFromDatabase == 0 || minutesFromBroker >= SignalControl.periodLengthStart.value){
			throw new UnsupportedOperationException();
		}else{
			Co.println("--> Mixed");
			throw new UnsupportedOperationException();
		}
		
		Co.println("--> Prefill: " + DateTools.getPrettyDate(calendarForStart.getTime()) + ", " + DateTools.getPrettyDate(calendarForEnd.getTime()));
	}
}
