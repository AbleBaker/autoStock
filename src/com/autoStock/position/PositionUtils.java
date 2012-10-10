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

	public PositionUtils(Position position, ArrayList<Order> listOfOrder, Lock lock) {
		this.position = position;
		this.listOfOrder = listOfOrder;
	}

	public int getOrderUnitsFilled() {

		int units = 0;
		for (Order order : listOfOrder) {
			if (order.orderType == OrderType.order_long || order.orderType == OrderType.order_short) {
				units += order.getUnitsFilled();
			}
		}
		return units;
	}

	public int getOrderUnitsRequested() {

		int units = 0;
		for (Order order : listOfOrder) {
			units += order.getUnitsRequested();
		}
		return units;
	}

	public int getOrderUnitsIntrinsic() {

		int units = 0;
		for (Order order : listOfOrder) {
			units += order.getUnitsIntrinsic();
		}
		return units;
	}

	public double getOrderTransactionFeesIntrinsic() {
		double transactionFees = 0;
		for (Order order : listOfOrder) {
			transactionFees += order.getOrderValue().transactionFees;
		}
		return transactionFees;
	}

	public double getOrderPriceRequested(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().priceRequestedWithFees : order.getOrderValue().valueRequested;
		}
		return priceTotal;
	}

	public double getOrderPriceFilled(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().priceFilledWithFees : order.getOrderValue().valueFilled;
		}
		return priceTotal;
	}

	public double getOrderPriceIntrinsic(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().priceIntrinsicWithFees : order.getOrderValue().valueIntrinsic;
		}
		return priceTotal;
	}

	public double getOrderValueRequested(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().valueRequestedWithFees : order.getOrderValue().valueRequested;
		}
		return priceTotal;
	}

	public double getOrderValueFilled(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().valueFilledWithFees : order.getOrderValue().valueFilled;
		}
		return priceTotal;
	}

	public double getOrderValueIntrinsic(boolean includeTransactionFees) {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += includeTransactionFees ? order.getOrderValue().valueIntrinsicWithFees : order.getOrderValue().valueIntrinsic;
		}
		
		return priceTotal;
	}

	public double getPositionValueCurrent(boolean includeTransactionFees) {
		double unitPriceLastKnown = position.getLastKnownUnitPrice();
		double priceTotal = getOrderUnitsIntrinsic() * unitPriceLastKnown;
		double transactionFees = Account.getInstance().getTransactionCost(getOrderUnitsIntrinsic(), unitPriceLastKnown);

		priceTotal -= (includeTransactionFees ? transactionFees : 0);
		
		return priceTotal;
	}

	public double getPositionPriceCurrent(boolean includeTransactionFees) {
		double unitPriceLastKnown = position.getLastKnownUnitPrice();
		double priceTotal = getOrderUnitsIntrinsic() * unitPriceLastKnown;
		double transactionFees = Account.getInstance().getTransactionCost(getOrderUnitsIntrinsic(), unitPriceLastKnown);

		return priceTotal + (includeTransactionFees ? transactionFees : 0);
	}

	public double getOrderUnitPriceRequested() {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += order.getOrderValue().unitPriceRequested;
		}
		return priceTotal;
	}

	public double getOrderUnitPriceFilled() {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += order.getOrderValue().unitPriceFilled;
		}
		return priceTotal;
	}

	public double getOrderUnitPriceIntrinsic() {
		double priceTotal = 0;
		for (Order order : listOfOrder) {
			priceTotal += order.getOrderValue().unitPriceIntrinsic;
		}
		return priceTotal;
	}
}
