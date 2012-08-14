package com.autoStock;

import com.autoStock.exchange.request.RequestMarketOrder;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketOrderListener;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.internal.Global;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 *
 */
public class MainMarketOrder {
	public MainMarketOrder(Exchange exchange, PositionType positionType, String symbol, int units){
		
		Global.callbackLock.requestLock();
		
		Position position = new Position();
		position.positionType = positionType;
		position.units = units;
		position.symbol = symbol;
		RequestMarketOrder request = new RequestMarketOrder(new RequestHolder(new RequestMarketOrderListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void receivedOrder(RequestHolder requestHolder, ExResultRowMarketOrder exResultRowMarketOrder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketOrder exResultSetMarketOrder) {
				
			}

			@Override
			public void failed() {
				
			}
		}), position, exchange);
	}
}
