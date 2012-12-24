package com.autoStock;

import com.autoStock.exchange.request.RequestMarketSymbolData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketSymbolDataListener;
import com.autoStock.exchange.results.ExResultMarketSymbolData.ExResultSetMarketSymbolData;
import com.autoStock.internal.Global;
import com.autoStock.order.OrderManager;
import com.autoStock.order.OrderDefinitions.OrderMode;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.ListenerOfPositionStatusChange;
import com.autoStock.position.PositionManager;
import com.autoStock.position.PositionValueTable;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Period;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.MarketSymbolData;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainMarketOrder implements ListenerOfPositionStatusChange {
	private RequestMarketSymbolData requestMarketSymbolData;
	
	public MainMarketOrder(final Exchange exchange, final PositionType positionType, final String symbol, final int units) {
		Global.callbackLock.requestLock();
		PositionManager.getInstance().orderMode = OrderMode.mode_exchange;
		
		requestMarketSymbolData = new RequestMarketSymbolData(new RequestHolder(null), new RequestMarketSymbolDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void receiveQuoteSlice(RequestHolder requestHolder, QuoteSlice quoteSlice) {
				Co.println("--> X: " + quoteSlice.priceClose);
				requestMarketSymbolData.cancel();
				
				Position position = new Position(positionType, units, new Symbol(symbol), exchange, "STK", quoteSlice.priceClose, null);
				position.setPositionListener(MainMarketOrder.this);
				position.executePosition();
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketSymbolData exResultSetMarketData) {
				
			}
		}, new MarketSymbolData(exchange, new Symbol(symbol), "STK") , Resolution.sec_5.seconds);
	}

	@Override
	public void positionStatusChanged(Position position) {
		Co.println("\n\n--> Position status changed: " + position.positionType.name());
		new PositionValueTable().printTable(position, position.getPositionValue());
	}
}
