/**
 * 
 */
package com.autoStock.backtest.watchmaker;

/**
 * @author Kevin
 *
 */

public class WMEvolutionParams {
	public enum WMEvolutionType {
		type_island,
		type_generational,
		none,
	}
	
	public enum WMEvolutionThorough {
		thorough_quick,
		thorough_slow,
	}
}
