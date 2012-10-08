/**
 * 
 */
package com.autoStock.trading.types;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.order.OrderDefinitions.OrderStatus;
import com.autoStock.order.OrderDefinitions.OrderType;
import com.autoStock.order.OrderStatusListener;
import com.autoStock.position.PositionCallback;
import com.autoStock.position.PositionUtils;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionStatusListener;
import com.autoStock.position.PositionValue;
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
	private final double unitPriceFirstKnown;
	private double unitPriceLastKnown;
	public PositionType positionType = PositionType.position_none;
	private ArrayList<Order> listOfOrder = new ArrayList<Order>();
	private PositionStatusListener positionStatusListener;
	private PositionUtils positionUtils;
	private Lock lock = new Lock();

	public Position(PositionType positionType, int units, Symbol symbol, Exchange exchange, String securityType, double currentPrice) {
		this.positionType = positionType;
		this.units = units;
		this.symbol = symbol;
		this.exchange = exchange;
		this.securityType = securityType;
		this.unitPriceFirstKnown = currentPrice;
		this.unitPriceLastKnown = currentPrice;
		
		positionUtils = new PositionUtils(this, listOfOrder, lock);
	}

	public void setPositionListener(PositionStatusListener positionStatusListener) {
		this.positionStatusListener = positionStatusListener;
	}

	public void executePosition() {
		synchronized (lock){
			if (positionType == PositionType.position_long_entry) {
				Order order = new Order(symbol, exchange, this, OrderType.order_long_entry, units, unitPriceLastKnown, this);
				order.executeOrder();
				listOfOrder.add(order);
			} else if (positionType == PositionType.position_short_entry) {
				Order order = new Order(symbol, exchange, this, OrderType.order_short_entry, units, unitPriceLastKnown, this);
				order.executeOrder();
				listOfOrder.add(order);
			} else if (positionType == PositionType.position_long_exit) {
				Order order = new Order(symbol, exchange, this, OrderType.order_long_exit, positionUtils.getOrderUnitsFilled(), unitPriceLastKnown, this);
				order.executeOrder();
			} else if (positionType == PositionType.position_short_exit) {
				Order order = new Order(symbol, exchange, this, OrderType.order_short_exit, positionUtils.getOrderUnitsFilled(), unitPriceLastKnown, this);
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

	public boolean isFilled() {
		if (positionType == PositionType.position_long || positionType == PositionType.position_short) {
			return true;
		}
		return false;
	}

	public double getPositionProfitLossAfterComission() {
		return MathTools.round(positionUtils.getOrderValueIntrinsic(true) - positionUtils.getOrderValueRequested(true));
	}
	
	public double getFirstKnownUnitPrice(){
		return unitPriceFirstKnown;
	}
	
	public double getLastKnownUnitPrice(){
		return unitPriceLastKnown;
	}
	
	public void updatePositionUnitPrice(double priceClose){
		unitPriceLastKnown = priceClose;
	}
	
	public PositionValue getPositionValue(){
		return new PositionValue(
			positionUtils.getOrderValueRequested(false), positionUtils.getOrderValueFilled(false), positionUtils.getOrderValueIntrinsic(false),  
			positionUtils.getOrderValueRequested(true), positionUtils.getOrderValueFilled(true), positionUtils.getOrderValueIntrinsic(true), 
			positionUtils.getOrderPriceRequested(true), positionUtils.getOrderPriceFilled(true), positionUtils.getOrderPriceIntrinsic(true),
			positionUtils.getPositionValueCurrent(false), positionUtils.getPositionValueCurrent(false),
			positionUtils.getPositionPriceCurrent(true), positionUtils.getPositionPriceCurrent(true),
			positionUtils.getOrderUnitPriceRequested(), positionUtils.getOrderUnitPriceFilled(), positionUtils.getOrderUnitPriceIntrinsic(),
			getLastKnownUnitPrice()
		);
	}

	@Override
	public void orderStatusChanged(Order order, OrderStatus orderStatus) {
		Co.println("--> Received order status change: " + order.orderType.name() + ", " + orderStatus.name());
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
