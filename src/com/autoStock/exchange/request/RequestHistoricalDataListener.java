/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestHistoricalDataListener {
	public void failed(RequestHolder requestHolder);
	public void completed(RequestHolder requestHolder, ExResultSetHistoricalData exResultSetHistoricalData);
}
