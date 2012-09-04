package com.autoStock.trading.platform.ib.subset;

import com.autoStock.exchange.ExchangeHelper.ExchangeDesignation;
import com.autoStock.exchange.request.RequestMarketScanner.MarketScannerType;
import com.autoStock.trading.platform.ib.core.ScannerSubscription;
import com.autoStock.types.Exchange;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SubsetOfScannerSubscription {
//	public static final int maxCombinedResults = 200;

	public ScannerSubscription getScanner(Exchange exchange, MarketScannerType marketScannerType) {
		ScannerSubscription scannerSubscription = new ScannerSubscription();

		scannerSubscription.numberOfRows(100);
		scannerSubscription.stockTypeFilter("ALL");
		scannerSubscription.averageOptionVolumeAbove(0);

		if (exchange.exchangeDesignation == ExchangeDesignation.NYSE) {
			scannerSubscription.instrument("STK");
			scannerSubscription.locationCode("STK.NYSE");
		} else if (exchange.exchangeDesignation == ExchangeDesignation.ASX) {
			scannerSubscription.instrument("STOCK.HK");
			scannerSubscription.locationCode("STK.HK.ASX");
		} else {
			throw new UnsupportedOperationException();
		}

		scannerSubscription.abovePrice(4.00);
		scannerSubscription.belowPrice(1000.00);

		modifyScannerWithType(scannerSubscription, marketScannerType);

		return scannerSubscription;
	}

	private void modifyScannerWithType(ScannerSubscription scannerSubscription, MarketScannerType marketScannerType) {
		if (marketScannerType == MarketScannerType.type_percent_gain_open) {
			scannerSubscription.scanCode("TOP_OPEN_PERC_GAIN");
			scannerSubscription.aboveVolume(100 * 1000);
		}

		else if (marketScannerType == MarketScannerType.type_percent_gain) {
			scannerSubscription.scanCode("TOP_PERC_GAIN");
			scannerSubscription.aboveVolume(100 * 1000);
		}

		else if (marketScannerType == MarketScannerType.type_top_trade_rate) {
			scannerSubscription.scanCode("TOP_TRADE_RATE");
			scannerSubscription.aboveVolume(100 * 1000);
		}

		else if (marketScannerType == MarketScannerType.type_most_active) {
			scannerSubscription.scanCode("MOST_ACTIVE_USD");
			scannerSubscription.aboveVolume(100 * 1000);
		}

		else if (marketScannerType == MarketScannerType.type_hot_by_price) {
			scannerSubscription.scanCode("HOT_BY_PRICE");
			scannerSubscription.aboveVolume(100 * 1000);
		} 
		
		else if (marketScannerType == MarketScannerType.type_hot_by_volume) {
			scannerSubscription.scanCode("HOT_BY_VOLUME");
			scannerSubscription.aboveVolume(100 * 1000);
		} 
		
		else {
			throw new UnsupportedOperationException();
		}
	}
}
