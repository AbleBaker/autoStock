package com.autoStock.guage;

import com.autoStock.signal.SignalDefinitions.SignalBounds;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.types.basic.MutableEnum;

/**
 * @author Kevin Kowalewski
 *
 */
public class SignalGuage {
	public final MutableEnum<SignalGuageType> mutableEnumForSignalGuageType;
	public final SignalBounds signalBounds;
	public int threshold;
	
	public SignalGuage(MutableEnum<SignalGuageType> mutableEnumForSignalGuageType, SignalBounds signalBounds, int threshold) {
		this.mutableEnumForSignalGuageType = mutableEnumForSignalGuageType;
		this.signalBounds = signalBounds;
		this.threshold = threshold;
	}
}
