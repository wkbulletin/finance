package com.huacai.util;

import static java.time.Duration.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CDCUtil {

	private CDCUtil() {
	}

	static int CDCSKIP = 11000;

	final private static Instant //
	CDC1970 = new Date(0).toInstant();

	public static String toDate(int cdc) {
		return TimeUtil.getDateEx(//
		/*   */Date.from(CDC1970.plus(//
						cdc, ChronoUnit.DAYS)));
	}

	public static int getCDC() {
		return getCDC(TimeUtil.getDate());
	}

	public static int getCDC(Date date) {
		return getCDC(date.toInstant());
	}

	public static int getCDC(String date) {
		return getCDC(TimeUtil.getDate(date));
	}

	public static int getCDC2(Date date) {
		return getCDC(date) - CDCUtil.CDCSKIP;
	}

	public static int getCDC2(String date) {
		return getCDC(date) - CDCUtil.CDCSKIP;
	}

	/**
	 * JAVA8-???
	 */
	public static int getCDC(//
					Instant date) {
		return offCDC(date, 0);
	}

	public static int offCDC(//
					long minutes) {
		final Date date = new Date();
		return offCDC(date, minutes);
	}

	public static int offCDC(//
					Date date,//
					long minutes) {
		Instant dt = date.toInstant();
		return offCDC(dt, minutes);
	}

	public static int offCDC(//
					Instant date,//
					long minutes) {
		Duration durate = null;// ???
		durate = between(CDC1970, date);
		durate.plusMinutes(-minutes);// ????
		return (int) (durate.toDays() + 0);
	}

	public static int getCDC2() {
		return getCDC() - CDCSKIP;
	}

	public static int getCDC2(//
					Instant date) {
		return getCDC(date) - CDCSKIP;
	}

	public static int getZHOU() {
		return (getCDC() + 4) / 7;
	}

	public static int getZHOU(Date date) {
		return (getCDC(date) + 4) / 7;
	}

	public static int getZHOU(String date) {
		return (getCDC(date) + 4) / 7;
	}

	public static int getZHOU(Instant date) {
		return (getCDC(date) + 4) / 7;
	}

	public static int getWeek() {
		return (getCDC() + 4) % 7 + 1;
	}

	public static int getWeek(Date date) {
		return (getCDC(date) + 4) % 7 + 1;
	}

	public static int getWeek(String date) {
		return (getCDC(date) + 4) % 7 + 1;
	}

	public static int getWeek(Instant date) {
		return (getCDC(date) + 4) % 7 + 1;
	}

	public static void main(String[] args) {
		Date from = TimeUtil.getDate("1970-01-05");
		System.out.println(getZHOU(from));
		
	}
}
