/**
 * 
 */
package com.autoStock.dataFeed;

import java.util.ArrayList;

import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.exchange.results.ResultQuoteSlice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.trading.types.TypeHistoricalData;

/**
 * @author Kevin Kowalewski
 *
 */
public class DataFeedHistoricalPrices {
	private int feedInterval;
	private TypeHistoricalData typeHistoricalData;
	private ArrayList<DataFeedListenerOfQuoteSlice> listOfListener = new ArrayList<DataFeedListenerOfQuoteSlice>();
	private ArrayList<DbStockHistoricalPrice> listOfPrices;
	private Thread threadForDelivery;
	
	public DataFeedHistoricalPrices(TypeHistoricalData typeHistoricalData, ArrayList<DbStockHistoricalPrice> listOfPrices){
		this.typeHistoricalData = typeHistoricalData;
		this.listOfPrices = listOfPrices;
	}
	
	public void startFeed(int feedIntervalSec, final int delayMsec){
		if (feedIntervalSec < 1){throw new IllegalArgumentException();}
		
		threadForDelivery = new Thread(new Runnable(){
			@Override
			public void run() {
				for (DbStockHistoricalPrice price : listOfPrices){
					feed(price);
					try {Thread.sleep(delayMsec);}catch(InterruptedException e){return;}
				}
			}
		});
		
		threadForDelivery.setPriority(Thread.MIN_PRIORITY);
		threadForDelivery.start();
	}
	
	private void feed(DbStockHistoricalPrice price){
		for (DataFeedListenerOfQuoteSlice listener : listOfListener){
			listener.receivedQuoteSlice(new ResultQuoteSlice(price.symbol, price.priceOpen, price.priceHigh, price.priceLow, price.priceClose, -1, -1, price.sizeVolume, price.dateTime));
		}
	}
	
	public void addListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.add(listener);
	}
	
	public void removeListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.remove(listener);
	}
}
