/**
 * 
 */
package com.autoStock.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 *
 */
public class DateTools {
	public static String getPrettyDate(long date){
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		//dateFormat.applyPattern("EEE, MMM d, yyyy hh:mm:ss a");
		dateFormat.applyPattern("yyyy/MM/dd hh:mm:ss a");
		return dateFormat.format(new Date(date));
	}
	
	public static String getPrettyDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy/MM/dd hh:mm:ss a");
		return dateFormat.format(date);
	}
	
	public static String getSqlDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		return dateFormat.format(date);
	}
	
	public static Date getDateFromString(String date){
		try {
			return new SimpleDateFormat("yyyy/MM/dd.HH:mm.a").parse(date);
		}catch (ParseException e1){
			try {
				return new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss.a").parse(date);
			}catch (ParseException e2){
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
				}catch (ParseException e3){
					try {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);	
					}catch (ParseException e4){
						try {
							return new SimpleDateFormat("MM/dd/yyyy").parse(date);
						}catch (ParseException e5){
							Co.println("Could not parse: " + date);
							return null;	
						}
					}
				}
			}
		}
	}
	
	public static Time getTimeFromString(String timeString){
		Time time = new Time();
		time.hour = Integer.valueOf(timeString.substring(0,2));
		time.minute = Integer.valueOf(timeString.substring(4,5));
		time.second = Integer.valueOf(timeString.substring(7,8));
		return time;
	}
}
