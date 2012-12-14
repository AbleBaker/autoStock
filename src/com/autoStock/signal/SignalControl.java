package com.autoStock.signal;

import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 * 
 */
public class SignalControl {
	public static ImmutableInteger periodLengthStart = new ImmutableInteger(16);
	public static ImmutableInteger periodLengthMiddle = new ImmutableInteger(32);
	public static ImmutableInteger periodLengthEnd = new ImmutableInteger(10);

	public static int periodAverageForPPC = 3;

	public static int maxStopLossValue;
	public static final int periodAverageForRSI = 0;
	public static final int periodAverageForSTORSI = 0;
}
