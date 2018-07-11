package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 上午11:04:35
* @description:描述
*/
public interface HomePageRepository {
	/**
	 * 传播情况统计
	 * */
	String spreadCondition(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime) throws IOException ;
	
	/**
	 * 文章转载总数实时统计
	 * */
	String articleTransCount(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime) throws IOException ;
	
	/**
	 * 个平台作品占比
	 * */
	String platformProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	/**
	 * 平台组作品占比
	 * */
	String platformTypeProportion(int accountType,Date startTime,Date endTime)throws IOException;

	/**
	 * 原创文章占比
	 * */
	String originArticleProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 传播时间趋势
	 * */
	String spreadDateTrend(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 传播时间趋势天统计
	 * */
	String spreadDateTrendOneDay(int accountType, String platformTypeId, List<String> platformIdList, Date startTime,Date endTime) throws IOException;
	/**
	 * 传播时间趋势转载数实时统计
	 * */
	String spreadDateTrendTransCount(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 传播时间趋势转载数实时天统计
	 * */
	String spreadDateTrendTransCountOneDay(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 传播地域分布
	 * */
	String spreadAreaDistribution(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-转载媒体类型占比
	 * */
	String transMediaTypeProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-转载媒体类型列表
	 * */
	String transMediaTypeList(int mediaType,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
	
	/**
	 * 媒体转载分析-转载渠道占比
	 * */
	String transChannelProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-转载渠道列表
	 * */
	String transChannelList(int channel,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
	
	/**
	 * 媒体转载分析-被转载文章所属平台排行榜
	 * */
	String platformTransedBang(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-被转载文章所属平台列表
	 * */
	String transPlatformList(int accountType,String platformId,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
	
	/**
	 * 媒体转载分析-媒体转载排行榜
	 * */
	String TransMediaBang(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	/**
	 * 媒体转载分析-媒体转载列表
	 * */
	String transMediaNameList(int accountType,String mediaName,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
	
	
}
