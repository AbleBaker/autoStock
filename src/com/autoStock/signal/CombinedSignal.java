package com.autoStock.signal;

/**
 * @author Kevin Kowalewski
 * 
 */
public class CombinedSignal {
	public int longEntry = 0;
	public int longExit = 0;
	public int shortEntry = 0;
	public int shortExit = 0;
	public double strength = 0;

	public CombinedSignal() {

	}

	public CombinedSignal(int longEntry, int longExit, int shortEntry, int shortExit, double strength) {
		this.longEntry = longEntry;
		this.longExit = longExit;
		this.shortEntry = shortEntry;
		this.shortExit = shortExit;
		this.strength = strength;
	}
}