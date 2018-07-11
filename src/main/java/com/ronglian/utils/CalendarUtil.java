package com.ronglian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarUtil {
	
	/**
	 * @Description: 传入日期，得到下年元旦
	 */
	public static Date getNextNewYearsDay(Date now) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int nextYear= cal.get(Calendar.YEAR)+1;
		String newYearDay=nextYear+"-01-01";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date nextNewYearDay=null;
		try {
			nextNewYearDay = sdf.parse(newYearDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return nextNewYearDay;
	}
	
	/**
	 * @Description: 传入年，月数字得到当月周日
	 */
	public static List<Date> getMondayInMonth(int year, int month) {
        List<Date> list = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar(year, month-1, 1);
        int i = 1;
        while (calendar.get(Calendar.MONTH) < month) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            if (calendar.get(Calendar.MONTH) == month-1) {
            	Date date=calendar.getTime();
            	list.add(date);
                //System.out.printf("星期一：%tF%n", calendar);
            }
        }
        return list;
	}
	/**
	 * @Description: 传入年，月数字得到当月周日
	 */
	public static List<Date> getSundayInMonth(int year, int month) {
        List<Date> list = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar(year, month-1, 1);
        int i = 1;
        while (calendar.get(Calendar.MONTH) < month) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.MONTH) == month-1) {
            	Date date=calendar.getTime();
            	list.add(date);
                //System.out.printf("星期天：%tF%n", calendar);
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.MONTH) == month-1) {
                //System.out.printf("星期六：%tF%n", calendar);
            }
        }
        return list;
	}
	
	/**
	* 获取给定月的第一天和最后一天
	*/
	public static List<Date> getFirstAndLastdayOfMonth(int year,int month) {
		List<Date> monthday=new ArrayList<Date>();
			try {
				Calendar ca = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
				Date first;
				first = format.parse(year+"-"+month+"-01");
				ca.setTime(first);// someDate 为你要获取的那个月的时间
				ca.set(Calendar.DAY_OF_MONTH, 1);
				ca.add(Calendar.MONTH, 1);
				ca.add(Calendar.DAY_OF_MONTH, -1);
				// 最后一天
				Date lastDate = ca.getTime();
				monthday.add(first);
				monthday.add(lastDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//System.out.println(monthday.toString());
			return monthday;
		}
	
	/**
     * 获取指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     */
    public static Date plusDay(int num,Date currdate){
        //System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        //System.out.println("增加天数以后的日期：" + currdate);
        return currdate;
    }
    
    //根据日期得到本周一
    public static Date getThisWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        // 获得当前日期是一个星期的第几天  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if (1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        return cal.getTime();  
    }  
}
