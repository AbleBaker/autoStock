/**
 * 
 */
package com.autoStock.tables;

import java.text.DecimalFormat;

import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.strategy.StrategyResponse;

/**
 * @author Kevin
 *
 */
public class TableTools {
	private static DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	public static String getTransactionDetails(StrategyResponse strategyResponse){ 
		String responseString = "";
		if (strategyResponse.positionGovernorResponse.position != null){
			String valueGainString = "$" + strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(false);
			String percentGainString = "(" + "%" + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(true)) + ")";
			
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getInitialUnitsFilled();
				responseString += " * " + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getPositionValue().unitPriceFilled);
				responseString += ") + " + strategyResponse.positionGovernorResponse.position.getPositionUtils().getOrderTransactionFeesIntrinsic();
				responseString += " = $" + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getPositionValue().priceCurrentWithFee);
				responseString += " | " + valueGainString;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getUnitsIntrinsic();
				responseString += " * " + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getUnitPriceFilled();
				responseString += ") + " + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getTransactionFees();
				responseString += " = $" + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getOrderValue().priceIntrinsicWithFees);
				responseString += " | " + valueGainString + " | " + percentGainString;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getUnitsIntrinsic();
				responseString += " * " + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getUnitPriceFilled();
				responseString += ") - " + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getTransactionFees();
				responseString += " = $" + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getPositionValue().valueCurrentWithFee);
				responseString += " | $" + strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true);
				responseString += " (" + "%" + decimalFormat.format(strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(true)) + ")";
			}else{
				responseString = valueGainString + " | " + percentGainString;
			}
		}
		
		return responseString;
	}
}
