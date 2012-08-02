package com.autoStock.exchange;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.autoStock.types.basic.Time;

/**
 * @author Kevin Kowalewski
 * 
 */
public class ExchangeHelper {
	public Date getLocalTimeFromForeignTime(Time time, String timeZone) {

		Calendar calendarForLocal = new GregorianCalendar();

		GregorianCalendar calendarForForeign = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		calendarForForeign.set(Calendar.HOUR_OF_DAY, time.hours);
		calendarForForeign.set(Calendar.MINUTE, time.minutes);
		calendarForForeign.set(Calendar.SECOND, time.seconds);

		calendarForLocal = new GregorianCalendar();
		calendarForLocal.setTimeInMillis(calendarForForeign.getTimeInMillis());

		return new Date(calendarForLocal.getTimeInMillis());
	}

	public Time getTimeUntil(Date date) {
		Time time = new Time();
		Date localDate = new Date();

		long millisDiff = date.getTime() - localDate.getTime();

		time.hours = (int) (millisDiff / (1000*60*60));
		time.minutes = (int) ((millisDiff % (1000*60*60)) / (1000*60));
		time.seconds = (int) (((millisDiff % (1000*60*60)) % (1000*60)) / 1000);

		return time;
	}
	
	public static enum ExchangeDesignation {
		NYSE,
		ASX,
	}
}
