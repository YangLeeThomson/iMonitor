package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月19日 上午9:53:41
* @description:描述
*/
public interface SingleArticleAnalysisRepository {

	/**
	 * 转载趋势
	 * */
	String platformsSpreadTrend(int accountType,List<String> platformList,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 评论发布时间趋势
	 * */
	String commentSpreadTimeTrend(int accountType,String articleUnionId,Date startTime,Date endTime)throws IOException;
	
	/**
	 * 评论地域分布
	 * */
	String commentSpreadArea(int accountType,String articleUnionId,Date startTime,Date endTime)throws IOException;
}
