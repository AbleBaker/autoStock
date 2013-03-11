package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGuage {
	public final SignalGuageType signalGuageType;
	public final SignalBounds signalBounds;
	public int threshold;
	
	public SignalGuage(SignalGuageType signalGuageType, SignalBounds signalBounds, int threshold) {
		this.signalGuageType = signalGuageType;
		this.signalBounds = signalBounds;
		this.threshold = threshold;
	}
}
