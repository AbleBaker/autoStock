package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.types.basic.MutableEnum;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGuage {
	public final MutableEnum<SignalGuageType> immutableEnumForSignalGuageType;
	public final SignalBounds signalBounds;
	public int threshold;
	
	public SignalGuage(MutableEnum<SignalGuageType> immutableEnumForSignalGuageType, SignalBounds signalBounds, int threshold) {
		this.immutableEnumForSignalGuageType = immutableEnumForSignalGuageType;
		this.signalBounds = signalBounds;
		this.threshold = threshold;
	}
}
