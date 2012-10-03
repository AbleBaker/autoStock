package com.autoStock.algorithm.core;

import java.util.ArrayList;

import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.position.PositionDefinitions.PositionType;
import com.autoStock.position.PositionManager;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class AlgorithmManagerTable {
	private ArrayList<ArrayList<String>> listOfDisplayRows = new ArrayList<ArrayList<String>>();
	
	public void addRow(AlgorithmTest algorithm, ArrayList<QuoteSlice> listOfQuoteSlice){
		ArrayList<String> columnValues = new ArrayList<String>();
		
		Position position = PositionManager.instance.getPosition(algorithm.symbol.symbolName);
		
		double percentGainFromAlgorithm = 0;
		double percentGainFromPosition = 0;
		
		if (algorithm.firstQuoteSlice != null && algorithm.getCurrentQuoteSlice() != null){
			if (algorithm.firstQuoteSlice.priceClose != 0 && algorithm.getCurrentQuoteSlice().priceClose != 0){
				percentGainFromAlgorithm = (algorithm.getCurrentQuoteSlice().priceClose / algorithm.firstQuoteSlice.priceClose) -1d;
			}
		}
		
		if (position != null && (position.positionType == PositionType.position_long || position.positionType == PositionType.position_short)){
			if (position.price != 0 && position.lastKnownPrice != 0){
				percentGainFromPosition = (position.lastKnownPrice / position.price);
			}
		}
		
		columnValues.add(algorithm.getCurrentQuoteSlice() != null && algorithm.getCurrentQuoteSlice().dateTime != null ? DateTools.getPrettyDate(algorithm.getCurrentQuoteSlice().dateTime) : "?"); 
		columnValues.add(algorithm.symbol.symbolName);
		columnValues.add(algorithm.strategy.lastStrategyResponse == null ? "-" : (algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.signalPointType.name() + ", " + algorithm.strategy.lastStrategyResponse.positionGovernorResponse.signalPoint.signalMetricType.name()));
		columnValues.add(position == null ? "-" : position.positionType.name());
		columnValues.add(String.valueOf(algorithm.getFirstQuoteSlice() == null ? 0 : MathTools.round(algorithm.getFirstQuoteSlice().priceClose)));
		columnValues.add(String.valueOf(algorithm.getCurrentQuoteSlice() == null ? 0 : MathTools.round(algorithm.getCurrentQuoteSlice().priceClose)));
		columnValues.add(String.valueOf(percentGainFromAlgorithm));
		columnValues.add(String.valueOf(percentGainFromPosition == 1 ? "-" : percentGainFromPosition));
		columnValues.add(String.valueOf(position == null ? "-" : ("P&L: " + StringTools.addPlusToPositiveNumbers(position.getPositionProfitLossAfterComission()))));
		
		listOfDisplayRows.add(columnValues);
	}
	
	public ArrayList<ArrayList<String>> getListOfDisplayRows(){
		return listOfDisplayRows;
	}

	public void clear() {
		listOfDisplayRows.clear();
	}
}
