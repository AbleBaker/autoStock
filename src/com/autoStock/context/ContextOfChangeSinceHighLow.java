/**
 * 
 */
package com.autoStock.context;

import java.util.ArrayList;

import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogSubframe;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogFrameSupport.EncogFrameSource;
import com.autoStock.tools.MathTools;
import com.autoStock.trading.types.Position;
import com.autoStock.types.QuoteSlice;

/**
 * @author Kevin
 *
 */
public class ContextOfChangeSinceHighLow extends ContextBase implements EncogFrameSource{
	private QuoteSlice currentQuoteSlice;
	private ArrayList<QuoteSlice> listOfQuoteSlice;
	
	@Override
	public EncogFrame asEncogFrame() {
		double percentFromMin = 0;
		double percentFromMax = 0;
		double minPrice = Double.MAX_VALUE;
		double maxPrice = Double.MIN_VALUE;
		
		for (QuoteSlice quote : listOfQuoteSlice){
			maxPrice = Math.max(maxPrice, quote.priceHigh);
			minPrice = Math.min(minPrice, quote.priceLow);
		}
		
		percentFromMin = (currentQuoteSlice.priceLow / minPrice) -1;
		percentFromMax = (currentQuoteSlice.priceHigh / maxPrice) -1;
		
		EncogFrame encogFrame = new EncogFrame(this.getClass().getSimpleName(), FrameType.percent_change);
		EncogSubframe subframeForPositionValue = new EncogSubframe(this.getClass().getSimpleName(), new double[]{percentFromMin, percentFromMax}, FrameType.percent_change, 1, -1);
		encogFrame.addSubframe(subframeForPositionValue);
		return encogFrame;
	}

	@Override
	public void run() {}

	public void setCurrentQuoteSlice(QuoteSlice currentQuoteSlice, ArrayList<QuoteSlice> listOfQuoteSlice) {
		this.currentQuoteSlice = currentQuoteSlice;
		this.listOfQuoteSlice = listOfQuoteSlice;
	}
}
