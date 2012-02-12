/**
 * 
 */
package com.autoStock.display;

import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.exchange.request.RequestHistoricalData;
import com.autoStock.exchange.request.RequestMarketData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestHistoricalDataListener;
import com.autoStock.exchange.request.listener.RequestMarketDataListener;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.results.ExResultMarketData;
import com.autoStock.trading.results.ExResultMarketData.ExResultSetMarketData;
import com.autoStock.trading.results.ExResultHistoricalData.*;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.trading.types.TypeMarketData;

/**
 * @author Kevin Kowalewski
 *
 */
public class DisplayMarketData {
	
	private TypeMarketData typeMarketData;
	
	public DisplayMarketData(TypeMarketData typeMarketData){
		this.typeMarketData = typeMarketData;
	}
	
	public void display(){
		new RequestMarketData(new RequestHolder(null), new RequestMarketDataListener() {
			@Override
			public void failed(RequestHolder requestHolder) {
				
			}
			
			@Override
			public void completed(RequestHolder requestHolder, ExResultSetMarketData exResultSetMarketData) {
				Co.println("Completed!");
				
			}
		}, typeMarketData);
	}
}
