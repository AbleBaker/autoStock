/**
 * 
 */
package com.autoStock.exchange.request.listener;

import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultRealtimeData.ExResultSetRealtimeData;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public interface RequestRealtimeDataListener {
		public void failed(RequestHolder requestHolder);
		public void receiveQuoteSlice(RequestHolder requestHolder, TypeQuoteSlice typeQuoteSlice);
		public void completed(RequestHolder requestHolder, ExResultSetRealtimeData exResultSetRealtimeData);
}
