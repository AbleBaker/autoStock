package com.autoStock.position;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionValue {
	public final double valueRequested;
	public final double valueFilled;
	public final double valueIntrinsic;

	public final double valueRequestedWithFees;
	public final double valueFilledWithFees;
	public final double valueIntrinsicWithFees;
	
	public final double priceRequestedWithFees;
	public final double priceFilledWithFees;
	public final double priceIntrinsicWithFees;

	public final double valueCurrent;
	public final double valueCurrentWithFees;
	
	public final double priceCurrent;
	public final double priceCurrentWithFees;
	
	public final double unitPriceRequested;
	public final double unitPriceIntrinsic;
	public final double unitPriceFilled;
	public final double unitPriceCurrent;
	
	public PositionValue(double valueRequested, double valueFilled, double valueIntrinsic, 
						 double valueRequestedWithFees, double valueFilledWithFees, double valueIntrinsicWithFees, 
						 double priceRequestedWithFees, double priceFilledWithFees, double priceIntrinsicWithFees, 
						 double valueCurrent, double valueCurrentWithFees, 
						 double priceCurrent, double priceCurrentWithFees, 
						 double unitPriceRequested, double unitPriceIntrinsic, double unitPriceFilled, 
						 double unitPriceCurrent) {
		this.valueRequested = valueRequested;
		this.valueFilled = valueFilled;
		this.valueIntrinsic = valueIntrinsic;
		this.valueRequestedWithFees = valueRequestedWithFees;
		this.valueFilledWithFees = valueFilledWithFees;
		this.valueIntrinsicWithFees = valueIntrinsicWithFees;
		this.priceRequestedWithFees = priceRequestedWithFees;
		this.priceFilledWithFees = priceFilledWithFees;
		this.priceIntrinsicWithFees = priceIntrinsicWithFees;
		this.valueCurrent = valueCurrent;
		this.valueCurrentWithFees = valueCurrentWithFees;
		this.priceCurrent = priceCurrent;
		this.priceCurrentWithFees = priceCurrentWithFees;
		this.unitPriceRequested = unitPriceRequested;
		this.unitPriceIntrinsic = unitPriceIntrinsic;
		this.unitPriceFilled = unitPriceFilled;
		this.unitPriceCurrent = unitPriceCurrent;	
	}	
}
