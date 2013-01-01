/**
 * 
 */
package com.autoStock.dataFeed;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.backtest.BacktestRevolverListener;
import com.autoStock.dataFeed.listener.DataFeedListenerOfQuoteSlice;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class DataFeedHistoricalPrices implements BacktestRevolverListener {
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
	
	public void startFeed(){		
		threadForDelivery = new Thread(new Runnable(){
			@Override
			public void run() {
				for (DbStockHistoricalPrice price : listOfPrices){
					feed(price);
				}
				
				feedFinished();
				threadForDelivery.interrupt();
			}
		});
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
				ApplicationStates.shutdown();
			}
		});
		threadForDelivery.start();
	}
	
	private void feedFinished(){
		for (DataFeedListenerOfQuoteSlice listener : listOfListener){
			listener.endOfFeed();
		}
	}
	
	private void feed(DbStockHistoricalPrice dbStockHistoricalPrice){
		for (DataFeedListenerOfQuoteSlice listener : listOfListener){
			listener.receivedQuoteSlice(QuoteSliceTools.getQuoteSlice(dbStockHistoricalPrice, resolution));
		}
	}
	
	public void addListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.add(listener);
	}
	
	public void removeListener(DataFeedListenerOfQuoteSlice listener){
		listOfListener.remove(listener);
	}

	@Override
	public void proceedFeed() {
		
	}
}
