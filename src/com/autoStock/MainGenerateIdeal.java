/**
 * 
 */
package com.autoStock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.MLRegression;
import org.encog.ml.anneal.SimulatedAnnealing;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.nm.NelderMeadTraining;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.propagation.scg.ScaledConjugateGradient;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.JordanPattern;
import org.encog.neural.pattern.RadialBasisPattern;

import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.algorithm.core.AlgorithmListener;
import com.autoStock.algorithm.extras.StrategyOptionsOverride;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.BacktestEvaluationReader;
import com.autoStock.backtest.ListenerOfBacktest;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.cache.GenericPersister;
import com.autoStock.chart.CombinedLineChart.ClickPoint;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.CallbackLock;
import com.autoStock.internal.Global;
import com.autoStock.misc.Pair;
import com.autoStock.position.PositionGovernorResponseStatus;
import com.autoStock.signal.SignalDefinitions.SignalPointType;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogInputWindow;
import com.autoStock.signal.extras.EncogNetworkGenerator;
import com.autoStock.signal.extras.EncogNetworkProvider;
import com.autoStock.signal.extras.EncogSubframe;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.strategy.StrategyOptions;
import com.autoStock.strategy.StrategyResponse;
import com.autoStock.strategy.StrategyResponse.StrategyActionCause;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 *
 */
public class MainGenerateIdeal implements AlgorithmListener, ListenerOfBacktest {
	private GenericPersister genericPersister = new GenericPersister();
	private ArrayList<ClickPoint> listOfClickPoint;
	private SingleBacktest singleBacktest;
	private ArrayList<ArrayList<Double>> listOfInput = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> listOfIdeal = new ArrayList<ArrayList<Double>>();
	private Symbol symbol = new Symbol("MS", SecurityType.type_stock);
	private Exchange exchange = new Exchange("NYSE");
	private Date dateStart = DateTools.getDateFromString("09/02/2014");
	private Date dateEnd = DateTools.getDateFromString("09/30/2014");
	private StrategyOptionsOverride soo;
	
