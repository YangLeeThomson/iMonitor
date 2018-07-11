/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.utils 
 * @author: YeohLee   
 * @date: 2018年5月26日 下午4:50:18 
 */
package com.ronglian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;




 /** 
 * @ClassName: DateTimeUtils 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月26日 下午4:50:18  
 */
public class DateTimeUtils {

	   /** 
     * 默认日期格式 
     */  
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";  
    public static String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    
    public static boolean isSunday(){
    	 Calendar c=Calendar.getInstance();
         int weekDay=c.get(Calendar.DAY_OF_WEEK);
         if(weekDay==1){
        	 return true; 
         }
    	return false;
    }
	public static Date getCurrentDay(Date day) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(day);  
//        System.out.println("格式化后的日期：" + dateNowStr);  
//        String str = "2012-1-13 17:26:33";  //要跟上面sdf定义的格式一样  
        Date today = sdf.parse(dateNowStr);  
//        System.out.println("字符串转成日期：" + today); 
		return today;
	}
	/**
	 * 取本周7天的第一天（周一的日期）
	 */
	public static Date getNowWeekMonday() {
	    int mondayPlus;
	    Calendar cd = Calendar.getInstance();
	    // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
	    int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
	    if (dayOfWeek == 1) {
	        mondayPlus = 0;
	    } else {
	        mondayPlus = 1 - dayOfWeek;
	    }
	    GregorianCalendar currentDate = new GregorianCalendar();
	    currentDate.add(GregorianCalendar.DATE, mondayPlus);
	    Date monday = currentDate.getTime();
	    monday = DateUtils.truncate(monday, Calendar.DATE);
	    return monday;
	}
	/**
	 * 获取上周时间的第一天（上周一的日期）
	 */
    public static Date getLastWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getThisWeekMonday(date));  
        cal.add(Calendar.DATE, -7);  
        return cal.getTime();  
    }  
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
    //获取前月的第一天
	public static Date getLastMonthBegin() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar   cal_1=Calendar.getInstance();//获取当前日期 
	    cal_1.add(Calendar.MONTH, -1);
	    cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	    String firstDay = format.format(cal_1.getTime());
//	    System.out.println("-----1------firstDay:"+firstDay);
	    return format.parse(firstDay);
	}
	
	//获取前月的最后一天
	public static Date getLastMonthEnd() throws ParseException{
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar cale = Calendar.getInstance();   
		    cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		    String lastDay = format.format(cale.getTime());
//		    System.out.println("-----2------lastDay:"+lastDay);
		    return format.parse(lastDay);
	}
	 //获取当前月第一天：
	public static Date getNowMonthBegin() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();    
	    c.add(Calendar.MONTH, 0);
	    c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	    String first = format.format(c.getTime());
//	    System.out.println("===============first:"+first);
	    return format.parse(first);
	}
	
    //获取当前月最后一天
	public static Date getNowMonthEnd() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar ca = Calendar.getInstance();    
		    ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		    String last = format.format(ca.getTime());
//		    System.out.println("===============last:"+last);
		    return format.parse(last);
	}
  

	public static int daysOfTwo(Date startDate, Date endDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(startDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(endDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	    }
  
    /** 
     * 格式化日期 
     * @param date 日期对象 
     * @return String 日期字符串 
     */  
    public static String formatDate(Date date){  
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);  
        String sDate = f.format(date);  
        return sDate;  
    }  
    
    public static String formatDate2(Date date){  
        SimpleDateFormat f = new SimpleDateFormat(MINUTE_FORMAT);  
        String sDate = f.format(date);  
        return sDate;  
    } 
      
    /** 
     * 获取当年的第一天 
     * @param year 
     * @return 
     */  
    public static Date getCurrYearFirst(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearFirst(currentYear);  
    }  
      
    /** 
     * 获取当年的最后一天 
     * @param year 
     * @return 
     */  
    public static Date getCurrYearLast(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearLast(currentYear);  
    }  
      
    /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
    /**
     * 获取14天前日期 
     * @throws ParseException 
     */
    public static Date getFourteenDayBefore(int num) throws ParseException{
        Date date=new Date();//取时间  
        Calendar calendar = new GregorianCalendar();  
        calendar.setTime(date);  
        calendar.add(calendar.DATE,num);//把日期往后增加一天.整数往后推,负数往前移动  
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String dateString = formatter.format(date);  
//        System.out.println(dateString); 
    	return formatter.parse(dateString);
    }
    /**
     * 获取某天前日期 
     * @throws ParseException 
     */
    public static Date getOnedayByNum(int num,Date date) throws ParseException{
        Calendar calendar = new GregorianCalendar();  
        calendar.setTime(date);  
        calendar.add(calendar.DATE,num);//把日期往后增加一天.整数往后推,负数往前移动  
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果   
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String dateString = formatter.format(date);  
    	return formatter.parse(dateString);
    }
    /** 
     * 获取某月的最后一天 
     * @Title:getLastDayOfMonth 
     * @Description: 
     * @param:@param year 
     * @param:@param month 
     * @param:@return 
     * @return:String 
     * @throws 
     */  
    public static String getLastDayOfMonth(int year,int month)  
    {  
        Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最大天数  
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
        //设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, lastDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String lastDayOfMonth = sdf.format(cal.getTime());  
          
        return lastDayOfMonth;  
    }  
}
