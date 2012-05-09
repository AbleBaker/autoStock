/**
 * 
 */
package com.autoStock.exchange.request;

import com.autoStock.Co;
import com.autoStock.exchange.ExchangeController;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.results.ExResultMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultRowMarketOrder;
import com.autoStock.exchange.results.ExResultMarketOrder.ExResultSetMarketOrder;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestMarketOrder {
	private ExResultSetMarketOrder exResultSetMarketOrder;
	private RequestHolder requestHolder;
	private Position typePosition;
	
	public RequestMarketOrder(RequestHolder requestHolder, Position typePosition){
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.typePosition = typePosition;
		this.exResultSetMarketOrder = new ExResultMarketOrder(). new ExResultSetMarketOrder(typePosition);
		
		if (typePosition.positionType == PositionType.position_long_entry){
			ExchangeController.getIbExchangeInstance().placeBuyOrder(typePosition, requestHolder);
		}else if (typePosition.positionType == PositionType.position_long_exit){
			ExchangeController.getIbExchangeInstance().placeSellOrder(typePosition, requestHolder);
		}else if (typePosition.positionType == PositionType.position_short_entry){
			 ExchangeController.getIbExchangeInstance().placeShortOrder(typePosition, requestHolder);
		}else{
			throw new UnsupportedOperationException();
		}
	}
	
	public synchronized void addResult(ExResultRowMarketOrder exResultRowMarketOrder){
		this.exResultSetMarketOrder.listOfExResultRowMarketOrder.add(exResultRowMarketOrder);
	}
	
	public synchronized void finished(){
		Co.println("Finished market order...");
	}
}
