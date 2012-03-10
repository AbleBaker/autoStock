/**
 * 
 */
package com.autoStock.signal;

import java.util.ArrayList;

import com.autoStock.signal.SignalDefinitions.SignalSource;
import com.autoStock.signal.SignalDefinitions.SignalType;

/**
 * @author Kevin Kowalewski
 *
 */
public class Signal {
	public SignalType signalType;
	public SignalSource signalSource;
	public ArrayList<SignalMetric> listOfSignalMetric = new ArrayList<SignalMetric>();
	
	public Signal(SignalType signalType, SignalSource signalSource) {
		this.signalType = signalType;
		this.signalSource = signalSource;
	}
}
