package com.autoStock.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYBoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.XYDataset;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 * 
 */
public class PostivieNegativeXYBarRenderer extends XYAreaRenderer2 {
	private int rendererIndex;
	
	public PostivieNegativeXYBarRenderer(int rendererIndex){
		super();
		this.rendererIndex = rendererIndex;
//		setShadowVisible(false);
//		setBarPainter(new StandardXYBarPainter());
	}
	
	@Override
	public Paint getItemPaint(int x_row, int x_col) {
		XYDataset xyDataset = getPlot().getDataset(rendererIndex);
		double l_value = xyDataset.getYValue(x_row, x_col);

		if (l_value < 0) {
			return Color.decode("#FFC7C7");
		} else {
			return Color.decode("#A8FFB7");
		}
	}
}
