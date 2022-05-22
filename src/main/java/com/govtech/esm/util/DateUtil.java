package com.govtech.esm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DateUtil {

	private DateUtil() {

	}
	/**
	 * Get from String
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date parse(String strDate, String format) {

		try {
			if (strDate.length() != format.length()) {
				return null;
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setLenient(false);
			return simpleDateFormat.parse(strDate);
		} catch (Exception e) {
			log.error("Parse Date error", e);
			return null;
		}
	}

	/**
	 * Method to convert a Date object to a date in specified String format <br>
	 * 
	 * @param date   Date
	 * @param format String
	 * @return String
	 * @pre None
	 * @post None
	 */
	public static String format(Date date, String format) {

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			log.error("Format Date error", e);
			return null;
		}
	}

}