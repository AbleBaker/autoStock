package com.autoStock.types.basic;

/**
 * @author Kevin Kowalewski
 *
 */
public class Time {
	public int hours;
	public int minutes;
	public int seconds;
	
	public boolean isFuture(){
		if (hours >= 0 && minutes >= 0 && seconds >= 0){
			return true;
		}
		return false;
	}
	
	public boolean isPast(){
		if (hours <= 0 && minutes <= 0 && seconds <= 0){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "Time: " + hours + " : " + minutes + " : " + seconds;
	}
}
