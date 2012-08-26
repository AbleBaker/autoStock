package com.autoStock.algorithm.core;

import java.util.ArrayList;

import com.autoStock.signal.Signal;
import com.autoStock.signal.SignalGroup;
import com.autoStock.signal.SignalTools;
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
	
	public void addTableRow(ArrayList<QuoteSlice> listOfQuoteSlice, Signal signal, SignalGroup signalGroup){
		ArrayList<String> columnValues = new ArrayList<String>();
		QuoteSlice quoteSlice = listOfQuoteSlice.get(listOfQuoteSlice.size()-1);

		columnValues.add(DateTools.getPrettyDate(quoteSlice.dateTime));
		columnValues.add(String.valueOf(MathTools.round(quoteSlice.priceClose)));
		columnValues.add(String.valueOf(StringTools.addPlusToPositiveNumbers(MathTools.round(quoteSlice.priceClose - listOfQuoteSlice.get(listOfQuoteSlice.size() - 2).priceClose))));
		columnValues.add(String.valueOf(signalGroup.signalOfPPC.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfDI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfCCI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfRSI.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfMACD.getSignal().strength));
		columnValues.add(String.valueOf(signalGroup.signalOfTRIX.getSignal().strength));
		columnValues.add(String.valueOf(SignalTools.getCombinedSignal(signal).strength));
		
		columnValues.add("");
		columnValues.add("");
		columnValues.add("");
		
		listOfDisplayRows.add(columnValues);
	}

	public void display() {
		new TableController().displayTable(AsciiTables.algorithm_test, listOfDisplayRows);
	}
}
