package com.autoStock.encog;

import java.util.ArrayList;

import org.encog.ml.MLRegression;
import org.encog.neural.networks.training.CalculateScore;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.tables.TableController;
import com.autoStock.tables.TableDefinitions.AsciiTables;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class FeedScore implements CalculateScore {
	private Exchange exchange;
	private Symbol symbol;
	private HistoricalData historicalData;	
	public static long runCount;
	public static ArrayList<EncogTest> listOfEncogTest = new ArrayList<EncogTest>();
	
	public FeedScore(){
		exchange = new Exchange("NYSE");
		symbol = new Symbol("AIG", SecurityType.type_stock); 
		
		historicalData = new HistoricalData(exchange, symbol, DateTools.getDateFromString("03/05/2012"), DateTools.getDateFromString("03/08/2012"), Resolution.min);
		
		historicalData.startDate.setHours(historicalData.exchange.timeOpenForeign.hours);
		historicalData.startDate.setMinutes(historicalData.exchange.timeOpenForeign.minutes);
		historicalData.endDate.setHours(historicalData.exchange.timeCloseForeign.hours);
		historicalData.endDate.setMinutes(historicalData.exchange.timeCloseForeign.minutes);
	}
	
	@Override
	public synchronized double calculateScore(MLRegression network) {
//		Co.println("--> Calculate score... ");
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_silent); //See AlgorithmTest init()...
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.remodel(AlgorithmModel.getCurrentAlgorithmModel(singleBacktest.backtestContainer.algorithm));
		singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork(network);
		singleBacktest.runBacktest();
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
		
//		String table = null;
//		if (singleBacktest.backtestContainer.algorithm.algorithmTable != null){
//			table = new TableController().getTable(AsciiTables.algorithm_test, singleBacktest.backtestContainer.algorithm.algorithmTable.listOfDisplayRows);
//			singleBacktest.backtestContainer.algorithm.algorithmTable.listOfDisplayRows.clear();
//		}
//		
//		if (backtestEvaluation.getScore() >= SolveFeedTest.bestScore && backtestEvaluation.getScore() != 0){
//			listOfEncogTest.add(new EncogTest(network, backtestEvaluation, table));
//		}
		
		runCount++;
		
		return backtestEvaluation.getScore();
	}

	@Override
	public boolean shouldMinimize() {
		return false;
	}
	
	public static class EncogTest {
		public MLRegression network;
		public BacktestEvaluation backtestEvaluation;
		public String table;
		
		public EncogTest(MLRegression network, BacktestEvaluation backtestEvaluation, String table) {
			this.network = network;
			this.backtestEvaluation = backtestEvaluation;
			this.table = table;
		}
	}
}
