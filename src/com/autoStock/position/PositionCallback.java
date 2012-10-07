package com.autoStock.position;

import com.autoStock.finance.Account;
import com.autoStock.order.OrderDefinitions.OrderType;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.trading.types.Order;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionCallback {
	public static void setPositionSuccess(Position position) throws IllegalAccessError {
		if (position.positionType == PositionType.position_long_entry){position.positionType = PositionType.position_long;}
		else if (position.positionType == PositionType.position_short_entry){position.positionType = PositionType.position_short;}
		else if (position.positionType == PositionType.position_long_exit){position.positionType = PositionType.position_exited;}
		else if (position.positionType == PositionType.position_short_exit){position.positionType = PositionType.position_exited;}
		else throw new UnsupportedOperationException("No condition matched PositionType: " + position.positionType.name());
	}

	public static void affectBankBalance(Order order){
		if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short){
			Account.getInstance().changeAccountBalance(order.getValue(), Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getPrice()));
		}else if (order.orderType == OrderType.order_long_exited || order.orderType == OrderType.order_short_exited){
			Account.getInstance().changeAccountBalance(-1 * order.getValue(), Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getPrice()));
		}else{
			throw new IllegalStateException("Order type is: " + order.orderType.name());
		}
	}
}
