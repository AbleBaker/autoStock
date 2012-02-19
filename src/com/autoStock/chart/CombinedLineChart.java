/**
 * 
 */
package com.autoStock.chart;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CombinedLineChart {

	public class LineChartDisplay extends ApplicationFrame {

		public LineChartDisplay(TimeSeriesCollection timeSeriesCollection1, TimeSeriesCollection timeSeriesCollection2) {
			super("autoStock - Chart");

			ChartPanel chartPanel = (ChartPanel) createPanel(timeSeriesCollection1, timeSeriesCollection2);
			chartPanel.setPreferredSize(new java.awt.Dimension(500*2, 270*3));
			setContentPane(chartPanel);

			RefineryUtilities.centerFrameOnScreen(this);
			setVisible(true);
			pack();
		}

		public JPanel createPanel(XYDataset dataset1, XYDataset dataset2) {
			JFreeChart chart = createChart(dataset1, dataset2);
			ChartPanel panel = new ChartPanel(chart);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
			return panel;
		}

		private JFreeChart createChart(XYDataset dataset1, XYDataset dataset2) {
			
			NumberAxis numberAxis1 = new NumberAxis("Range 1");
			numberAxis1.setAutoRange(true);
			XYPlot subPlot1 = new XYPlot(dataset1, null, numberAxis1, new StandardXYItemRenderer());
			subPlot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
			
			NumberAxis numberAxis2 = new NumberAxis("Range 2");
			numberAxis2.setAutoRange(true);
			XYPlot subPlot2 = new XYPlot(dataset2, null, numberAxis2, new StandardXYItemRenderer());
			subPlot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
			
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Domain"));
			plot.setGap(10);
			plot.add(subPlot1, 1);
			plot.add(subPlot2, 1);
			plot.setOrientation(PlotOrientation.VERTICAL);

//			JFreeChart chart = ChartFactory.createTimeSeriesChart("autoStock - Analysis",
//					"Date", // x-axis label
//					"Price Per Share", // y-axis label
//					plot,
//					true,
//					true,
//					false 
//					);
			
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
			
			JFreeChart chart = new JFreeChart("autoStock - Analysis", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

			chart.setBackgroundPaint(Color.white);

			return chart;

		}
	}
}
