/**
 * 
 */
package com.autoStock.internal;

/**
 * @author Kevin Kowalewski
 *
 */
public class Global {
	public static Mode mode;
	
	public static enum Mode {
		server,
		client
	}
	
	public static enum Brokerage {
		ib,
	}
}
