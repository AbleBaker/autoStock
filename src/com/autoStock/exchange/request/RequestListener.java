/**
 * 
 */
package com.autoStock.exchange.request;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestListener {
	public void completed(RequestHolder requestHolder);
	public void failed(RequestHolder requestHolder);
}
