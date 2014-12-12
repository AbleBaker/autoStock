/**
 * 
 */
package com.autoStock.backtest;

import java.util.ArrayList;
import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.MLMethod;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.temporal.TemporalDataDescription;
import org.encog.ml.data.temporal.TemporalPoint;
import org.encog.ml.data.temporal.TemporalDataDescription.Type;
import org.encog.ml.data.temporal.TemporalMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.genetic.NeuralGeneticAlgorithm;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.pso.NeuralPSO;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;
import org.encog.util.simple.EncogUtility;

import com.autoStock.Co;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.Config;
import com.autoStock.signal.signalMetrics.SignalOfEncog;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 *
 */
public class BacktestPredictFuture {
	private NormalizedField nf = new NormalizedField(NormalizationAction.Normalize, "Normalizer", 20, 17, 1, 0);
	
	public void fun(){
		TemporalMLDataSet tds = new TemporalMLDataSet(10, 1);
		tds.addDescription(new TemporalDataDescription(new ActivationTANH(), Type.RAW, true, true));
		tds.addDescription(new TemporalDataDescription(new ActivationTANH(), Type.RAW, true, false));
		tds.addDescription(new TemporalDataDescription(new ActivationTANH(), Type.RAW, true, false));
		tds.addDescription(new TemporalDataDescription(new ActivationTANH(), Type.RAW, true, false));
		
		HistoricalData historicalData = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), DateTools.getDateFromString("03/05/2012"), DateTools.getDateFromString("03/09/2012"), Resolution.min);
		historicalData.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));

		Co.println("--> Size: " + listOfResults.size());
		
		for (DbStockHistoricalPrice slice : listOfResults){
			Co.println("--> Slice: " + slice.dateTime + ", " + slice.priceClose);
			
			TemporalPoint tp = new TemporalPoint(4);
			tp.setData(0, nf.normalize(slice.priceClose));
			tp.setData(1, nf.normalize(slice.priceOpen));
			tp.setData(2, nf.normalize(slice.priceHigh));
			tp.setData(3, nf.normalize(slice.priceLow));
			tds.getPoints().add(tp);
		}
		
		tds.generate();
		
		BasicNetwork network = getMLNetwork(40, 1);
		
		MLTrain train = new ResilientPropagation(network, tds);
		
//		MLTrain train = new NeuralPSO(network, new NguyenWidrowRandomizer(), new TrainingSetScore(tds), 256);
		//train.addStrategy(new HybridStrategy(new NeuralSimulatedAnnealing(network, new TrainingSetScore(tds), 10, 2, 128), 0.010f, 100, 100));
		//train.addStrategy(new HybridStrategy(new NeuralGeneticAlgorithm(network, new NguyenWidrowRandomizer(), new TrainingSetScore(tds), 128, 0.010f, 0.25f), 0.10, 100, 100));
		
		for (int i=0; i<1000; i++){
			train.iteration();
			System.out.println("" + train.getError() * 1000);
		}
		
		Co.println("--> Inputs: " + network.getInputCount());
		
		testOutput(listOfResults.subList(30, 40), network, listOfResults.get(40));
		testOutput(listOfResults.subList(40, 50), network, listOfResults.get(50));
		testOutput(listOfResults.subList(50, 60), network, listOfResults.get(60));		
	}
	
	private void testOutput(List<DbStockHistoricalPrice> list, BasicNetwork network, DbStockHistoricalPrice actual) {
		MLData input = new BasicMLData(40);
		
		int index = 0;
		
//		Co.println("--> List: " + list.size());
		
		Co.print("--> Output for input: ");
		
		for (DbStockHistoricalPrice slice : list){
			input.add(index, nf.normalize(slice.priceClose)); 			index++;
			input.add(index, nf.normalize(slice.priceOpen)); 			index++;
			input.add(index, nf.normalize(slice.priceHigh)); 			index++;
			input.add(index, nf.normalize(slice.priceLow)); 			index++;
			Co.print("" + slice.priceClose + " ");
		}
		
		for (double value : input.getData()){
			if (value == 0){
				throw new IllegalStateException();
			}
		}
	
//		Co.println("--> Input length: " + input.size());
		
		MLData output = network.compute(input);
		Co.print(" --> " + nf.deNormalize(output.getData(0)));
		Co.println(" / Actual: " + actual.priceClose);
	}
	
	
	public BasicNetwork getMLNetwork(int inputSize, int outputSize){
//		ElmanPattern pattern = new ElmanPattern();
//		JordanPattern pattern = new JordanPattern();
		
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(inputSize);
		pattern.addHiddenLayer(inputSize/2);
		pattern.addHiddenLayer(inputSize/3);
		pattern.setOutputNeurons(outputSize);
		pattern.setActivationFunction(new ActivationTANH());
		return (BasicNetwork) pattern.generate();
	}
}
