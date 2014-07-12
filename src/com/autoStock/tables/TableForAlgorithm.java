/**
 * 
 */
package com.autoStock.tables;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.autoStock.account.BasicAccount;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin
 *
 */
public class TableForAlgorithm extends BaseTable {
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	private static DecimalFormat decimalFormat = new DecimalFormat("#.00");
	
	public void addTableRow(ArrayList<QuoteSlice> listOfQuoteSlice, Signal signal, SignalGroup signalGroup, StrategyResponse strategyResponse, BasicAccount basicAccount){
		ArrayList<String> columnValues = new ArrayList<String>();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);

		columnValues.add(DateTools.getPrettyDate(quoteSlice.dateTime));
		columnValues.add(String.valueOf(quoteSlice.sizeVolume));
		columnValues.add(decimalFormat.format(quoteSlice.priceClose));
		columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.round(quoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size() - 2).priceClose))));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfDI.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfCCI.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfRSI.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfMACD.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfTRIX.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfROC.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfMFI.getStrength())));
		columnValues.add(String.valueOf(new DecimalFormat("0.00").format(signalGroup.signalOfWILLR.getStrength())));
		
		columnValues.add(strategyResponse.positionGovernorResponse.status.name());
		columnValues.add(strategyResponse.strategyAction == StrategyAction.no_change ? "-" : (strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name()));
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalPointType == SignalPointType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalPointType.name());
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalMetricType == SignalMetricType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name());
		columnValues.add(getTransactionDetails(strategyResponse));
		columnValues.add(decimalFormat.format(basicAccount.getBalance()));
		
		listOfDisplayRows.add(columnValues);
	}
	
	public String getTransactionDetails(StrategyResponse strategyResponse){ 
		String responseString = "none";
		if (strategyResponse.positionGovernorResponse.position != null){
			String percentGainString = "$" + strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(false);
			percentGainString += " (" + "%" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(true)) + ")";
			responseString = percentGainString;
			
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getInitialUnitsFilled();
				responseString += " * " + strategyResponse.positionGovernorResponse.position.getPositionValue().unitPriceFilled;
				responseString += ") + " + strategyResponse.positionGovernorResponse.position.getPositionUtils().getOrderTransactionFeesIntrinsic();
				responseString += " = $" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionValue().priceCurrentWithFee);
				responseString += " | " + percentGainString;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_reentry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_reentry){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getUnitsIntrinsic();
				responseString += " * " + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getUnitPriceFilled();
				responseString += ") + " + strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getTransactionFees();
				responseString += " = $" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getLastEntryOrder().getOrderValue().priceIntrinsicWithFees);
				responseString += " | " + percentGainString;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				responseString = "(" + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getUnitsIntrinsic();
				responseString += " * " + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getUnitPriceFilled();
				responseString += ") - " + strategyResponse.positionGovernorResponse.position.getLastExitOrder().getTransactionFees();
				responseString += " = $" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionValue().valueCurrentWithFee);
				responseString += " | $" + strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(true);
				responseString += " (" + "%" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(true)) + ")";
			}else{
				//pass
			}
		}
		
		return responseString;
	}

	@Override
	public ArrayList<ArrayList<String>> getDisplayRows() {
		return 	listOfDisplayRows;
	}

}
