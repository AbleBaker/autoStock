/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.dataFeed.DataFeedHistoricalPrices;
import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.exchange.results.ResultQuoteSlice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class Backtest implements DataFeedListenerOfQuoteSlice {
	private TypeHistoricalData typeHistoricalData;
	private DataFeedHistoricalPrices dataFeedHistoricalPrices;
	private ArrayList<DbStockHistoricalPrice> listOfPrices;
	
	public Backtest(TypeHistoricalData typeHistoricalData, ArrayList<DbStockHistoricalPrice> listOfPrices){
		this.typeHistoricalData = typeHistoricalData;
		this.listOfPrices = listOfPrices;
		this.dataFeedHistoricalPrices = new DataFeedHistoricalPrices(typeHistoricalData, listOfPrices);
	}
	
	public void performBacktest(){
		dataFeedHistoricalPrices.addListener(this);
		dataFeedHistoricalPrices.startFeed(60, 10);
	}

	@Override
	public void receivedQuoteSlice(ResultQuoteSlice resultQuoteSlice) {
		Co.println("Received backtest quote: " + DateTools.getPrettyDate(resultQuoteSlice.dateTime) + ", " + resultQuoteSlice.priceClose);
	}	
}
