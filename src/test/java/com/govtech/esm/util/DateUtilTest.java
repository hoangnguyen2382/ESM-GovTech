package com.govtech.esm.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateUtilTest {

	@Test
	public void testGetDateFromString() throws ParseException {

		String strDate = "2001-10-16";
		String format = Constants.DATE_FORMAT_1;

		Date dateFromString = DateUtil.parse(strDate, format);

		Date expectedDate = new SimpleDateFormat(Constants.DATE_FORMAT_2).parse("16-Oct-01");
		assertNotNull(dateFromString);
		assertEquals(expectedDate, dateFromString);
	}
	
	@Test
	public void testGetDateFromStringNullvalue() throws ParseException {

		String strDate = "";
		String format = Constants.DATE_FORMAT_1;

		Date dateFromString = DateUtil.parse(strDate, format);

		assertNull(dateFromString);
	}


	@Test
	public void testGetStringfromDate() throws ParseException {
		Date date = new SimpleDateFormat(Constants.DATE_FORMAT_1).parse("2001-10-16");

		String stringfromDate = DateUtil.format(date, Constants.DATE_FORMAT_2);

		assertNotNull("16-Oct-01", stringfromDate);
	}

	@Test
	public void testGetStringfromDateNullvVlue() throws ParseException {
		Date date =null;

		String stringfromDate = DateUtil.format(date, Constants.DATE_FORMAT_2);

		assertNull(stringfromDate);
	}
}