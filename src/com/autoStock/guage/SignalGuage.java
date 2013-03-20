package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.types.basic.ImmutableEnum;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGuage {
	public final ImmutableEnum<SignalGuageType> immutableEnumForSignalGuageType;
	public final SignalBounds signalBounds;
	public int threshold;
	
	public SignalGuage(ImmutableEnum<SignalGuageType> immutableEnumForSignalGuageType, SignalBounds signalBounds, int threshold) {
		this.immutableEnumForSignalGuageType = immutableEnumForSignalGuageType;
		this.signalBounds = signalBounds;
		this.threshold = threshold;
	}
}
