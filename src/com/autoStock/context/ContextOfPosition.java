/**
 * 
 */
package com.autoStock.context;

import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogSubframe;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogFrameSupport.EncogFrameSource;
import com.autoStock.trading.types.Position;

/**
 * @author Kevin
 *
 */
public class ContextOfPosition extends ContextBase implements EncogFrameSource{
	private Position position;

	@Override
	public EncogFrame asEncogFrame() {
		EncogFrame encogFrame = new EncogFrame(this.getClass().getSimpleName(), FrameType.raw);
		EncogSubframe subframeForPositionValue = new EncogSubframe(this.getClass().getSimpleName(), new double[]{position == null ? 0 : position.getCurrentPercentGainLoss(true)}, FrameType.raw, 1, -1);
		EncogSubframe subFrameForHavePosition = new EncogSubframe(this.getClass().getSimpleName(), new double[]{position == null ? -1 : 1}, FrameType.raw, 1, -1);
		encogFrame.addSubframe(subframeForPositionValue);
		encogFrame.addSubframe(subFrameForHavePosition);
		return encogFrame;
	}

	@Override
	public void run() {}

	public void setPosition(Position position) {
		this.position = position;
	}
}
