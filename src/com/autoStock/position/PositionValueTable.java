package com.autoStock.position;

import com.autoStock.Co;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public class PositionValueTable {
	public synchronized void printTable(Position position, PositionValue positionValue){
		Co.println("\n\n --> PositionValue... " + position.symbol.symbolName);
		Co.println(
				   "\n valueRequested -> " + positionValue.valueRequested //OK
				   + "\n valueFilled -> " + positionValue.valueFilled //OK
				   + "\n valueIntrinsic -> " + positionValue.valueIntrinsic //OK
				   
				   + "\n\n valueRequestedWithFees -> " + positionValue.valueRequestedWithFees //OK
				   + "\n valueFilledWithFees -> " + positionValue.valueFilledWithFees  //OK 
				   + "\n valueInstrinsicWithFees -> " + positionValue.valueIntrinsicWithFees //OK
				   
				   + "\n\n priceRequestedWithFees -> " + positionValue.priceRequestedWithFees //OK
				   + "\n priceFilledWithFees -> " + positionValue.priceFilledWithFees //OK
				   + "\n priceIntrinsicWithFees -> " + positionValue.priceIntrinsicWithFees //OK
				   
				   + "\n\n valueCurrent -> " + positionValue.valueCurrent //OK
				   + "\n valueCurrentWithFees -> " + positionValue.valueCurrentWithFees //OK

				   + "\n\n priceCurrent -> " + positionValue.priceCurrent //OK
				   + "\n priceCurrentWithFees -> " + positionValue.priceCurrentWithFees //OK	

				   + "\n\n unitPriceRequested -> " + positionValue.unitPriceRequested //OK
				   + "\n unitPriceFilled -> " + positionValue.unitPriceFilled //OK
				   + "\n unitPriceIntrinsic -> " + positionValue.unitPriceIntrinsic //OK
				   + "\n unitPriceCurrent -> " + positionValue.unitPriceCurrent //OK
		);
	}
}
