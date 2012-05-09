/**
 * 
 */
package com.autoStock.scanner;

import java.util.ArrayList;

import com.autoStock.database.DatabaseDefinitions;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.queryResults.QueryResult;
import com.autoStock.database.queryResults.QueryResult.QrSymbolCountFromExchange;
import com.autoStock.types.ShorlistOfStock;

/**
 * @author Kevin Kowalewski
 * 
 */
public class Shortlist {
	private String exchange;
	
	public static enum ShortlistReason {
		high_volume,
		high_signal_combined,
		high_signal_ppc,
	}
	
	public Shortlist(String exchange){
		this.exchange = exchange;
	}
	
	public ArrayList<ShorlistOfStock> getShortlistedStocks(){
		ArrayList<ShorlistOfStock> listOfSymbol = new ArrayList<ShorlistOfStock>();
		
		for (QueryResult.QrSymbolCountFromExchange queryResult : generateShortlist()){
			listOfSymbol.add(new ShorlistOfStock(queryResult.symbol, exchange, ShortlistReason.high_volume));
		}
		
		return listOfSymbol;
	}

	private ArrayList<QueryResult.QrSymbolCountFromExchange> generateShortlist() {
		ArrayList<QueryResult.QrSymbolCountFromExchange> listOfQr = (ArrayList<QrSymbolCountFromExchange>) new DatabaseQuery().getQueryResults(
				DatabaseDefinitions.BasicQueries.basic_get_symbol_list_most_volume, 
				QueryArgs.exchange.setValue(exchange),
				QueryArgs.startDate.setValue("2011-01-05 09:30:00"),
				QueryArgs.endDate.setValue("2011-01-05 16:00:00"),
				QueryArgs.limit.setValue("10")
		);
		
		return listOfQr;
	}
}
