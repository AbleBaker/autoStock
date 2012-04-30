/**
 * 
 */
package com.autoStock.display;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.exchange.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.trading.types.TypeMarketData;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DisplayMarketData {
	
	private TypeMarketData typeMarketData;
	
	public DisplayMarketData(TypeMarketData typeMarketData){
		this.typeMarketData = typeMarketData;
	}
	
	public void display(){
		new RequestMarketData(new RequestHolder(null), new RequestMarketDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
				Co.println("Completed!");
				
			}

			@Override
			public void receiveQuoteSlice(RequestHolder requestHolder, TypeQuoteSlice typeQuoteSlice) {
				// TODO Auto-generated method stub
				
			}
		}, typeMarketData, 0);
	}
}
