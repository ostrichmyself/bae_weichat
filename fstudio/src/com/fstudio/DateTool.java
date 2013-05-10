package com.fstudio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具
 * @author tiant5
 *
 */
public class DateTool {
	
	
	/**
	 * 将日期文字转化为具体的日期
	 * 
	 * @return
	 */
	public static Date string2Date(String dateString, String formatString) {
		SimpleDateFormat formate = new SimpleDateFormat(formatString);
		Date date;
		try {
			date = formate.parse(dateString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * 将日期转化为字符串
	 * @return
	 */
	public static String Date2String(Date date, String formatString) {
		SimpleDateFormat formate = new SimpleDateFormat(formatString);
		return formate.format(date);
	}
	
	
	/**
	 * 返回为1表示星期一
	 * 返回为7表示星期天
	 * 其它一次类推
	 * @param date
	 * @return
	 */
	public static int indexOfCurrentWeek(Date date)
	{
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(date);           
        int w=cal.get(Calendar.DAY_OF_WEEK)-1; 
        if(w==0)
        {
        	w = 7;
        }
        return w;
	}
	
	public static String getTimeString(Date date, String style) {
		SimpleDateFormat myFmt2 = new SimpleDateFormat(style);
		return myFmt2.format(date);
	}
	

}
