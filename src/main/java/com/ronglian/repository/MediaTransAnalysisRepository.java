package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月18日 下午11:00:47
* @description:媒体转载分析
*/
public interface MediaTransAnalysisRepository {

	/**
	 * 媒体转载分析-媒体转载排行榜
	 * */
	String TransMediaBang(Integer mediaType,Integer channel,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-媒体转载排行榜,多参数
	 * */
	public String TransMediaBangSuper(Integer mediaType,Integer channel,int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime,int pageNo,int pageSize) throws IOException;
	/**
	 * 媒体转载分析-被转载的文章分类占比
	 * */
	String transArticleClassificationProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 媒体转载分析-媒体转载排行榜
	 * */
	String transArticleClassificationTop10(int classification,String other,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
	
	/**
	 * 媒体转载分析-转载时段占比
	 * */
	public String getTransPeriod(int accountType,String platformTypeId, List<String> platformIdList, Date startTime, Date endTime)
			throws IOException;
}
