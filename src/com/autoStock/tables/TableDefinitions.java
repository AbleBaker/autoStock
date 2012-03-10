package com.autoStock.tables;

import com.google.common.base.Ascii;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableDefinitions {
	public static enum AsciiTables{
		stock_historical_price_live(new AsciiColumns[]{AsciiColumns.symbol, AsciiColumns.dateTime, AsciiColumns.price, AsciiColumns.sizeVolume, AsciiColumns.sizeCount, AsciiColumns.derivedChange}), //, Columns.sizeVolume
		stock_historical_price_db(new AsciiColumns[]{AsciiColumns.id, AsciiColumns.symbol, AsciiColumns.priceOpen, AsciiColumns.priceHigh, AsciiColumns.priceLow, AsciiColumns.priceClose, AsciiColumns.sizeVolume, AsciiColumns.dateTime}),
		analysis_test(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.priceClose, AsciiColumns.derivedChange, AsciiColumns.derivedChange, AsciiColumns.signal ,AsciiColumns.ADX, AsciiColumns.BBUpper, AsciiColumns.BBLower, AsciiColumns.MACDSignal, AsciiColumns.STORSIK, AsciiColumns.STORISD});
		;
		
		AsciiColumns[] arrayOfColumns;
		
		AsciiTables (AsciiColumns[] arrayOfColumns){
			this.arrayOfColumns = arrayOfColumns;
		}
		
		public AsciiTables injectColumns(AsciiColumns... columns){
			AsciiColumns[] tempArrayOfColumns = new AsciiColumns[arrayOfColumns.length + columns.length];
	
			int i = 0;
			for (AsciiColumns column : arrayOfColumns){
				tempArrayOfColumns[i] = column;
				i++;
			}
			
			for (AsciiColumns column : columns){
				tempArrayOfColumns[i] = column;
				i++;
			}
			
			arrayOfColumns = tempArrayOfColumns;
			
			return this;
		}
	}
	
	public static enum AsciiColumns {
		id,
		symbol,
		orderType,
		quantity,
		priceLimit,
		priceStop,
		priceOpen,
		priceHigh,
		priceLow,
		priceClose,
		goodAfterDate,
		goodUntilDate,
		priceAverageFill,
		exchange,
		currency,
		securityType,
		price,
		sizeBid,
		sizeAsk,
		sizeLast,
		sizeVolume,
		dateTime,
		derivedChange,
		sizeCount,
		ADX,
		BBUpper,
		BBLower,
		CCI,
		MACDSignal,
		STORSIK,
		STORISD,
		signal,
	}
}
