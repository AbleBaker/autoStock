/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.dataFeed.DataFeedHistoricalPrices;
import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class Backtest implements DataFeedListenerOfQuoteSlice {
	private HistoricalData typeHistoricalData;
	private DataFeedHistoricalPrices dataFeedHistoricalPrices;
	private ArrayList<DbStockHistoricalPrice> listOfPrices;
	private ReceiverOfQuoteSlice receiverOfQuoteSlice;
	
	public Backtest(HistoricalData typeHistoricalData, ArrayList<DbStockHistoricalPrice> listOfPrices){
		this.typeHistoricalData = typeHistoricalData;
		this.listOfPrices = listOfPrices;
		this.dataFeedHistoricalPrices = new DataFeedHistoricalPrices(typeHistoricalData, listOfPrices);
	}
	
	public void performBacktest(ReceiverOfQuoteSlice reciever){
		receiverOfQuoteSlice = reciever;
		dataFeedHistoricalPrices.addListener(this);
		dataFeedHistoricalPrices.startFeed(Resolution.min.seconds, 0);
	}

	@Override
	public void receivedQuoteSlice(QuoteSlice typeQuoteSlice) {
		//Co.println("Received backtest quote: " + DateTools.getPrettyDate(resultQuoteSlice.dateTime) + ", " + resultQuoteSlice.priceClose);
		receiverOfQuoteSlice.receiveQuoteSlice(typeQuoteSlice);
	}

	@Override
	public void endOfFeed() {
		receiverOfQuoteSlice.endOfFeed();
	}	
}
