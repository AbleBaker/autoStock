package com.autoStock;

import com.autoStock.internal.Global;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionStatusListener;
import com.autoStock.position.PositionValueTable;
import com.autoStock.trading.types.Position;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class MainMarketOrder implements PositionStatusListener {
	public MainMarketOrder(Exchange exchange, PositionType positionType, String symbol, int units) {
		Global.callbackLock.requestLock();
		Position position = new Position(positionType, units, new Symbol(symbol), exchange, "STK", 0);
		position.setPositionListener(this);
		position.executePosition();
	}

	@Override
	public void positionStatusChange(Position position) {
		Co.println("\n\n--> Position status changed: " + position.positionType.name());
		new PositionValueTable().printTable(position, position.getPositionValue());
	}
}
