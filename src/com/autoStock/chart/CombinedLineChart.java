/**
 * 
 */
package com.autoStock.chart;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
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
	
	public int usedColor = -1;

	public class LineChartDisplay extends ApplicationFrame {

		public LineChartDisplay(TimeSeriesCollection... timeSeriesCollections) {
			super("autoStock - Chart");

			ChartPanel chartPanel = (ChartPanel) createPanel(timeSeriesCollections);
			chartPanel.setPreferredSize(new java.awt.Dimension(500*2, 250* timeSeriesCollections.length));
			setContentPane(chartPanel);

			setVisible(true);
			pack();
			
			RefineryUtilities.centerFrameOnScreen(this);
		}

		public JPanel createPanel(XYDataset... xydatasets) {
			JFreeChart chart = createChart(xydatasets);
			ChartPanel panel = new ChartPanel(chart);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
			return panel;
		}

		private JFreeChart createChart(XYDataset... xydatasets) {
			
			CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new DateAxis("Domain"));
			plot.setGap(10);
			
			int i = 1;
			for (XYDataset xydataset : xydatasets){
				usedColor = -1;
				XYPlot subPlot = new XYPlot(xydataset, null, new NumberAxis("Range " + i), new StandardXYItemRenderer());
				subPlot.getRenderer().setSeriesPaint(0, getColor());
				plot.add(subPlot, 1);
				i++;
			}
			
			plot.setOrientation(PlotOrientation.VERTICAL);
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
		
		public Color getColor(){
			Color[] arrayOfColors = new Color[]{Color.GRAY, Color.BLUE, Color.GREEN, Color.RED, Color.PINK, Color.BLACK, Color.CYAN};
			if (usedColor >= arrayOfColors.length-1){usedColor = -1;}
			usedColor++;
			return arrayOfColors[usedColor];
		}
	}
}
