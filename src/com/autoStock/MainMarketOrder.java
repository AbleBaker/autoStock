package com.autoStock;

import com.autoStock.internal.Global;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainMarketOrder {
	public MainMarketOrder(Exchange exchange, PositionType positionType, String symbol, int units) {
		Global.callbackLock.requestLock();
		Position position = new Position(positionType, units, new Symbol(symbol), exchange, "STK", units);
	}
}
