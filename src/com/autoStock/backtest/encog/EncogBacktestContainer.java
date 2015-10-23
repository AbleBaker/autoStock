package com.autoStock.backtest.encog;

import java.util.ArrayList;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.account.AccountProvider;
import com.autoStock.account.BasicAccount;
import com.autoStock.algorithm.DummyAlgorithm;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmListener;
import com.autoStock.algorithm.extras.StrategyOptionsOverride;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
import com.autoStock.backtest.BacktestEvaluationWriter;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.backtest.encog.TrainEncogSignal.EncogNetworkType;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class EncogBacktestContainer {
	private static boolean USE_SO_OVERRIDE = true;
	public DummyAlgorithm algorithm;
	public Symbol symbol;
	public Exchange exchange;
	public Date dateStart;
	public Date dateEnd;
	private double bestResult = 0;
	private TrainEncogSignal trainEncogSignal;
	private HistoricalData historicalData;
	private int currentDay;
	private static enum Mode {day_over_day, full}
	private final Mode MODE = Mode.day_over_day;

	public EncogBacktestContainer(Symbol symbol, Exchange exchange, Date dateStart, Date dateEnd) {
		this.symbol = symbol;
		this.exchange = exchange;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		
		this.historicalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min);
		this.historicalData.setStartAndEndDatesToExchange();
	
		algorithm = new DummyAlgorithm(exchange, symbol, AlgorithmMode.mode_backtest_with_adjustment, new BasicAccount(AccountProvider.defaultBalance));
	}
	
	public void runBacktest(){
		StrategyOptionsOverride strategyOptionsOverride = new StrategyOptionsOverride() {
			@Override
			public void override(StrategyOptions strategyOptions) {
				//Looser training
				strategyOptions.disableAfterYield.value = 1000d;
				strategyOptions.enableContext = false;
				strategyOptions.enablePremise = false;
			}
		};
		
		if (MODE == Mode.full){
			trainEncogSignal = new TrainEncogSignal(AlgorithmModel.getEmptyModel(), historicalData, false, "full");
			blankNetwork();
			trainEncogSignal.execute(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, USE_SO_OVERRIDE ? strategyOptionsOverride : null), bestResult);
			trainEncogSignal.getTrainer().saveNetwork();
		}else{
			ArrayList<HistoricalData> listOfHistoricalData = BacktestUtils.getHistoricalDataListForDates(historicalData);
			
			for (HistoricalData historicalDataIn : listOfHistoricalData){
				trainEncogSignal = new TrainEncogSignal(AlgorithmModel.getEmptyModel(), historicalDataIn, false, "day-" + DateTools.getEncogDate(historicalDataIn.startDate));
				blankNetwork();
				trainEncogSignal.execute(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, USE_SO_OVERRIDE ? strategyOptionsOverride : null), bestResult);
				trainEncogSignal.getTrainer().saveNetwork();
				currentDay++;
			}
		}
	}
	
	private void blankNetwork(){
		Co.println("--> Blanking the network... ");
		if (SignalOfEncog.encogNetworkType == EncogNetworkType.basic){
			trainEncogSignal.getTrainer().saveNetwork();
		}else if (SignalOfEncog.encogNetworkType == EncogNetworkType.neat){
			trainEncogSignal.setDetails(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol));
			for (int i=0; i<TrainEncogSignal.TRAINING_ITERATIONS; i++){
				trainEncogSignal.getTrainer().train(1, 0);
				if (trainEncogSignal.getTrainer().bestScore != 0){trainEncogSignal.getTrainer().saveNetwork(); break;}
			}
		}
	}
}
