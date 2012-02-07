/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.listener.RequestHistoricalDataListener;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.trading.results.ExResultHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultRowHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.results.ExResultMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypeMarketData;

/**
 * @author Kevin Kowalewski
 * 
 */
public class RequestMarketData {
	public RequestHolder requestHolder;
	public RequestMarketDataListener requestMarketDataListener;
	public ExResultSetMarketData exResultSetMarketData;
	public TypeMarketData typeMarketData;

	public RequestMarketData(RequestHolder requestHolder, RequestMarketDataListener requestMarketDataListener, TypeMarketData typeMarketData) {
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.requestMarketDataListener = requestMarketDataListener;
		this.typeMarketData = typeMarketData;
		this.exResultSetMarketData = new ExResultMarketData(). new ExResultSetMarketData(typeMarketData);
		
		ExchangeController.getIbExchangeInstance().getMarketData(typeMarketData, requestHolder);
	}
	
	public synchronized void addResult(ExResultRowMarketData exResultRowMarketData){
		this.exResultSetMarketData.listOfExResultRowMarketData.add(exResultRowMarketData);
	}
}
