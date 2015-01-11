package com.autoStock.algorithm;

import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.premise.PremiseOfOHLC;
import com.autoStock.signal.SignalDefinitions.SignalMetricType;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.strategy.StrategyOfTest;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
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
			setAnalyzeAndActive(ListTools.getList(Arrays.asList(new SignalMetricType[]{SignalMetricType.metric_cci, SignalMetricType.metric_uo, SignalMetricType.metric_willr, SignalMetricType.metric_adx, SignalMetricType.metric_di, SignalMetricType.metric_roc})), strategyBase.strategyOptions.listOfSignalMetricType);
			//setAnalyzeAndActive(SignalMetricType.asList(), strategyBase.strategyOptions.listOfSignalMetricType);
		}else{
			setAnalyzeAndActive(SignalMetricType.asList(), strategyBase.strategyOptions.listOfSignalMetricType);
		}

		initialize();
		
		if (strategyBase.strategyOptions.prefillEnabled){
			prefill();
		}
		
		if (strategyBase.strategyOptions.premiseEnabled){
			
//			Co.println("--> Today: " + DateTools.getPrettyDate(startingDate));
//			Co.println("--> Earliest weekday: " + DateTools.getPrettyDate(DateTools.getFirstWeekdayBefore(startingDate)));
			
			premiseController.reset();
			//premiseController.addPremise(new PremiseOfOHLC(exchange, symbol, DateTools.getFirstWeekdayBefore(startingDate), Resolution.min_30, 3));
			//premiseController.addPremise(new PremiseOfOHLC(exchange, symbol, DateTools.getFirstWeekdayBefore(startingDate), Resolution.day, 5));
			//premiseController.determinePremise();
//			Co.println("--> Premise...");
			
			int length = 0;
			
			for (EncogFrame ef : premiseController.getEncogFrames()){
				length += ef.asDoubleList().size();
			}
			
//			Co.println("--> Length: " + length);
		}
	}

	@Override
	public void receiveQuoteSlice(QuoteSlice quoteSlice) {
		receivedQuoteSlice(quoteSlice);
			
		if (listOfQuoteSlice.size() >= getPeriodLength()) {
			//Co.print("\n --> QS: " + quoteSlice.toString());
			
			if (signalCache != null && signalCache.isAvailable()){
				signalCache.setToQuoteSlice(quoteSlice, receiveIndex);
				signalGroup.generateSignalsCached();
			}else{
				commonAnalysisData.setAnalysisData(listOfQuoteSlice);
				indicatorGroup.setDataSet();
				indicatorGroup.analyze();
				signalGroup.generateSignals(commonAnalysisData, position);
				signalGroup.processEncog(premiseController.getEncogFrames());
			}
			
			baseInformStrategy(quoteSlice);
			finishedReceiveQuoteSlice();
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
