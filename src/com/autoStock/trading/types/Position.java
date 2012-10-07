/**
 * 
 */
package com.autoStock.trading.types;

import java.io.ObjectInputStream.GetField;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.order.OrderDefinitions.OrderStatus;
import com.autoStock.order.OrderDefinitions.OrderType;
import com.autoStock.order.OrderStatusListener;
import com.autoStock.position.PositionCallback;
import com.autoStock.position.PositionStatusListener;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class Position implements OrderStatusListener {
	private int units;
	public Symbol symbol;
	public Exchange exchange;
	public String securityType;
	private double firstKnownPrice;
	public double lastKnownPrice;
	public PositionType positionType = PositionType.position_none;
	private ArrayList<Order> listOfOrder = new ArrayList<Order>();
	private PositionStatusListener positionStatusListener;
	private Lock lock = new Lock();

	public Position(PositionType positionType, int units, Symbol symbol, Exchange exchange, String securityType, double currentPrice) {
		this.positionType = positionType;
		this.units = units;
		this.symbol = symbol;
		this.exchange = exchange;
		this.securityType = securityType;
		this.firstKnownPrice = currentPrice;
		this.lastKnownPrice = currentPrice;
	}

	public void setPositionListener(PositionStatusListener positionStatusListener) {
		this.positionStatusListener = positionStatusListener;
	}

	public void executePosition() {
		synchronized (lock){
			if (positionType == PositionType.position_long_entry) {
				Order order = new Order(symbol, exchange, this, OrderType.order_long_entry, units, lastKnownPrice, this);
				order.executeOrder();
				listOfOrder.add(order);
			} else if (positionType == PositionType.position_short_entry) {
				Order order = new Order(symbol, exchange, this, OrderType.order_short_entry, units, lastKnownPrice, this);
				order.executeOrder();
				listOfOrder.add(order);
			} else if (positionType == PositionType.position_long_exit) {
				Order order = new Order(symbol, exchange, this, OrderType.order_long_exit, getUnitsFilled(), lastKnownPrice, this);
				order.executeOrder();
			} else if (positionType == PositionType.position_short_exit) {
				Order order = new Order(symbol, exchange, this, OrderType.order_short_exit, getUnitsFilled(), lastKnownPrice, this);
				order.executeOrder();
			} else {
				throw new IllegalStateException("PositionType: " + positionType.name());
			}
		}
		
		Co.println("--> Executed position: " + positionType.name());
	}
	
	public void cancelEntry(){
		synchronized (lock){
			if (positionType == PositionType.position_long_entry || positionType == PositionType.position_short_entry){
				positionType = PositionType.position_cancelling; 
			}else{
				throw new IllegalSelectorException();
			}
		
			for (Order order : listOfOrder){
				order.cancelOrder();
			}
			
			positionType = PositionType.position_canceled;
		}		
	}

	public int getUnitsFilled() {
		synchronized (lock) {
			int units = 0;
			for (Order order : listOfOrder) {
				if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short){
					units += order.getUnitsFilled();
				}
			}
			return units;
		}
	}

	public int getUnitsRequested() {
		synchronized (lock) {
			int units = 0;

			for (Order order : listOfOrder) {
				if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short){
					units += order.getUnitsRequested();
				}
			}

			return units;
		}
	}

	public double getAveragePrice() {
		return 1;
	}

	public boolean isFilled() {
		if (positionType == PositionType.position_long || positionType == PositionType.position_short) {

			Co.println("--> Position is filled: " + getUnitsRequested() + ", " + getUnitsFilled() + ", " + firstKnownPrice + ", " + lastKnownPrice);

			return true;
		}
		return false;
	}

	public double getPositionEntryPrice(boolean includeTransactionFees) {
		return MathTools.round(units * firstKnownPrice + (includeTransactionFees ? Account.getInstance().getTransactionCost(units, firstKnownPrice) : 0));
	}

	public double getPositionCurrentPrice(boolean includeTransactionFees) {
		return MathTools.round((units * lastKnownPrice) + (includeTransactionFees ? Account.getInstance().getTransactionCost(units, lastKnownPrice) : 0));
	}

	// TODO: Fix this
	public double getPositionEntryValue(boolean includeTransactionFees) {
		return MathTools.round(units * firstKnownPrice - (includeTransactionFees ? Account.getInstance().getTransactionCost(units, firstKnownPrice) : 0));
	}

	// TODO: fix this too
	public double getPositionCurrentValue(boolean includeTransactionFees) {
		return MathTools.round((units * lastKnownPrice) - (includeTransactionFees ? Account.getInstance().getTransactionCost(units, lastKnownPrice) : 0));
	}

	public double getPositionProfitLossAfterComission() {
		return MathTools.round(getPositionCurrentValue(true) - getPositionEntryPrice(true));
	}

	@Override
	public void orderStatusChanged(Order order, OrderStatus orderStatus) {
		Co.println("--> Received order status change: " + orderStatus.name());
		if (orderStatus == OrderStatus.status_filled) {
			if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short){
				PositionCallback.setPositionSuccess(this);
			}else if (order.orderType == OrderType.order_long_exited || order.orderType == OrderType.order_short_exited){
				positionType = PositionType.position_exited;
			}else{
				throw new IllegalStateException();
			}
			PositionCallback.affectBankBalance(order);
			positionStatusListener.positionStatusChange(this);	
		}else if (orderStatus == OrderStatus.status_cancelled){
			positionType = PositionType.position_failed;
		}
	}
}
