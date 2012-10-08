package com.autoStock.algorithm.core;

import java.util.ArrayList;

import com.autoStock.finance.Account;
import com.autoStock.position.PositionGovernorResponse.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalTools;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTable {
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	
	public void addTableRow(ArrayList<QuoteSlice> listOfQuoteSlice, Signal signal, SignalGroup signalGroup, StrategyResponse strategyResponse){
		ArrayList<String> columnValues = new ArrayList<String>();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);

		columnValues.add(DateTools.getPrettyDate(quoteSlice.dateTime));
		columnValues.add(String.valueOf(quoteSlice.sizeVolume));
		columnValues.add(String.valueOf(MathTools.round(quoteSlice.priceClose)));
		columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.round(quoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size() - 2).priceClose))));
		columnValues.add(String.valueOf(signalGroup.signalOfPPC.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfDI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfCCI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfRSI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfMACD.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfTRIX.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfROC.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfMFI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfWILLR.getSignal().strength));
		columnValues.add(String.valueOf(SignalTools.getCombinedSignal(signal).strength));
		
		columnValues.add(strategyResponse.positionGovernorResponse.status.name());
		columnValues.add(strategyResponse.strategyAction == StrategyAction.no_change ? "-" : (strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name()));
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalPointType == SignalPointType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalPointType.name());
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalMetricType == SignalMetricType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name());
		columnValues.add(getTransactionDetails(strategyResponse));
		columnValues.add(String.valueOf(Account.getInstance().getAccountBalance()));
		
		listOfDisplayRows.add(columnValues);
	}
	
	public String getTransactionDetails(StrategyResponse strategyResponse){ 
		String responseString = "none";
		if (strategyResponse.positionGovernorResponse.position != null){
			responseString = StringTools.addPlusToPositiveNumbers(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission());
			
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				responseString = String.valueOf(strategyResponse.positionGovernorResponse.position.getPositionValue().priceCurrentWithFees);
				
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				responseString = String.valueOf(strategyResponse.positionGovernorResponse.position.getPositionValue().valueCurrentWithFees);
				responseString += "(" + StringTools.addPlusToPositiveNumbers(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission()) + ")";
			}
		}
		
		return responseString;
	}

	public void display() {
		new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);
	}
}
