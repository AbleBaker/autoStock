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
		dateFormat.applyPattern("EEE, MMM d, yyyy hh:mm:ss a");
		return dateFormat.format(new Date(date));
	}	
	
	public static Date getDateFromString(String date){
		try {
			return new SimpleDateFormat("yyyy/MM/dd.HH:mm.a").parse(date);
		}catch (ParseException e){
			try {
				return new SimpleDateFormat("yyyy/MM/dd.HH:mm:ss.a").parse(date);
			}catch (ParseException ex){
				Co.println("Could not parse: " + date);
				ApplicationStates.shutdown();
				return null;
			}
		}
	}
}
