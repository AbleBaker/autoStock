package com.qutoStock.tools;

import java.util.Date;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class Benchmark {
	private long startMark = new Date().getTime();
	private int lastMark = 0;
	
	public void tick(){
		long currentTimeMills = new Date().getTime();
		Co.log("Tick: " + (currentTimeMills - lastMark));
	}
	
	public void total(){
		long currentTimeMills = new Date().getTime();
		Co.log("Benchmark: " + (currentTimeMills - startMark));
	}
}