	public void run(){
		Global.callbackLock.requestLock();
		
		// Load the data
		HistoricalData historicalData = new HistoricalData(exchange, symbol, dateStart, dateEnd, Resolution.min);
		historicalData.setStartAndEndDatesToExchange();
		
		soo = new StrategyOptionsOverride() {
			@Override
			public void override(StrategyOptions strategyOptions) {
				strategyOptions.enableContext = true;
				strategyOptions.enablePremise = false;
			}
		};
		
		singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest);
		singleBacktest.setListenerOfBacktestCompleted(this);
		singleBacktest.backtestContainer.algorithm.setAlgorithmListener(this);
		singleBacktest.remodel(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, soo));
		
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		
		Co.print(new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer).toString());	
	}
	
	@Override
	public void initialize(Date startingDate) {
		BasicNetwork basicNetwork = new EncogNetworkProvider().getBasicNetwork(exchange.exchangeName + "-" + symbol.symbolName + "-day-" + DateTools.getEncogDate(startingDate));
		if (basicNetwork == null){throw new IllegalStateException("Couldn't find network to load");}
		singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork(basicNetwork, 0);
	}

	@Override
	public void receiveStrategyResponse(StrategyResponse strategyResponse) {
		Co.println("--> Received strategy response");
	}
	
	boolean haveChange = false;
	PositionGovernorResponseStatus status = PositionGovernorResponseStatus.none;

	@Override
	public void receiveChangedStrategyResponse(StrategyResponse strategyResponse) {
		Co.println("--> Received changed strategy response: " + strategyResponse.positionGovernorResponse.status.name());
		status = strategyResponse.positionGovernorResponse.status;
		haveChange = true;
	}

	@Override
	public void receiveTick(QuoteSlice quote, int receivedIndex, int processedIndex, boolean processed) {
		Co.println("--> Received tick " + receivedIndex + ", " + processedIndex);
		
		EncogInputWindow eiw = singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.getInputWindow();
		ArrayList<Double> listOfIdealOutputs = new ArrayList<Double>();
		
		if (processed && eiw != null){			
			if (haveChange && status != PositionGovernorResponseStatus.none){
				haveChange = false;
				Co.println("--> Have change at tick: " + DateTools.getPrettyDate(quote.dateTime) + " EIW: " + eiw.getHash());
				if (status == PositionGovernorResponseStatus.changed_long_entry){
					listOfIdealOutputs.add(1d);
					listOfIdealOutputs.add(-1d);
					listOfIdealOutputs.add(-1d);
				}
				else if (status == PositionGovernorResponseStatus.changed_short_entry){
					listOfIdealOutputs.add(-1d);
					listOfIdealOutputs.add(1d);
					listOfIdealOutputs.add(-1d);
				}
				else if (status == PositionGovernorResponseStatus.changed_long_exit || status == PositionGovernorResponseStatus.changed_short_exit){
					listOfIdealOutputs.add(-1d);
					listOfIdealOutputs.add(-1d);
					listOfIdealOutputs.add(1d);
				}
				else { throw new IllegalStateException(); }
			}else{
				listOfIdealOutputs.add(-1d);
				listOfIdealOutputs.add(-1d);
				listOfIdealOutputs.add(-1d);
			}
			
			listOfInput.add(ListTools.getListFromArray(eiw.getAsWindow(true)));
			listOfIdeal.add(listOfIdealOutputs);
		}
	}
	
	int currentDay = 0;

	@Override
	public void endOfAlgorithm() {		
		//Co.println("--> Have input list of: " + listOfInput.size());
		//Co.println("--> Have ideal list of: " + listOfIdeal.size());
//		Co.println("***** Current day: " + currentDay);
		currentDay++;
	}
	
	private BasicNetwork getNetwork(){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(SignalOfEncog.getInputWindowLength());
		pattern.addHiddenLayer(75);
		pattern.addHiddenLayer(25);
		pattern.setOutputNeurons(3);
		pattern.setActivationFunction(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}

	@Override
	public void onCompleted(Symbol symbol, AlgorithmBase algorithmBase) {
		Co.println("--> Backtest completed!");
		Co.println("--> Have input list of: " + listOfInput.size());
		Co.println("--> Have ideal list of: " + listOfIdeal.size());
		
		BasicMLDataSet dataSet = new BasicMLDataSet();
		
		for (int i=0; i<listOfInput.size(); i++){
			dataSet.add(new BasicMLData(ArrayTools.getArrayFromListOfDouble(listOfInput.get(i))), new BasicMLData(ArrayTools.getArrayFromListOfDouble(listOfIdeal.get(i))));
		}
		
		// Train the network
		BasicNetwork network =  getNetwork(); //EncogNetworkGenerator.getBasicNetwork(SignalOfEncog.getInputWindowLength(), 3);
		new NguyenWidrowRandomizer().randomize(network);

		MLTrain train = new ManhattanPropagation(network, dataSet, 0.025);
//		MLTrain train = new ResilientPropagation(network, dataSet, 0.01, 10);
//		MLTrain train = NEATUtil.constructNEATTrainer(new TrainingSetScore(dataSet), SignalOfEncog.getInputWindowLength(), 3, 512);
//		MLTrain train = new NeuralPSO(network, dataSet);
//		train.addStrategy(new HybridStrategy(new NeuralPSO(network, dataSet), 0.100, 500, 500));
//		train.addStrategy(new HybridStrategy(new NeuralSimulatedAnnealing(network, new TrainingSetScore(dataSet), 10, 2, 100), 0.010, 500, 100));
	
		for (int i=0; i<2000; i++){
			train.iteration();
			System.out.println(i + " - " + train.getError() * 1000);
		}
		
//		train = new ResilientPropagation(network, dataSet);
//		
//		for (int i=0; i<10000; i++){
//			train.iteration();
//			System.out.println(i + " - " + train.getError() * 1000);
//		}
		
//		train = new NeuralSimulatedAnnealing(network, new TrainingSetScore(dataSet), 10, 2, 500);
//
//		for (int i=0; i<1000; i++){
//			train.iteration();
//			System.out.println("" + train.getError() * 1000);
//		}
		
//		dataSet.add(, new BasicMLData(ArrayTools.getArrayFromListOfDouble(listOfIdeal.get(i))));
		
		
		// Verficiation stage
		if (train.getError() * 1000 < 10){
			Co.println("--> Good score");
			
			HistoricalData historicalData = new HistoricalData(exchange, symbol, DateTools.getDateFromString("09/02/2014"), DateTools.getDateFromString("09/31/2014"), Resolution.min);
			historicalData.setStartAndEndDatesToExchange();
			
			SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
			singleBacktest.remodel(BacktestEvaluationReader.getPrecomputedModel(exchange, symbol, soo));
			singleBacktest.backtestContainer.algorithm.signalGroup.signalOfEncog.setNetwork((MLRegression) train.getMethod(), 0);
			singleBacktest.selfPopulateBacktestData();
			singleBacktest.runBacktest();
			
			Co.println(new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer).toString());
			
			new EncogNetworkProvider().saveNetwork(network, "NYSE-MS");
		}
	}
}
