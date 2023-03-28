package com.example.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
	private static Calendar calendar = Calendar.getInstance();
	public static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");

	public static Date dateNow() {
		return calendar.getTime();
	}

	public static Date addMINUTE(int minute) {
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static boolean checkTime15P(Date min, Date max) {
		System.out.println("min:" + min);
		System.out.println("max:" + max);
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			cal.setTime(min);
			System.out.println("min - : " + cal.get(cal.MINUTE) + "- max - : " + max.getMinutes());
			if (max.getMinutes() - cal.get(cal.MINUTE) >= 15)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static Date toDate(String date) {
        try {
            return DATE_FORMATER.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

}
