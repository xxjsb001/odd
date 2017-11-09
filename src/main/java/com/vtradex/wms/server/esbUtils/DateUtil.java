package com.vtradex.wms.server.esbUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}
	
	
	public static long timeDiff(Date date1, Date date2) {
		  return Math.abs((date2.getTime() - date1.getTime()));// 得到两者的毫秒数
	}
	


	public static void main(String[] args) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 指定一个日期
		Date date = dateFormat.parse("2013-6-1 23:59:59");
		Date date2 = dateFormat.parse("2013-6-2 23:59:59");
		//System.out.println(isSameDate(date, date2));
		System.out.println(timeDiff(date2, date));
	}

}
