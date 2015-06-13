/**
 * 
 */
package com.autoStock.backtest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.TunnelRefusedException;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.temporal.TemporalDataDescription;
import org.encog.ml.data.temporal.TemporalDataDescription.Type;
import org.encog.ml.data.temporal.TemporalMLDataSet;
import org.encog.ml.data.temporal.TemporalPoint;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
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
import com.autoStock.tools.DateTools;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 *
 */
public class BacktestPredictFuture {
	private static final int POINT_SIZE = 4;
	private static final int INPUT_POINTS = 10;
	private static final int OUTPUT_NEURONS = 1;
	private static final int OFFSET = 0;
	private static final int INPUT_NEURONS = INPUT_POINTS * POINT_SIZE;
	private DecimalFormat df = new DecimalFormat("#.##");
	private ActivationFunction activationFunction = new ActivationTANH();
	private ArrayList<PredictionResult> listOfPredictionResult = new ArrayList<PredictionResult>();
	
	private NormalizedField nf = new NormalizedField(NormalizationAction.Normalize, "Normalizer", 0, 0, 1, -1);
	
	private static class PredictionResult {
		public int countCorrect = 0;
		public int countIncorrect = 0;
		public double devation = 0;
		
		public PredictionResult(int countCorrect, int countIncorrect) {
			this.countCorrect = countCorrect;
			this.countIncorrect = countIncorrect;
		}
	}
	
	public void run(){
		// Temporal data set formats delta's and percent change automatically
		TemporalMLDataSet tds = new TemporalMLDataSet(INPUT_POINTS, OUTPUT_NEURONS);
		tds.addDescription(new TemporalDataDescription(activationFunction, Type.PERCENT_CHANGE, true, false));
		tds.addDescription(new TemporalDataDescription(activationFunction, Type.PERCENT_CHANGE, true, false));
		tds.addDescription(new TemporalDataDescription(activationFunction, Type.PERCENT_CHANGE, true, false));
		tds.addDescription(new TemporalDataDescription(activationFunction, Type.PERCENT_CHANGE, true, true));
//		tds.setSequenceGrandularity(TimeUnit.MINUTES);
		
		HistoricalData historicalDataIS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("09/08/2014"), DateTools.getDateFromString("09/11/2014"), Resolution.min);
		historicalDataIS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataIS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataIS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataIS.endDate)));
		
		HistoricalData historicalDataOS = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("09/12/2014"), DateTools.getDateFromString("09/12/2014"), Resolution.min);
		historicalDataOS.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsOS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalDataOS.symbol.symbolName), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").exchangeName), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalDataOS.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalDataOS.endDate)));

		Co.println("--> Size: " + listOfResultsIS.size() + ", " + listOfResultsOS.size());
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		for (DbStockHistoricalPrice slice : listOfResultsIS){
			min = Math.min(min, slice.priceLow);
			max = Math.max(max, slice.priceHigh);
		}
		
		for (DbStockHistoricalPrice slice : listOfResultsOS){
			min = Math.min(min, slice.priceLow);
			max = Math.max(max, slice.priceHigh);
		}
		
		nf.setActualLow(MathTools.roundOut((int) min, 10) - 10);
		nf.setActualHigh(MathTools.roundOut((int) max, 10));
		
		Co.println("--> Min / max? " + min + ", " + max + " -> " + nf.getActualHigh() + ", " + nf.getActualLow());
		
		for (DbStockHistoricalPrice slice : listOfResultsIS){
//			Co.println("--> Slice: " + slice.dateTime + ", " + slice.priceClose);
			
			TemporalPoint tp = new TemporalPoint(POINT_SIZE);
//			tp.setData(0, nf.normalize(slice.priceOpen));
//			tp.setData(1, nf.normalize(slice.priceHigh));
//			tp.setData(2, nf.normalize(slice.priceLow));
//			tp.setData(3, nf.normalize(slice.priceClose));
//			
			tp.setData(0, slice.priceOpen);
			tp.setData(1, slice.priceHigh);
			tp.setData(2, slice.priceLow);
			tp.setData(3, slice.priceClose);
			tds.getPoints().add(tp);
		}
		
		tds.generate();
		
		BasicNetwork network = getMLNetwork(INPUT_NEURONS, OUTPUT_NEURONS);
		
		MLTrain train = new ResilientPropagation(network, tds);
		
//		MLTrain train = new NeuralPSO(network, new NguyenWidrowRandomizer(), new TrainingSetScore(tds), 256);
		//train.addStrategy(new HybridStrategy(new NeuralSimulatedAnnealing(network, new TrainingSetScore(tds), 10, 2, 128), 0.010f, 100, 100));
		//train.addStrategy(new HybridStrategy(new NeuralGeneticAlgorithm(network, new NguyenWidrowRandomizer(), new TrainingSetScore(tds), 128, 0.010f, 0.25f), 0.10, 100, 100));
		
		for (int i=0; i<1000; i++){
			train.iteration();
			System.out.println("" + train.getError() * 1000);
		}
		
