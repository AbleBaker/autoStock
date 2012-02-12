/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.trading.results.ExResultHistoricalData.ExResultSetHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestHistoricalDataListener {
	public void failed(RequestHolder requestHolder);
	public void completed(RequestHolder requestHolder, ExResultSetHistoricalData exResultSetHistoricalData);
}
