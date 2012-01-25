package com.autoStock.tools;

import java.util.Date;

import com.autoStock.Co;

/**
 * @author Kevin Kowalewski
 *
 */
public class Benchmark {
	private Date date = new Date();
	private long startMark = date.getTime();
	private int lastMark = 0;
	
	public void tick(){
		long currentTimeMills = date.getTime();
		Co.log("Tick: " + (currentTimeMills - lastMark));
	}
	
	public void total(){
		long currentTimeMills = date.getTime();
		Co.log("Benchmark: " + (currentTimeMills - startMark));
	}
}
