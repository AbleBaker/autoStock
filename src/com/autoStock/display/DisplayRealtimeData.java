/**
 * 
 */
package com.autoStock.display;

import com.autoStock.exchange.request.RequestRealtimeData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestRealtimeDataListener;
import com.autoStock.trading.results.ExResultRealtimeData.ExResultSetRealtimeData;
import com.autoStock.trading.types.TypeRealtimeData;

/**
 * @author Kevin Kowalewski
 *
 */
public class DisplayRealtimeData {

	public TypeRealtimeData typeRealtimeData;
	
	public DisplayRealtimeData(TypeRealtimeData typeRealtimeData) {
		this.typeRealtimeData = typeRealtimeData;
	}
	
	public void display(){
		new RequestRealtimeData(new RequestHolder(null), new RequestRealtimeDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetRealtimeData exResultSetRealtimeData) {}
		}, typeRealtimeData);
	}
}
