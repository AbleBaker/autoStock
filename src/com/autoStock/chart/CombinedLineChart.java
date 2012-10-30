/**
 * 
 */
package com.autoStock.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import com.autoStock.chart.ChartForAlgorithmTest.TimeSeriesType;
import com.autoStock.chart.ChartForAlgorithmTest.TimeSeriesTypePair;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CombinedLineChart {
	public int usedColor = -1;

	public class LineChartDisplay extends ApplicationFrame {
		public String title;
		public TimeSeriesTypePair[] arrayOfTimeSeriesPair;
		
		public LineChartDisplay(String title, TimeSeriesTypePair... timeSeriesPairs) {
			super("autoStock - Chart - " + title);
			
			this.title = title;
			arrayOfTimeSeriesPair = timeSeriesPairs;

			ChartPanel chartPanel = (ChartPanel) createPanel();
			chartPanel.setPreferredSize(new Dimension(1600, 900));
			setContentPane(chartPanel);
			setVisible(true);
			toFront();
			pack();
			
			RefineryUtilities.centerFrameOnScreen(this);
		}

		public JPanel createPanel() {
			JFreeChart chart = createChart();
			ChartPanel panel = new ChartPanel(chart);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
			return panel;
		}

		private JFreeChart createChart() {
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Domain"));
			plot.setGap(10);
			resetColor();
			
			XYPlot subPlotForSignalTotal = new XYPlot(getPairForType(TimeSeriesType.type_signal_total).timeSeriesCollection, null, new NumberAxis(getPairForType(TimeSeriesType.type_signal_total).timeSeriesType.displayName), new StandardXYItemRenderer());
			subPlotForSignalTotal.getRenderer().setSeriesPaint(0, getColor());
			plot.add(subPlotForSignalTotal, 1);
			
			XYPlot subPlotForSignals = new XYPlot(getPairForType(TimeSeriesType.type_signals).timeSeriesCollection, null, new NumberAxis(getPairForType(TimeSeriesType.type_signals).timeSeriesType.displayName), new StandardXYItemRenderer());
			subPlotForSignals.getRenderer().setSeriesPaint(0, getColor());
			plot.add(subPlotForSignals, 1);
			
			XYPlot subPlotForPrice = new XYPlot(getPairForType(TimeSeriesType.type_price).timeSeriesCollection, null, new NumberAxis(getPairForType(TimeSeriesType.type_price).timeSeriesType.displayName), new StandardXYItemRenderer());
			subPlotForPrice.getRenderer().setSeriesPaint(0, Color.BLACK);
			subPlotForPrice.getRangeAxis().setAutoRange(true);
			((NumberAxis)subPlotForPrice.getRangeAxis()).setAutoRangeIncludesZero(false);
			plot.add(subPlotForPrice, 1);
			
			subPlotForPrice.setDataset(1, getPairForType(TimeSeriesType.type_value).timeSeriesCollection);
	        NumberAxis axis2 = new NumberAxis(TimeSeriesType.type_value.displayName);
	        subPlotForPrice.setRangeAxis(1, axis2);
	        subPlotForPrice.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
	        subPlotForPrice.setRenderer(1, new StandardXYItemRenderer());       
			subPlotForPrice.getRenderer(1).setSeriesPaint(0, Color.ORANGE);
	        subPlotForPrice.mapDatasetToRangeAxis(1, 1);
	        
			subPlotForPrice.setDataset(2, getPairForType(TimeSeriesType.type_entry).timeSeriesCollection);
	        subPlotForPrice.setRenderer(2, new XYShapeRenderer());
	        subPlotForPrice.getRenderer(2).setSeriesShape(0, ShapeUtilities.createUpTriangle(5));
	        subPlotForPrice.getRenderer(2).setSeriesPaint(0, Color.GREEN);
	        
			subPlotForPrice.setDataset(3, getPairForType(TimeSeriesType.type_exit).timeSeriesCollection);
	        subPlotForPrice.setRenderer(3, new XYShapeRenderer());
	        subPlotForPrice.getRenderer(3).setSeriesShape(0, ShapeUtilities.createDownTriangle(5));
	        subPlotForPrice.getRenderer(3).setSeriesPaint(0, Color.RED);
	        
	        subPlotForPrice.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
			
			plot.setOrientation(PlotOrientation.VERTICAL);
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
			
			JFreeChart chart = new JFreeChart("autoStock - Analysis - " + title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

			chart.setBackgroundPaint(Color.white);

			return chart;

		}
		
		public TimeSeriesTypePair getPairForType(TimeSeriesType timeSeriesType){
			for (TimeSeriesTypePair pair : arrayOfTimeSeriesPair){
				if (pair.timeSeriesType == timeSeriesType){
					return pair;
				}
			}
			
			return null;
		}
		
		public Color getColor(){
			Color[] arrayOfColors = new Color[]{Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.PINK, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.YELLOW};
			if (usedColor >= arrayOfColors.length-1){usedColor = -1;}
			usedColor++;
			return arrayOfColors[usedColor];
		}
		
		public void resetColor(){
			usedColor = -1;
		}
	}
}
