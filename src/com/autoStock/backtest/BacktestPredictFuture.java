/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.temporal.TemporalDataDescription;
import org.encog.ml.data.temporal.TemporalDataDescription.Type;
import org.encog.ml.data.temporal.TemporalMLDataSet;
import org.encog.ml.data.temporal.TemporalPoint;
import org.encog.ml.train.MLTrain;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.time.TimeUnit;

import com.autoStock.Co;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.StringTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;
import com.autoStock.misc.Pair;

/**
 * @author Kevin
 *
 */
public class BacktestPredictFuture {
	private static final int INPUT_POINT_SIZE = 4;
	private static final int INPUT_POINTS = 10;
	private static final int OUTPUT_POINT_SIZE = 4;
	private static final int OUTPUT_POINTS = 1;
	private static final int IDEAL_OFFSET = 8;
	private static final int INPUT_NEURONS = INPUT_POINTS * INPUT_POINT_SIZE;
	private static final int OUTPUT_NEURONS = OUTPUT_POINTS * OUTPUT_POINT_SIZE; 
	private ArrayList<PredictionResult> listOfPredictionResult = new ArrayList<PredictionResult>();
	
	private static class PredictionResult {
		public int countCorrect = 0;
		public int countIncorrect = 0;
		
		public PredictionResult(int countCorrect, int countIncorrect) {
			this.countCorrect = countCorrect;
			this.countIncorrect = countIncorrect;
		}
	}
	
	public void run(){
		// Load the data
		HistoricalData historicalDataIS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("02/03/2014"), DateTools.getDateFromString("04/01/2014"), Resolution.min);
		historicalDataIS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataIS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataIS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataIS.endDate)));
		
		HistoricalData historicalDataOS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("09/08/2014"), DateTools.getDateFromString("09/08/2014"), Resolution.min);
		historicalDataOS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsOS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataOS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataOS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataOS.endDate)));

		Co.println("--> Size: " + listOfResultsIS.size() + ", " + listOfResultsOS.size());
		
		BasicMLDataSet dataSet = new BasicMLDataSet();
		
		// Build the input & ideal lists as % deltas
		ArrayList<Double> percentChangeWindow = new ArrayList<Double>();
		ArrayList<Pair<DbStockHistoricalPrice, MLData>> priceIdealList = new ArrayList<>();
		
		for (DbStockHistoricalPrice slice : listOfResultsIS){
//			Co.println("--> Slice: (" + sequence + ")" + slice.dateTime + ", " + slice.priceClose);
			MathTools.genPercentChangeList(percentChangeWindow, IDEAL_OFFSET, slice.priceOpen, slice.priceHigh, slice.priceLow, slice.priceClose);
			priceIdealList.add(new Pair<DbStockHistoricalPrice, MLData>(slice, new BasicMLData(ArrayTools.getDoubleArray(ListTools.getLast(percentChangeWindow, 4)))));
		}
		
		//Remove the fist nW as it will be 0
		Co.println("--> A1: " + percentChangeWindow.size());
		percentChangeWindow = ListTools.subList(percentChangeWindow, INPUT_NEURONS + ((IDEAL_OFFSET -1) * INPUT_POINTS), -1);
		priceIdealList = ListTools.subList(priceIdealList, INPUT_POINTS, -1);
		Co.println("--> A2: " + percentChangeWindow.size());
		Co.println("--> A3 ideal: " + priceIdealList.size());
	
		// Setup MLData accordingly
		for (int i=0; i<percentChangeWindow.size() - INPUT_NEURONS - OUTPUT_NEURONS; i++){
			MLData input = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i, i + INPUT_NEURONS)));
			MLData ideal = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i + INPUT_NEURONS,  i + INPUT_NEURONS + OUTPUT_NEURONS)));
			
			dataSet.add(input, ideal);
		}
		
//		for (Pair<DbStockHistoricalPrice, MLData> item : priceIdealList){
//			if (item.second != null){
//				Co.println("--> Price close, predication: " + item.first.dateTime + ", " + item.first.priceClose + ", " + item.second.getData(3) + " = " + ((item.first.priceClose * (item.second.getData(3)/100)) + item.first.priceClose));
//			}
//		}
		
		// Train the network
		BasicNetwork network = getMLNetwork(INPUT_NEURONS, OUTPUT_NEURONS);
		MLTrain train = new ResilientPropagation(network, dataSet);
		//((ResilientPropagation)train).setDroupoutRate(0.5f);
	
		for (int i=0; i<1000; i++){
			train.iteration();
			Co.println(i + "." + " " + train.getError() * 1000);
		}
		
		// Test the network
		for (int i=0; i<percentChangeWindow.size() - INPUT_NEURONS - OUTPUT_NEURONS; i++){
			MLData input = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i, i + INPUT_NEURONS)));
			MLData ideal = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i + INPUT_NEURONS,  i + INPUT_NEURONS + OUTPUT_NEURONS)));
			MLData computed = network.compute(input);
			
			for (int c=0; c<OUTPUT_POINTS; c++){
				Co.println("--> c: " + c);
				for (int d=0; d<OUTPUT_POINT_SIZE; d++){
					Co.println("--> Computed, ideal: " + computed.getData(c+d) + " -> " + ideal.getData(c+d));	
				}
			}
		}
	}
	
	public void getPercentChange(ArrayList<Double> percentChangeWindow, int toIndex){
		
	}
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputSize);
		pattern.addHiddenLayer(inputSize/2);
		pattern.addHiddenLayer(inputSize/3);
		pattern.setOutputNeurons(outputSize);
		pattern.setActivationFunction(new ActivationTANH());
		pattern.setActivationOutput(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}
}
