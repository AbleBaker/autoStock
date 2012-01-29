package com.autoStock.exchange.request;

import com.autoStock.MainClient;
import com.autoStock.trading.results.ExResultHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultRowHistoricalData;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHistoricalData {
	public RequestHolder requestHolder;
	public RequestHistoricalDataListener requestHistoricalDataListener;
	public TypeHistoricalData typeHistoricalData;
	public ExResultSetHistoricalData exResultSetHistoricalData;
	
	public RequestHistoricalData(RequestHolder requestHolder, RequestHistoricalDataListener requestListener, TypeHistoricalData typeHistoricalData){
		this.requestHolder = requestHolder;
		this.requestHistoricalDataListener = requestListener;
		this.typeHistoricalData = typeHistoricalData;
		this.exResultSetHistoricalData = new ExResultHistoricalData(). new ExResultSetHistoricalData(typeHistoricalData);
		this.requestHolder.caller = this;
		
		MainClient.ibExchangeInstance.getHistoricalPrice(typeHistoricalData, requestHolder);
	}
	
	public void addResult(ExResultRowHistoricalData exResultRowHistoricalData){
		this.exResultSetHistoricalData.listOfExResultRowHistoricalData.add(exResultRowHistoricalData);
	}
}
