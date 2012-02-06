/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.listener.RequestHistoricalDataListener;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypeMarketData;

/**
 * @author Kevin Kowalewski
 * 
 */
public class RequestMarketData {
	public RequestHolder requestHolder;
	public RequestMarketDataListener requestMarketDataListener;
	public TypeMarketData typeMarketData;

	public RequestMarketData(RequestHolder requestHolder, RequestMarketDataListener requestMarketDataListener, TypeMarketData typeMarketData) {
		this.requestHolder = requestHolder;
		this.requestMarketDataListener = requestMarketDataListener;
		this.typeMarketData = typeMarketData;
		
		ExchangeController.getIbExchangeInstance().getMarketData(typeMarketData, requestHolder);
	}
}
