package com.autoStock.algorithm;

import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.indicator.IndicatorGroup;
import com.autoStock.signal.SignalGroup;
import com.autoStock.strategy.StrategyHelper;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase implements ReceiverOfQuoteSlice {
	public StrategyOfTest strategy = new StrategyOfTest(this);
	
	public AlgorithmTest(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode) {
		super(canTrade, exchange, symbol, algorithmMode);
		
		indicatorGroup = new IndicatorGroup(periodLength, commonAnlaysisData);
		signalGroup = new SignalGroup();
	}

	@Override
	public synchronized void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
		
		if (listOfQuoteSlice.size() >= periodLength) {
			commonAnlaysisData.setAnalysisData(listOfQuoteSlice);
			indicatorGroup.setDataSet(listOfQuoteSlice, periodLength);
			indicatorGroup.analyize();
			signalGroup.generateSignals(commonAnlaysisData, indicatorGroup, periodLength);

			StrategyResponse strategyResponse = strategy.informStrategy(indicatorGroup, signalGroup, listOfQuoteSlice);
		
			handleStrategyResponse(strategyResponse);

			if (algorithmMode.displayChart) {
				algorithmChart.addChartPointData(quoteSlice, strategy.signal, signalGroup);
			}
			
			if (algorithmMode.displayTable) {
				algorithmTable.addTableRow(listOfQuoteSlice, strategy.signal, signalGroup, strategyResponse);
			}
			
			finishedReceiverOfQuoteSlice();
			periodLength = StrategyHelper.getUpdatedPeriodLength(quoteSlice.dateTime, exchange, periodLength, strategy.strategyOptions);
		}
	}

	@Override
	public synchronized void endOfFeed(Symbol symbol) {
		if (algorithmMode.displayChart) {
			algorithmChart.display();
		}
		if (algorithmMode.displayTable) {
			algorithmTable.display();
		}
		if (algorithmListener != null) {
			algorithmListener.endOfAlgorithm();
		}
	}
}
