/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.request.listener.RequestRealtimeDataListener;
import com.autoStock.trading.results.ExResultMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.trading.results.ExResultRealtimeData;
import com.autoStock.trading.results.ExResultRealtimeData.ExResultRowRealtimeData;
import com.autoStock.trading.results.ExResultRealtimeData.ExResultSetRealtimeData;
import com.autoStock.trading.types.TypeMarketData;
import com.autoStock.trading.types.TypeRealtimeData;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestRealtimeData {
	public RequestHolder requestHolder;
	public RequestRealtimeDataListener requestRealtimeDataListener;
	public ExResultSetRealtimeData exResultSetRealtimeData;
	public TypeRealtimeData typeRealtimeData;

	public RequestRealtimeData(RequestHolder requestHolder, RequestRealtimeDataListener requestRealtimeDataListener, TypeRealtimeData typeRealtimeData) {
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.requestRealtimeDataListener = requestRealtimeDataListener;
		this.typeRealtimeData = typeRealtimeData;
		this.exResultSetRealtimeData = new ExResultRealtimeData(). new ExResultSetRealtimeData(typeRealtimeData);
		
		ExchangeController.getIbExchangeInstance().getRealtimeData(typeRealtimeData, requestHolder);
	}
	
	public synchronized void addResult(ExResultRowRealtimeData exResultRowRealtimeData){
		this.exResultSetRealtimeData.listOfExResultRowRealtimeData.add(exResultRowRealtimeData);
	}
}
