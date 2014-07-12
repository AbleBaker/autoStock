package com.autoStock.backtest.encog;

import java.util.ArrayList;

import org.encog.ml.MLRegression;
import org.encog.neural.networks.training.CalculateScore;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmRemodeler;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
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
public class EncogScoreProvider implements CalculateScore {
	private HistoricalData historicalData;	
	public static long runCount;
	private AlgorithmModel algorithmModel;
	public static ArrayList<EncogTest> listOfEncogTest = new ArrayList<EncogTest>();
	
	public void setDetails(AlgorithmModel algorithmModel, HistoricalData historicalData){
		this.algorithmModel = algorithmModel;
		this.historicalData = historicalData;
	}
	
	@Override
	public synchronized double calculateScore(MLRegression network) {
//		Co.println("--> Calculate score... ");
//		Co.println(BacktestEvaluationReader.getPrecomputedEvaluation(exchange, symbol).toString());
		
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		new AlgorithmRemodeler(singleBacktest.backtestContainer.algorithm, algorithmModel).remodel(true, true, true, false);
		singleBacktest.selfPopulateBacktestData();
//		singleBacktest.remodel(AlgorithmModel.getCurrentAlgorithmModel(singleBacktest.backtestContainer.algorithm));
		singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork(network);
		singleBacktest.runBacktest();
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer);
		
		String table = null;
		if (singleBacktest.backtestContainer.algorithm.tableForAlgorithm != null){
			table = new TableController().getTable(AsciiTables.algorithm_test, singleBacktest.backtestContainer.algorithm.tableForAlgorithm.getDisplayRows());
		}
		
		if (backtestEvaluation.getScore() > TrainEncogSignal.bestScore && backtestEvaluation.getScore() != 0){
			listOfEncogTest.add(new EncogTest(network, backtestEvaluation, table));
		}
		
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
