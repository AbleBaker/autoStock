/**
 * 
 */
package com.autoStock.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kevin Kowalewski
 *
 */
public class DateTools {
	public static String getPrettyDate(long date){
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("EEE, MMM d, yyyy hh:mm aaa");
		return dateFormat.format(new Date(date));
	}
}
