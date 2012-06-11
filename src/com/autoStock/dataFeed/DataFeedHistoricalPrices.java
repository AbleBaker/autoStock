/**
 * 
 */
package com.autoStock.dataFeed;

import java.util.ArrayList;

import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class DataFeedHistoricalPrices {
	private int feedInterval;
	private HistoricalData typeHistoricalData;
	private ArrayList<DataFeedListenerOfQuoteSlice> listOfListener = new ArrayList<DataFeedListenerOfQuoteSlice>();
	private ArrayList<DbStockHistoricalPrice> listOfPrices;
	private Resolution resolution;
	private Thread threadForDelivery;
	
	public DataFeedHistoricalPrices(HistoricalData typeHistoricalData, ArrayList<DbStockHistoricalPrice> listOfPrices){
		this.typeHistoricalData = typeHistoricalData;
		this.listOfPrices = listOfPrices;
		this.resolution = typeHistoricalData.resolution;
	}
	
	public void startFeed(int feedIntervalSec, final int delayMsec){		
		threadForDelivery = new Thread(new Runnable(){
			@Override
			public void run() {
				for (DbStockHistoricalPrice price : listOfPrices){
					feed(price);
					try {Thread.sleep(delayMsec);}catch(InterruptedException e){return;}
				}
				
				feedFinished();
				threadForDelivery.interrupt();
			}
		});
		
		threadForDelivery.start();
	}
	
	private void feedFinished(){
		for (DataFeedListenerOfQuoteSlice listener : listOfListener){
			listener.endOfFeed();
		}
	}
	
	private void feed(DbStockHistoricalPrice price){
		for (DataFeedListenerOfQuoteSlice listener : listOfListener){
			listener.receivedQuoteSlice(new QuoteSlice(price.symbol, price.priceOpen, price.priceHigh, price.priceLow, price.priceClose, -1, -1, price.sizeVolume, price.dateTime, resolution));
		}
	}
	
	public void addListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.add(listener);
	}
	
	public void removeListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.remove(listener);
	}
}
