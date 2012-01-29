/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestHistoricalData {
	public RequestHolder requestHolder;
	public RequestHistoricalDataListener requestHistoricalDataListener;
	public TypeHistoricalData typeHistoricalData;
	
	public RequestHistoricalData(RequestHolder requestHolder, RequestHistoricalDataListener requestListener, TypeHistoricalData typeHistoricalData){
		this.requestHolder = requestHolder;
		this.requestHistoricalDataListener = requestListener;
		this.typeHistoricalData = typeHistoricalData;
	}
}
