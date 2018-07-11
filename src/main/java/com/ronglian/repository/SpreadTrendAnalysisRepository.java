package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午12:09:42
* @description:传播趋势分析
*/
public interface SpreadTrendAnalysisRepository {

	/**
	 * 传播时间趋势当日文章top 10
	 * */
	String spreadTimeTrendOneDayTop10(String platformTypeId,List<String> platformIdList,Date day,long start,long end,String orderField,int pageNo,int pageSize)throws IOException ;
	
	/**
	 * 传播时间趋势当日文章top 10
	 * */
	String spreadTimeTrendOneDayTop10TransNum(String platformTypeId,List<String> platformIdList,Date day,long start,long end,String orderField,int pageNo,int pageSize)throws IOException ;

	/**
	 * 作品发布时间趋势
	 * */
	String articlePublishTrend(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException ;
	/**
	 * 作品发布时间趋势天查询
	 * */
	String articlePublishTrendDay(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException ;
}
