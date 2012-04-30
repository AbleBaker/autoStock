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
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalType;
import com.autoStock.trading.types.TypePosition;

/**
 * @author Kevin Kowalewski
 *
 */
public class RequestMarketOrder {
	private ExResultSetMarketOrder exResultSetMarketOrder;
	private RequestHolder requestHolder;
	private TypePosition typePosition;
	
	public RequestMarketOrder(RequestHolder requestHolder, TypePosition typePosition, PositionType positionType){
		this.requestHolder = requestHolder;
		this.requestHolder.caller = this;
		this.typePosition = typePosition;
		this.exResultSetMarketOrder = new ExResultMarketOrder(). new ExResultSetMarketOrder(typePosition);
		
		if (positionType == PositionType.position_buy){
			ExchangeController.getIbExchangeInstance().placeBuyOrder(typePosition, requestHolder);
		}else if (positionType == PositionType.position_sell){
			ExchangeController.getIbExchangeInstance().placeSellOrder(typePosition, requestHolder);
		}else if (positionType == PositionType.position_short){
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
