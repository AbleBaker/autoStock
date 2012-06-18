package com.autoStock.tools;

import java.util.Date;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class Benchmark {
	private long startMark = new Date().getTime();
	private long lastMark = new Date().getTime();
	
	public void tick(){
		long currentTimeMills = new Date().getTime();
		Co.log("Tick: " + (currentTimeMills - lastMark) + "ms");
		lastMark = currentTimeMills;
	}
	
	public void printTick(String action){
		long currentTimeMills = new Date().getTime();
		Co.log("Tick: [" + action + "] " + (currentTimeMills - lastMark) + "ms");
		lastMark = currentTimeMills;
	}
	
	public void printTotal(){
		long currentTimeMills = new Date().getTime();
		Co.log("Benchmark: " + (currentTimeMills - startMark));
	}
}
