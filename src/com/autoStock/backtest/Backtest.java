/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.balance.AccountBalance;
import com.autoStock.dataFeed.DataFeedHistoricalPrices;
import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class Backtest implements DataFeedListenerOfQuoteSlice {
	private TypeHistoricalData typeHistoricalData;
	private DataFeedHistoricalPrices dataFeedHistoricalPrices;
	private ArrayList<DbStockHistoricalPrice> listOfPrices;
	private ReceiverOfQuoteSlice receiverOfQuoteSlice;
	
	public Backtest(TypeHistoricalData typeHistoricalData, ArrayList<DbStockHistoricalPrice> listOfPrices){
		this.typeHistoricalData = typeHistoricalData;
		this.listOfPrices = listOfPrices;
		this.dataFeedHistoricalPrices = new DataFeedHistoricalPrices(typeHistoricalData, listOfPrices);
	}
	
	public void performBacktest(ReceiverOfQuoteSlice reciever){
		receiverOfQuoteSlice = reciever;
		dataFeedHistoricalPrices.addListener(this);
		dataFeedHistoricalPrices.startFeed(60, 0);
	}

	@Override
	public void receivedQuoteSlice(TypeQuoteSlice typeQuoteSlice) {
		//Co.println("Received backtest quote: " + DateTools.getPrettyDate(resultQuoteSlice.dateTime) + ", " + resultQuoteSlice.priceClose);
		receiverOfQuoteSlice.receiveQuoteSlice(typeQuoteSlice);
	}

	@Override
	public void endOfFeed() {
		receiverOfQuoteSlice.endOfFeed();
		Co.println("Bank Balance: " + AccountBalance.bankBalance);
	}	
}
