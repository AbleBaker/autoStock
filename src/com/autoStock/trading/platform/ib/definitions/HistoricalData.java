/**
 * 
 */
package com.autoStock.trading.platform.ib.definitions;

/**
 * @author Kevin Kowalewski
 *
 */
public class HistoricalData {
	public static enum Period{
		year(new Resolution[]{Resolution.day}),
		month_6(new Resolution[]{Resolution.day}),
		month_3(new Resolution[]{Resolution.day}),
		month(new Resolution[]{Resolution.day, Resolution.hour}),
		week(new Resolution[]{Resolution.day, Resolution.hour, Resolution.min_30, Resolution.min_15}),
		day(new Resolution[]{Resolution.hour, Resolution.min_30, Resolution.min_15, Resolution.min}),
		hour_1(new Resolution[]{Resolution.min_30, Resolution.min_15, Resolution.min, Resolution.sec_30,  Resolution.sec_15, Resolution.sec_5}),
		min_30(new Resolution[]{Resolution.min_15, Resolution.min, Resolution.sec_30, Resolution.sec_15, Resolution.sec_5}),
		min(new Resolution[]{Resolution.sec_30, Resolution.sec_15, Resolution.sec_5, Resolution.sec}),
		;
		
		Resolution[] arrayOfResolution;
		
		Period(Resolution[] arrayOfResolution){
			this.arrayOfResolution = arrayOfResolution;
		}
	}
	
	public static enum Resolution {
		day(86400),
		hour(3600),
		min_30(1800),
		min_15(900),
		min_5(300),
		min(60),
		sec_30(30),
		sec_15(15),
		sec_5(5),
		sec(1),
		;
		
		public int seconds;
		
		Resolution(int seconds){
			this.seconds = seconds;
		}
	}
}