//		Co.println("--> Inputs: " + network.getInputCount());
//		
		testOutput(0, network, listOfResultsOS);
		testOutput(1, network, listOfResultsOS);
		testOutput(2, network, listOfResultsOS);
		testOutput(3, network, listOfResultsIS);
		testOutput(4, network, listOfResultsIS);
		testOutput(5, network, listOfResultsIS);
		
		Co.println("--> Out of sample");
		
//		testOutput(0, network, listOfResultsOS);
//		testOutput(1, network, listOfResultsOS);
//		testOutput(2, network, listOfResultsOS);
//		testOutput(3, network, listOfResultsOS);
//		testOutput(4, network, listOfResultsOS);
//		testOutput(5, network, listOfResultsOS);
//		testOutput(6, network, listOfResultsOS);
//		testOutput(7, network, listOfResultsOS);
		
		int countCorrect = 0;
		int countIncorrect = 0;
		
		for (PredictionResult result : listOfPredictionResult){
			Co.println("--> Correct / Incorrect: " + result.countCorrect + ", " + result.countIncorrect);
			countCorrect += result.countCorrect;
			countIncorrect += result.countIncorrect;
		}
		
		Co.println("--> TOTAL Correct / Incorrect: " + countCorrect + ", " + countIncorrect + " = " + ((double) ((double)countCorrect / (double)listOfPredictionResult.size()) * 10) + " %");
	}
	
	private void testOutput(int index, BasicNetwork network, List<DbStockHistoricalPrice> listOfResults){
		List<DbStockHistoricalPrice> listForInput = listOfResults.subList(index * INPUT_POINTS, (index+1) * INPUT_POINTS);
		List<DbStockHistoricalPrice> listOfActual = listOfResults.subList(((index+1) * INPUT_POINTS) + OFFSET, (((index+1) * INPUT_POINTS) + OUTPUT_NEURONS) + OFFSET);
		
		Co.println("--> Start, end: " + listForInput.get(0).dateTime + ", " + listForInput.get(listForInput.size()-1).dateTime);
		Co.println("--> Predict start, end: " + listOfActual.get(0).dateTime + ", " + listOfActual.get(listOfActual.size()-1).dateTime);
		
		PredictionResult result = testOutput(listForInput, listOfActual, network);
		listOfPredictionResult.add(result);
	}
	
	private PredictionResult testOutput(List<DbStockHistoricalPrice> listForInput, List<DbStockHistoricalPrice> listOfActual, BasicNetwork network) {
		MLData input = new BasicMLData(INPUT_NEURONS);
		
		Co.print("--> Output for input: ");
		
		int index = 0;
		double lastPrice = listForInput.get(listForInput.size()-1).priceClose;
		
		for (DbStockHistoricalPrice slice : listForInput){
			addPoint(input, index, new double[]{slice.priceOpen, slice.priceHigh, slice.priceLow, slice.priceClose});
			index += POINT_SIZE;
			Co.print("" + slice.priceClose + " ");
		}
		
		for (double value : input.getData()){
			if (value == 0){
//				throw new IllegalStateException();
			}
		}
	
//		Co.println("--> Input length: " + input.size());
		
		MLData output = network.compute(input);
		
		int actualIndex = 0;
		int countCorrect = 0;
		int countIncorrect = 0;
		
		Co.println("");
		
		for (DbStockHistoricalPrice slice : listOfActual){
			double predicted = lastPrice + (lastPrice * output.getData(actualIndex)); // nf.deNormalize(output.getData(actualIndex));
			double actual = slice.priceClose;
			boolean direction = false;
			
			if (predicted >= lastPrice && actual >= lastPrice){direction = true;}
			if (predicted <= lastPrice && actual <= lastPrice){direction = true;}
			
			Co.print("--> Predicted " + output.getData(actualIndex) + ", " + predicted); //df.format(predicted));
			Co.print(" / Actual: " + actual);
//			Co.print(" / Dev: " + (actual / predicted));
			Co.println("  " + direction + "\n");
			
			if (direction){countCorrect++;}else{countIncorrect++;}
			
			actualIndex++;
		}
		
		return new PredictionResult(countCorrect, countIncorrect);
	}
	
	private void addPoint(MLData input, int index, double[] values){
		for (double value : values){
			input.add(index, nf.normalize(value));
			index++;
		}
	}
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
//		ElmanPattern pattern = new ElmanPattern();
//		JordanPattern pattern = new JordanPattern();
		
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
