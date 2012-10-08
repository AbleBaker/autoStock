package com.autoStock.position;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.finance.Account;
import com.autoStock.order.OrderDefinitions.OrderType;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Order;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionUtils {
	private Position position;
	private ArrayList<Order> listOfOrder;
	private Lock lock;
	
	public PositionUtils(Position position, ArrayList<Order> listOfOrder, Lock lock){
		this.position = position;
		this.listOfOrder = listOfOrder;
		this.lock = lock;
	}
	
	public int getOrderUnitsFilled() {
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

	public int getOrderUnitsRequested() {
		synchronized (lock) {
			int units = 0;
			for (Order order : listOfOrder) {
				units += order.getUnitsRequested();
			}
			return units;
		}
	}
	
	public int getOrderUnitsIntrinsic(){
		synchronized (lock) {
			int units = 0;
			for (Order order : listOfOrder) {
				units += order.getUnitsIntrinsic();
			}
			return units;
		}
	}
	
	public double getOrderTransactionFeesIntrinsic(){
		synchronized(lock){
			double transactionFees = 0;
			for (Order order : listOfOrder){
				transactionFees += order.getOrderValue().transactionFees;
			}
			return transactionFees;
		}
	}
	
	public double getOrderPriceRequested(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().priceRequestedWithFees : order.getOrderValue().valueRequested;
			}
			return priceTotal;
		}
	}
	public double getOrderPriceFilled(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().priceFilledWithFees : order.getOrderValue().valueFilled;
			}
			return priceTotal;
		}
	}
	public double getOrderPriceIntrinsic(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().priceIntrinsicWithFees : order.getOrderValue().valueIntrinsic;
			}
			return priceTotal;
		}
	}
	
	public double getOrderValueRequested(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().valueRequestedWithFees : order.getOrderValue().valueRequested;
			}
			return priceTotal;
		}
	}
	
	public double getOrderValueFilled(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().valueFilledWithFees : order.getOrderValue().valueFilled;
			}
			return priceTotal;
		}
	}
	
	public double getOrderValueIntrinsic(boolean includeTransactionFees){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += includeTransactionFees ? order.getOrderValue().valueIntrinsicWithFees : order.getOrderValue().valueIntrinsic;
			}
			return priceTotal;
		}
	}
	
	public double getPositionValueCurrent(boolean includeTransactionFees){
		double unitPriceLastKnown = position.getLastKnownUnitPrice();
		double priceTotal = getOrderUnitsIntrinsic() * unitPriceLastKnown;
		double transactionFees = Account.getInstance().getTransactionCost(getOrderUnitsIntrinsic(), unitPriceLastKnown);
		
		return priceTotal - (includeTransactionFees ? transactionFees : 0);
	}
	
	public double getPositionPriceCurrent(boolean includeTransactionFees){
		double unitPriceLastKnown = position.getLastKnownUnitPrice();
		double priceTotal = getOrderUnitsIntrinsic() * unitPriceLastKnown;
		double transactionFees = Account.getInstance().getTransactionCost(getOrderUnitsIntrinsic(), unitPriceLastKnown);
		
		return priceTotal + (includeTransactionFees ? transactionFees : 0);
	}
	
	public double getOrderUnitPriceRequested(){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += order.getOrderValue().unitPriceRequested;
			}
			return priceTotal;
		}
	}
	
	public double getOrderUnitPriceFilled(){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += order.getOrderValue().unitPriceFilled;
			}
			return priceTotal;
		}
	}
	
	public double getOrderUnitPriceIntrinsic(){
		synchronized (lock){
			double priceTotal = 0;
			for (Order order : listOfOrder){
				priceTotal += order.getOrderValue().unitPriceIntrinsic;
			}
			return priceTotal;
		}
	}
}
