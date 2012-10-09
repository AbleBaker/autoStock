package com.autoStock.position;

import com.autoStock.Co;
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
//		Co.println("Affecting bank balance: " + order.symbol.symbolName + ", " + order.getOrderValue().valueFilled);
		if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short){
//			Co.println("--> Changing bank balance: " + order.orderType.name() + ", " + (-1 * order.getOrderValue().valueFilled) + ", " + Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getOrderValue().unitPriceFilled));
			
			Account.getInstance().changeAccountBalance(-1 * order.getOrderValue().valueFilled, Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getOrderValue().unitPriceFilled));
		}else if (order.orderType == OrderType.order_long_exited || order.orderType == OrderType.order_short_exited){
//			Co.println("--> Changing bank balance: " + order.orderType.name() + ", " + (order.getOrderValue().valueFilled) + ", " + Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getOrderValue().unitPriceFilled));
			
			Account.getInstance().changeAccountBalance(order.getOrderValue().valueFilled, Account.getInstance().getTransactionCost(order.getUnitsFilled(), order.getOrderValue().unitPriceFilled));
		}else{
			throw new IllegalStateException("Order type is: " + order.orderType.name());
		}
	}
}
