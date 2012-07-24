package com.autoStock.exchange;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.autoStock.Co;
import com.autoStock.tools.DateTools;
import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ExchangeHelper {
	public Date getLocalTimeFromForeignTime(Time time, String timeZone) {

		Calendar calendarForLocal = new GregorianCalendar();

		GregorianCalendar calendarForForeign = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		calendarForForeign.set(Calendar.HOUR_OF_DAY, time.hour);
		calendarForForeign.set(Calendar.MINUTE, time.minute);
		calendarForForeign.set(Calendar.SECOND, time.second);

		calendarForLocal = new GregorianCalendar();
		calendarForLocal.setTimeInMillis(calendarForForeign.getTimeInMillis());

		return new Date(calendarForLocal.getTimeInMillis());
	}
}
