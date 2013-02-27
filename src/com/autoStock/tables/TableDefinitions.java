package com.autoStock.tables;


/**
 * @author Kevin Kowalewski
 *
 */
public class TableDefinitions {
	public static enum AsciiTables{
		stock_historical_price_live(new AsciiColumns[]{AsciiColumns.symbol, AsciiColumns.dateTime, AsciiColumns.price, AsciiColumns.sizeVolume, AsciiColumns.sizeCount, AsciiColumns.derivedChange}), //, Columns.sizeVolume
		stock_historical_price_db(new AsciiColumns[]{AsciiColumns.id, AsciiColumns.symbol, AsciiColumns.priceOpen, AsciiColumns.priceHigh, AsciiColumns.priceLow, AsciiColumns.priceClose, AsciiColumns.sizeVolume, AsciiColumns.dateTime}),
		analysis_test(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.priceClose, AsciiColumns.derivedChange, AsciiColumns.derivedChange, AsciiColumns.signal , AsciiColumns.PPC, AsciiColumns.ADX, AsciiColumns.CCI, AsciiColumns.BBUpper, AsciiColumns.BBLower, AsciiColumns.MACDHistogram, AsciiColumns.STORSIK, AsciiColumns.STORISD}),
		algorithm_test(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.sizeVolume, AsciiColumns.price, AsciiColumns.derivedChange, AsciiColumns.signalDI, AsciiColumns.signalCCI, AsciiColumns.signalRSI, AsciiColumns.signalMACD, AsciiColumns.signalTRIX, AsciiColumns.signalROC, AsciiColumns.signalMFI, AsciiColumns.signalWILLR, AsciiColumns.signalTotal, AsciiColumns.positionGovernorResponse, AsciiColumns.strategyResponse, AsciiColumns.signalPoint, AsciiColumns.signalMetric, AsciiColumns.transactionDetails, AsciiColumns.bankBalance}),
		algorithm_manager(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.symbol, AsciiColumns.status, AsciiColumns.signal, AsciiColumns.strategyResponse, AsciiColumns.position, AsciiColumns.priceVisible, AsciiColumns.priceEntered, AsciiColumns.priceClose, AsciiColumns.percentChange, AsciiColumns.percentChange, AsciiColumns.transactionDetails, AsciiColumns.signalMetric}),
		order_manager(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.symbol, AsciiColumns.orderType, AsciiColumns.orderStatus, AsciiColumns.orderUnitsRequested, AsciiColumns.orderUnitsRemaining, AsciiColumns.orderUnitsFilled, AsciiColumns.orderPriceRequested, AsciiColumns.orderPriceFilledAvg, AsciiColumns.orderPriceFilledLast}),
		backtest_strategy_response(new AsciiColumns[]{AsciiColumns.dateTime, AsciiColumns.symbol, AsciiColumns.price, AsciiColumns.strategyResponse, AsciiColumns.positionGovernorResponse, AsciiColumns.signal, AsciiColumns.profitLoss}),
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
		priceVisible,
		priceEntered,
		sizeBid,
		sizeAsk,
		sizeLast,
		sizeVolume,
		dateTime,
		derivedChange,
		sizeCount,
		PPC,
		ADX,
		BBUpper,
		BBLower,
		CCI,
		MACDHistogram,
		STORSIK,
		STORISD,
		signal,
		signalDI,
		signalCCI,
		signalRSI,
		signalMACD,
		signalSTORSI,
		signalTRIX,
		signalROC,
		signalMFI,
		signalWILLR,
		signalTotal,
		inducedAction,
		bankBalance,
		transactionDetails,
		peakDetect,
		position,
		percentChange,
		positionGovernorResponse,
		strategyResponse,
		signalPoint,
		signalMetric,
		profitLoss, 
		debug, 
		orderUnitsRequested,
		orderUnitsRemaining,
		orderUnitsFilled, 
		orderPriceRequested, 
		orderPriceFilledAvg,
		orderPriceFilledLast,
		orderStatus,
		status,
	}
}
