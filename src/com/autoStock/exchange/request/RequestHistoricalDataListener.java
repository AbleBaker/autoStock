/**
 * 
 */
package com.autoStock.exchange.request;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestHistoricalDataListener {
	public void completed(RequestHolder requestHolder);
	public void failed(RequestHolder requestHolder);
}
