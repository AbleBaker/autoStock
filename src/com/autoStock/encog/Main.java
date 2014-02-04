package com.autoStock.encog;

import com.autoStock.internal.ApplicationStates;
import com.autoStock.internal.Global.Mode;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationStates.startup(Mode.client_skip_tws);
		
		new SolveFeedTest().execute();
	}
}
