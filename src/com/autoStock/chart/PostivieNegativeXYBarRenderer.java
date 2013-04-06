package com.autoStock.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.RectangularShape;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

/**
 * @author Kevin Kowalewski
 * 
 */
public class PostivieNegativeXYBarRenderer extends XYBarRenderer {
	private int rendererIndex;
	
	public PostivieNegativeXYBarRenderer(int rendererIndex){
		super();
		this.rendererIndex = rendererIndex;
	}
	
	@Override
	public Paint getItemPaint(int x_row, int x_col) {
		XYDataset xyDataset = getPlot().getDataset(rendererIndex);
		
		double l_value = xyDataset.getYValue(x_row, x_col);
		
		if (l_value < 0.0) {
			return Color.decode("#FFEFEF");
		} else {
			return Color.decode("#DBFFE1");
		}
	}
}
