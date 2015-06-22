/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;
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
	private static final int POINT_SIZE = 4;
	private static final int INPUT_POINTS = 10;
	private static final int OUTPUT_POINTS = 1;
	private static final int IDEAL_OFFSET = 0;
	private static final int INPUT_NEURONS = INPUT_POINTS * POINT_SIZE;
	private static final int OUTPUT_NEURONS = OUTPUT_POINTS * POINT_SIZE;
	private ActivationFunction activationFunction = new ActivationTANH();
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
		HistoricalData historicalDataIS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("09/08/2014"), DateTools.getDateFromString("09/11/2014"), Resolution.min);
		historicalDataIS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataIS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataIS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataIS.endDate)));
		
		HistoricalData historicalDataOS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("09/12/2014"), DateTools.getDateFromString("09/12/2014"), Resolution.min);
		historicalDataOS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsOS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataOS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataOS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataOS.endDate)));

		Co.println("--> Size: " + listOfResultsIS.size() + ", " + listOfResultsOS.size());
		
		BasicMLDataSet dataSet = new BasicMLDataSet();
		
		int sequence = 0;
		
		ArrayList<Double> percentChangeWindow = new ArrayList<Double>();
		ArrayList<Pair<DbStockHistoricalPrice, MLData>> priceIdealList = new ArrayList<>();
		
		for (DbStockHistoricalPrice slice : listOfResultsIS){
			Co.println("--> Slice: (" + sequence + ")" + slice.dateTime + ", " + slice.priceClose);
			MathTools.getPercentChangeList(percentChangeWindow, slice.priceOpen, slice.priceHigh, slice.priceLow, slice.priceClose);
			
			if (sequence != 0 && sequence % INPUT_POINTS == 0){
				Co.println("--> Would add slice at: " + sequence);
				priceIdealList.add(new Pair<DbStockHistoricalPrice, MLData>(slice, null));
			}
			
			Co.println("");
			sequence++;
		}
		
		Co.println("--> A: " + percentChangeWindow.size());
		percentChangeWindow = ListTools.subList(percentChangeWindow, POINT_SIZE, -1);
		Co.println("--> A: " + percentChangeWindow.size() + ", " + priceIdealList.size());
		
		int sequenceAlign = 0;
		
		for (int i=0; i<percentChangeWindow.size() - INPUT_NEURONS; i += INPUT_NEURONS){
			MLData input = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i, i + INPUT_NEURONS)));
			MLData ideal = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i + INPUT_NEURONS,  i + INPUT_NEURONS + OUTPUT_NEURONS)));
			
			dataSet.add(input, ideal);
			
			priceIdealList.get(sequenceAlign).second = ideal;
			
			sequenceAlign++;
			
			Co.println("--> B " + sequenceAlign);
		}
		
		for (Pair<DbStockHistoricalPrice, MLData> item : priceIdealList){
			if (item.second != null){
				Co.println("--> Price close, predication: " + item.first.dateTime + ", " + item.first.priceClose + ", " + item.second.getData(3) + " = " + ((item.first.priceClose * (item.second.getData(3)/100)) + item.first.priceClose));
			}
		}
		
		BasicNetwork network = getMLNetwork(INPUT_NEURONS, OUTPUT_NEURONS);
		MLTrain train = new ResilientPropagation(network, dataSet);
	
		for (int i=0; i<10000; i++){
			train.iteration();
			System.out.println("" + train.getError() * 1000);
		}
		
		for (int i=0; i<percentChangeWindow.size() - INPUT_NEURONS; i += INPUT_NEURONS){
			MLData input = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i, i + INPUT_NEURONS)));
			MLData ideal = new BasicMLData(ArrayTools.getDoubleArray(percentChangeWindow.subList(i + INPUT_NEURONS + IDEAL_OFFSET,  i + INPUT_NEURONS + OUTPUT_NEURONS + IDEAL_OFFSET)));
			MLData computed = network.compute(input);
			
			for (int c=0; c<OUTPUT_POINTS; c++){
				Co.println("--> c: " + c);
				Co.println("--> Computed, ideal: " + computed.getData(c+0) + " -> " + ideal.getData(c+0));
				Co.println("--> Computed, ideal: " + computed.getData(c+1) + " -> " + ideal.getData(c+1));
				Co.println("--> Computed, ideal: " + computed.getData(c+2) + " -> " + ideal.getData(c+2));
				Co.println("--> Computed, ideal: " + computed.getData(c+3) + " -> " + ideal.getData(c+3));
			}
		}
	}
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputSize);
		pattern.addHiddenLayer(inputSize/2);
		pattern.addHiddenLayer(inputSize/3);
		pattern.addHiddenLayer(inputSize/4);
		pattern.setOutputNeurons(outputSize);
		pattern.setActivationFunction(activationFunction);
		return (BasicNetwork) pattern.generate();
	}
}
