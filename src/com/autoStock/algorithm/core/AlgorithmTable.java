package com.autoStock.algorithm.core;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.SignalGroup;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyAction;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.Lock;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmTable {
	private static Lock lock = new Lock();
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	private Symbol symbol;
	
	public AlgorithmTable(Symbol symbol) {
		this.symbol = symbol;
	}

	public void addTableRow(ArrayList<QuoteSlice> listOfQuoteSlice, Signal signal, SignalGroup signalGroup, StrategyResponse strategyResponse, BasicAccount basicAccount){
		ArrayList<String> columnValues = new ArrayList<String>();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);

		columnValues.add(DateTools.getPrettyDate(quoteSlice.dateTime));
		columnValues.add(String.valueOf(quoteSlice.sizeVolume));
		columnValues.add(String.valueOf(MathTools.round(quoteSlice.priceClose)));
		columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.round(quoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size() - 2).priceClose))));
		columnValues.add(String.valueOf(signalGroup.signalOfDI.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfCCI.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfRSI.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfMACD.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfTRIX.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfROC.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfMFI.getStrength()));
		columnValues.add(String.valueOf(signalGroup.signalOfWILLR.getStrength()));
		
		columnValues.add(strategyResponse.positionGovernorResponse.status.name());
		columnValues.add(strategyResponse.strategyAction == StrategyAction.no_change ? "-" : (strategyResponse.strategyAction.name() + ", " + strategyResponse.strategyActionCause.name()));
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalPointType == SignalPointType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalPointType.name());
		columnValues.add(strategyResponse.positionGovernorResponse.signalPoint.signalMetricType == SignalMetricType.no_change ? "-" : strategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name());
		columnValues.add(getTransactionDetails(strategyResponse));
		columnValues.add(String.valueOf(basicAccount.getBalance()));
		
		listOfDisplayRows.add(columnValues);
	}
	
	public String getTransactionDetails(StrategyResponse strategyResponse){ 
		String responseString = "none";
		if (strategyResponse.positionGovernorResponse.position != null){
			String percentGainString = "%" + new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getCurrentPercentGainLoss(true));
			responseString = StringTools.addPlusToPositiveNumbers(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(false))  + ", ";
			responseString += percentGainString;
			
			if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_entry || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_entry){
				responseString = new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionValue().priceCurrentWithFees);
				responseString += " | " + strategyResponse.positionGovernorResponse.position.getInitialUnitsFilled();
				responseString += " | " + strategyResponse.positionGovernorResponse.position.getPositionUtils().getOrderTransactionFeesIntrinsic();
				responseString += " | " + percentGainString;
			}else if (strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_long_exit || strategyResponse.positionGovernorResponse.status == PositionGovernorResponseStatus.changed_short_exit){
				responseString = new DecimalFormat("#.00").format(strategyResponse.positionGovernorResponse.position.getPositionValue().valueCurrentWithFees);
				responseString += " (" + StringTools.addPlusToPositiveNumbers(strategyResponse.positionGovernorResponse.position.getPositionProfitLossAfterComission(false)) + ") ";
				responseString += percentGainString;
			}else{
				//pass
			}
		}
		
		return responseString;
	}

	public void display() {
		synchronized (lock) {
			Co.println("\n--> Symbol " + symbol.symbolName);
			new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);	
//			Co.print(new ExportTools().exportToString(AsciiTables.algorithm_test, listOfDisplayRows));
		}
	}
}
