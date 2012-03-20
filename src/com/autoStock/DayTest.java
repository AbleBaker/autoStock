/**
 * 
 */
package com.autoStock;

import java.util.Calendar;
import java.util.Date;

import com.autoStock.exchange.request.RequestMarketScanner;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestMarketScannerListener;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultRowMarketScanner;
import com.autoStock.exchange.results.ExResultMarketScanner.ExResultSetMarketScanner;

/**
 * @author Kevin Kowalewski
 *
 */
public class DayTest {
	
	public void init(){
//		while (true){
//			//Co.println("Waiting for date... " + getDate().getTime() + ", " + getDate().getHours());
//			if (getDate().getHours() >= 9 && getDate().getMinutes() >= 35){
//				break;
//			}
//			try {Thread.sleep(1000);}catch(InterruptedException e){return;}
//		}
		
		dayStart();
	}
	
	public void dayStart(){
		new RequestMarketScanner(new RequestHolder(new RequestMarketScannerListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketScanner exResultSetMarketScanner) {
				for (ExResultRowMarketScanner result : exResultSetMarketScanner.listOfExResultRowMarketScanner){
					Co.println("Should run algorithm for symbol: " + result.symbol);
				}
			}
		}));
	}
	
	public void dayEnd(){
		
	}
	
	public Date getDate(){
		return new Date();
	}
}
