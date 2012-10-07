package com.autoStock.position;

import com.autoStock.trading.types.Position;

/**
 * @author Kevin Kowalewski
 *
 */
public interface PositionStatusListener {
	public void positionStatusChange(Position position);
}
