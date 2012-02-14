/**
 * 
 */
package com.autoStock.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.internal.ApplicationStates;

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
		}catch (ParseException e){
			try {
				return new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss.a").parse(date);
			}catch (ParseException ex){
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
				}catch (ParseException exc){
					Co.println("Could not parse: " + date);
					ApplicationStates.shutdown();
					return null;
				}
			}
		}
	}
}
