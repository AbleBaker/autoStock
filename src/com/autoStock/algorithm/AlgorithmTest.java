package com.autoStock.algorithm;

import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.position.PositionOptions;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 * 
 */
public class AlgorithmTest extends AlgorithmBase {
	public AlgorithmTest(Exchange exchange, Symbol symbol, AlgorithmMode algorithmMode, BasicAccount basicAccount) {
		super(exchange, symbol, algorithmMode, basicAccount);
		setStrategy(new StrategyOfTest(this));
	}

	public void init(Date startingDate){
		this.startingDate = startingDate;
		
		if (algorithmMode == AlgorithmMode.mode_backtest_single){
			setAnalyzeAndActive(ListTools.getList(Arrays.asList(new SignalMetricType[]{SignalMetricType.metric_cci, SignalMetricType.metric_uo, SignalMetricType.metric_willr})), strategyBase.strategyOptions.listOfSignalMetricType);
			//setAnalyzeAndActive(SignalMetricType.asList(), strategyBase.strategyOptions.listOfSignalMetricType);
		}else{
			setAnalyzeAndActive(SignalMetricType.asList(), strategyBase.strategyOptions.listOfSignalMetricType);
		}

		initialize();
		
		if (strategyBase.strategyOptions.prefillEnabled){
			prefill();			
		}
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
			
		if (listOfQuoteSlice.size() >= getPeriodLength()) {
//			Co.print("\n --> QS: " + quoteSlice.dateTime);
//			if (quoteSlice.dateTime.equals(DateTools.getDateFromString("2012/03/09 09:59:00 AM"))){
//				Co.println("--> Debug...");
//			}
			
			commonAnalysisData.setAnalysisData(listOfQuoteSlice);
			indicatorGroup.setDataSet();
			indicatorGroup.analyze();
			signalGroup.setIndicatorGroup(indicatorGroup);
			signalGroup.generateSignals(commonAnalysisData, position);
//			
//			for (int i=0; i<indicatorGroup.indicatorOfRSI.results.arrayOfRSI.length; i++){
//				Co.print(" " + indicatorGroup.indicatorOfRSI.results.arrayOfRSI[i]);
//			}
//			
//			System.exit(0);
			
			StrategyResponse strategyResponse = strategyBase.informStrategy(indicatorGroup, signalGroup, listOfQuoteSlice, listOfStrategyResponse, position, new PositionOptions(this));
		
			handleStrategyResponse(strategyResponse);

			if (algorithmMode.displayChart) {
				algorithmChart.addChartPointData(firstQuoteSlice, quoteSlice, strategyResponse);
			}
			
			if (algorithmMode.displayTable) {
				tableForAlgorithm.addTableRow(listOfQuoteSlice, strategyBase.signal, signalGroup, strategyResponse, basicAccount);
			}
				
//			Co.println("--> Sizes: " + listOfQuoteSlice.size() + ", " + algorithmTable.listOfDisplayRows.size());
			
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
			Co.println("--> " + symbol.symbolName);
			new TableController().displayTable(AsciiTables.algorithm, tableForAlgorithm.getDisplayRows());
		}
		if (algorithmListener != null) {
			algorithmListener.endOfAlgorithm();
		}
	}
}
