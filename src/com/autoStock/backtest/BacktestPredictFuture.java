/**
 * 
 */
package com.autoStock.backtest;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.swing.JPanel;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.randomize.NguyenWidrowRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.helpers.SubstituteLoggerFactory;

import com.autoStock.Co;
import com.autoStock.chart.ChartDataFiller;
import com.autoStock.chart.ChartForAlgorithmTest.TimeSeriesType;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.indicator.CommonAnalysisData;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global;
import com.autoStock.signal.extras.EncogNetworkProvider;
import com.autoStock.tools.ArrayTools;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.ListTools;
import com.autoStock.tools.MathTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.tools.ResultsTools;
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
	private static final int INPUT_POINTS = 10;
	private static final int OUTPUT_POINTS = 1;
	
	private Exchange exchange = new Exchange("NYSE");
	private Symbol symbol = new Symbol("MS", SecurityType.type_stock);
	private Date dateStart = DateTools.getDateFromString("01/03/2014");
	private Date dateEnd = DateTools.getDateFromString("01/03/2014");
	private double crossValidationRatio = 0.30; 
	private ArrayList<PredictionResult> listOfPredictionResult = new ArrayList<PredictionResult>();
	private PredictionResult result = new PredictionResult(0, 0);
	
	private BasicMLDataSet mlDataSetReg;
	private BasicMLDataSet mlDataSetCross;
	private DefaultHighLowDataset dataSetForDefaultHighLowDataset;
	private TimeSeriesCollection TSC = new TimeSeriesCollection();
	
	public BacktestPredictFuture(){
		Global.callbackLock.requestLock();
	}
	
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
		HistoricalData historicalData = new HistoricalData(new Exchange("NYSE"), new Symbol("MS", SecurityType.type_stock), dateStart, dateEnd, Resolution.min);
		historicalData.setStartAndEndDatesToExchange();
		ArrayList<DbStockHistoricalPrice> listOfResultsIS = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.name), new QueryArg(QueryArgs.exchange, new Exchange("NYSE").name), new QueryArg(QueryArgs.resolution, Resolution.min.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate)));
		
		BasicMLDataSet dataSet = new BasicMLDataSet();
		
		// Build the input & ideal lists as % deltas
		ArrayList<Pair<DbStockHistoricalPrice, MLData>> priceIdealList = new ArrayList<>();
		
		CommonAnalysisData commonAnalysisData = new CommonAnalysisData();
		commonAnalysisData.setAnalysisData(QuoteSliceTools.getListOfQuoteSliceFromDbStockHistoricalPrice(listOfResultsIS));
		
		double pero[] = new double[commonAnalysisData.length()];
		double perh[] = new double[commonAnalysisData.length()];
		double perl[] = new double[commonAnalysisData.length()];
		double perc[] = new double[commonAnalysisData.length()];
		double peroc[] = new double[commonAnalysisData.length()];
		double perlh[] = new double[commonAnalysisData.length()];
		double ideal[] = new double[commonAnalysisData.length()];
		
		int c = 0;
		
		int IDEAL_OFFSET = 1;
		int INPUT_WINDOW_SIZE = 1;
		
		int first = IDEAL_OFFSET + INPUT_WINDOW_SIZE;
		
		for (int i=IDEAL_OFFSET + INPUT_WINDOW_SIZE; i<commonAnalysisData.length() - IDEAL_OFFSET; i++){
//			pero[i] = (commonAnalysisData.arrayOfPriceOpen[i-1] / commonAnalysisData.arrayOfPriceOpen[i]) -1;
//			perh[i] = (commonAnalysisData.arrayOfPriceHigh[i-1] / commonAnalysisData.arrayOfPriceHigh[i]) -1;
//			perl[i] = (commonAnalysisData.arrayOfPriceLow[i-1] / commonAnalysisData.arrayOfPriceLow[i]) -1;
			perc[i] = (commonAnalysisData.arrayOfPriceClose[i-1] / commonAnalysisData.arrayOfPriceClose[i]) -1;
//			peroc[i] = (commonAnalysisData.arrayOfPriceOpen[i] / commonAnalysisData.arrayOfPriceClose[i]) -1;
//			perlh[i] = (commonAnalysisData.arrayOfPriceLow[i] / commonAnalysisData.arrayOfPriceHigh[i]) -1;
			
			ideal[i] = (commonAnalysisData.arrayOfPriceLow[i] / commonAnalysisData.arrayOfPriceHigh[i+IDEAL_OFFSET]) -1;
			
			Co.println("--> Last price, current price, difference, next price: ");
			Co.println("--> " + commonAnalysisData.arrayOfPriceClose[i-1]);
			Co.println("--> " + commonAnalysisData.arrayOfPriceClose[i]);
			Co.println("--> " + commonAnalysisData.arrayOfPriceClose[i-1]);
			
			
			BasicMLDataPair pair = new BasicMLDataPair(new BasicMLData(
					new double[]{
							perc[i],
							}), new BasicMLData(new double[]{ideal[i]}));
			dataSet.add(pair);
		}
		
		int crossCount = 0;
		int regularEnd = 0; 
		
		if (crossValidationRatio == 0){
			mlDataSetReg = dataSet;
		} else{
			crossCount = (int) (crossValidationRatio * (double)dataSet.size());
			regularEnd = dataSet.size() - crossCount;
			
			Co.println("--> DSSize: " + dataSet.size());
			Co.println("--> Regular end: " + regularEnd);
			Co.println("--> Cross start: " + (regularEnd + 1));
			
			mlDataSetReg = new BasicMLDataSet(BasicMLDataSet.toList(dataSet).subList(0, regularEnd));
			mlDataSetCross = new BasicMLDataSet(BasicMLDataSet.toList(dataSet).subList(regularEnd, dataSet.size()));
			
			Co.println("--> Check sizes: " + mlDataSetReg.size() + ", " + mlDataSetCross.size());
		}
		
		BasicNetwork network = getMLNetwork(dataSet.getInputSize(), 1);
		network.reset();
		
		MLTrain train = new ResilientPropagation(network, mlDataSetReg);
		
		for (int i=0; i<50; i++){
			train.iteration();
			Co.println(i + ". " + train.getError());
		}
		
		train.finishTraining();
		
		new EncogNetworkProvider().saveNetwork((BasicNetwork) train.getMethod(), historicalData.exchange.name + "-" + historicalData.symbol.name + "-predict");
		
		// Compute Regular
		for (int i=0; i<mlDataSetReg.getRecordCount(); i++){
			double idealValue = mlDataSetReg.getData().get(i).getIdeal().getData(0);
			double computedValue = network.compute(mlDataSetReg.getData().get(i).getInput()).getData(0);
			
			//Co.println("--> Inputs, ideal, computed: " + StringTools.arrayOfDoubleToString(dataSet.getData().get(i).getInputArray()) + "... " + idealValue + " = " + computedValue);
			
			if (idealValue > 0 && computedValue > 0){result.countCorrect++;}
			else if (idealValue < 0 && computedValue < 0){result.countCorrect++;}
			//else if (idealValue == 0){}
			else {result.countIncorrect++;}
		}
		
		Co.println("--> Directional accuracy (reg): " + result.countIncorrect + " / " + result.countCorrect + " = " + new DecimalFormat("#.##").format(((double)result.countCorrect / ((double)result.countCorrect + (double)result.countIncorrect) * 100)) + "%");
		
		
		// Compute Cross
		result.countCorrect = 0;
		result.countIncorrect = 0;
		
		for (int i=0; i<mlDataSetCross.getRecordCount(); i++){
			double idealValue = mlDataSetCross.getData().get(i).getIdeal().getData(0);
			double computedValue = network.compute(mlDataSetReg.getData().get(i).getInput()).getData(0);
			
			Co.println("--> Inputs, ideal, computed: " + StringTools.arrayOfDoubleToString(dataSet.getData().get(i).getInputArray()) + "... " + idealValue + " = " + computedValue);
			
			if (idealValue > 0 && computedValue > 0){result.countCorrect++;}
			else if (idealValue < 0 && computedValue < 0){result.countCorrect++;}
			//else if (idealValue == 0){}
			else {result.countIncorrect++;}
		}
		
		Co.println("--> Directional accuracy (cross): " + result.countIncorrect + " / " + result.countCorrect + " = " + new DecimalFormat("#.##").format(((double)result.countCorrect / ((double)result.countCorrect + (double)result.countIncorrect) * 100)) + "%");
		
		dataSetForDefaultHighLowDataset = new DefaultHighLowDataset("Price series", commonAnalysisData.arrayOfDates, commonAnalysisData.arrayOfPriceHigh, commonAnalysisData.arrayOfPriceLow, commonAnalysisData.arrayOfPriceOpen, commonAnalysisData.arrayOfPriceClose, ArrayTools.convertToDouble(commonAnalysisData.arrayOfSizeVolume));
		
		TSC.addSeries(new ChartDataFiller().getTimeSeries("Price ($)", ResultsTools.getBasicPair(commonAnalysisData.arrayOfDates, commonAnalysisData.arrayOfPriceClose)));
	
		TimeSeries tsReg = new TimeSeries("Regular");
		
		int offset = IDEAL_OFFSET + INPUT_WINDOW_SIZE;
		
		for (int i=0; i<mlDataSetReg.getRecordCount() - offset; i++){
			Co.println("--> Computed: " + network.compute(mlDataSetReg.get(i + offset).getInput()).getData(0));
			tsReg.add(new TimeSeriesDataItem(new Minute(commonAnalysisData.arrayOfDates[i + offset]), 1 + (network.compute(mlDataSetReg.get(i + offset).getInput()).getData(0) * commonAnalysisData.arrayOfPriceClose[i + offset - 1]))); 
		}
		
		TSC.addSeries(tsReg);
		
		TimeSeries tsCross = new TimeSeries("Cross");
		
		for (int i=0; i<mlDataSetCross.getRecordCount() - offset; i++){
			tsCross.add(new TimeSeriesDataItem(new Minute(commonAnalysisData.arrayOfDates[i + offset + regularEnd]), 1 + (network.compute(mlDataSetCross.get(i + offset).getInput()).getData(0) * commonAnalysisData.arrayOfPriceClose[i + offset + regularEnd - 1])));
		}
		
		TSC.addSeries(tsCross);
		
		Co.println("--> First: " + first + ", " + commonAnalysisData.arrayOfDates[first]);
		
		new PredictDisplay("autoStock - Forward Price Prediction");
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
	
	
	public class PredictDisplay extends ApplicationFrame {
		public PredictDisplay(String title) {
			super(title);
			
			final ChartPanel chartPanel = (ChartPanel) createPanel();
			chartPanel.setPreferredSize(new Dimension(1600, 1000));
			chartPanel.setHorizontalAxisTrace(true);
			setContentPane(chartPanel);
			setVisible(true);
			toFront();
			pack();
			RefineryUtilities.positionFrameOnScreen(this, 0, 0);
		}
		
		public JPanel createPanel() {
			JFreeChart chart = createChart();
			chart.setAntiAlias(true);
			chart.setTextAntiAlias(true);
			ChartPanel panel = new ChartPanel(chart, false);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
			return panel;
		}
		
		private JFreeChart createChart() {
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Domain"));
			plot.setGap(10);
			plot.setOrientation(PlotOrientation.VERTICAL);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			
			try {
				XYPlot subPlotForCandleStick = ChartFactory.createCandlestickChart("Candlestick Demo", "Time", "Candle Stick", dataSetForDefaultHighLowDataset, false).getXYPlot();
				((NumberAxis) subPlotForCandleStick.getRangeAxis()).setAutoRangeIncludesZero(false);
				subPlotForCandleStick.setBackgroundPaint(Color.white);
				subPlotForCandleStick.setDomainGridlinePaint(Color.lightGray);
				subPlotForCandleStick.setRangeGridlinePaint(Color.lightGray);
				subPlotForCandleStick.setRangeAxis(0, new NumberAxis("Price"));
				subPlotForCandleStick.getRangeAxis(0).setAutoRange(true);
				((NumberAxis) subPlotForCandleStick.getRangeAxis(0)).setAutoRangeIncludesZero(false);
				subPlotForCandleStick.setRangeAxis(1, new NumberAxis("Volume"));

				((CandlestickRenderer) subPlotForCandleStick.getRenderer()).setUseOutlinePaint(true);
				plot.add(subPlotForCandleStick, 1);
				
				XYPlot subPlotForPrice = new XYPlot(TSC, null, new NumberAxis("Action"), new StandardXYItemRenderer());
				subPlotForPrice.getRenderer().setSeriesPaint(0, Color.DARK_GRAY);
				subPlotForPrice.getRangeAxis().setAutoRange(true);
				((NumberAxis) subPlotForPrice.getRangeAxis()).setAutoRangeIncludesZero(false);
				subPlotForPrice.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
				
				subPlotForPrice.getRenderer().setSeriesPaint(1, Color.RED);
				
				plot.add(subPlotForPrice);
			} catch (Exception e) {}

			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("MM/dd HH:mm"));

			JFreeChart chart = new JFreeChart("autoStock - Fordward Price Prediction", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(Color.white);
			return chart;
		}
	}
}
