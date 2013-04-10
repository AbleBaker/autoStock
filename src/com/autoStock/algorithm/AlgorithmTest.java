package com.autoStock.algorithm;

import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.position.PositionOptions;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase {
	public StrategyOfTest strategy = new StrategyOfTest(this);
	
	public AlgorithmTest(boolean canTrade, Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode, Date startingDate) {
		super(canTrade, exchange, symbol, algorithmMode, startingDate);
		
		if (algorithmMode == AlgorithmMode.mode_backtest_with_adjustment){
			listOfSignalMetricType = strategy.strategyOptions.listOfSignalMetricType;
		}else{
			listOfSignalMetricType.addAll(Arrays.asList(SignalMetricType.values()));	
		}
	
		initialize(strategy);
		
//		for (QuoteSlice quoteSlice : listOfQuoteSlice){
//			Co.println("--> Prefilled with: " + quoteSlice.priceClose);
//		}
		
		if (listOfQuoteSlice.size() > 0){
			firstQuoteSlice = listOfQuoteSlice.get(0);
		}
	}
	
	public void init(){
//		prefill();
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
		
		if (listOfQuoteSlice.size() >= getPeriodLength()) {
			
			Co.print("\n --> QS: " + quoteSlice.dateTime);
			
			commonAnalysisData.setAnalysisData(listOfQuoteSlice);
			indicatorGroup.setDataSet();
			indicatorGroup.analyize();
			signalGroup.generateSignals(commonAnalysisData, getPeriodLength());
			
//			Co.println("--> ");
//			
			for (int i=0; i<indicatorGroup.indicatorOfRSI.results.arrayOfRSI.length; i++){
				Co.print(" " + indicatorGroup.indicatorOfRSI.results.arrayOfRSI[i]);
			}
//			
//			System.exit(0);

			StrategyResponse strategyResponse = strategy.informStrategy(indicatorGroup, signalGroup, listOfQuoteSlice, listOfStrategyResponse, position, new PositionOptions(this));
		
			handleStrategyResponse(strategyResponse);

			if (algorithmMode.displayChart) {
				algorithmChart.addChartPointData(firstQuoteSlice, quoteSlice, strategyResponse);
			}
			
			if (algorithmMode.displayTable) {
				algorithmTable.addTableRow(listOfQuoteSlice, strategy.signal, signalGroup, strategyResponse);
			}
			
//			periodLength = StrategyHelper.getUpdatedPeriodLength(quoteSlice.dateTime, exchange, periodLength, strategy.strategyOptions);
			finishedReceiverOfQuoteSlice();
		}
	}

	@Override
	public void endOfFeed(Symbol symbol) {
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
